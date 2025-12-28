package dev.imagineforgee.ms.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import dev.imagineforgee.ms.server.commands.*;
import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.ProtocolMoshi;
import dev.imagineforgee.ms.shared.protocol.payload.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CommandDispatcher {

    private static final Map<String, CommandHandler<?>> HANDLERS = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandDispatcher.class);
    private static final Moshi MOSHI = ProtocolMoshi.moshi();

    static {
        register(new SeekCommand());
        register(new PingCommand());
        register(new PauseCommand());
        register(new PlayCommand());
        register(new StopCommand());
        register(new ResumeCommand());
        register(new SetVolumeCommand());
        register(new SetSpeedCommand());
        register(new ListMediaCommand());
        register(new GetStateCommand());
    }

    private static void register(CommandHandler<?> handler) {
        HANDLERS.put(handler.op(), handler);
    }

    @SuppressWarnings("unchecked")
    public static Mono<Void> dispatch(WsSession session, Frame<?> frame) {
        CommandHandler<?> raw = HANDLERS.get(frame.op());

        if (raw == null) {
            return session.send(Frames.error(
                    frame.id(),
                    frame.op(),
                    "UNKNOWN_COMMAND",
                    "No handler for op=" + frame.op()
            ));
        }

        return invoke(raw, session, frame);
    }

    @SuppressWarnings("unchecked")
    private static <P extends Payload> Mono<Void> invoke(
            CommandHandler<P> handler,
            WsSession session,
            Frame<?> frame
    ) {
        Object rawPayload = frame.payload();
        if (rawPayload == null) {
            return session.send(Frames.error(frame.id(), frame.op(), "INVALID_PAYLOAD", "Payload is missing"));
        }

        try {
            JsonAdapter<Object> mapAdapter = MOSHI.adapter(Object.class);
            String json = mapAdapter.toJson(rawPayload);

            Class<P> payloadType = handler.payloadType();
            JsonAdapter<P> payloadAdapter = MOSHI.adapter(payloadType);

            P payload = payloadAdapter.fromJson(json);
            if (payload == null) {
                return session.send(Frames.error(frame.id(), frame.op(), "INVALID_PAYLOAD", "Payload is null"));
            }

            return handler.handle(session, payload, frame);
        } catch (Exception ex) {
            return session.send(Frames.error(frame.id(), frame.op(), "INVALID_PAYLOAD", ex.getMessage()));
        }
    }


}
