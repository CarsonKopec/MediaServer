package dev.imagineforgee.ms.server.ws;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import dev.imagineforgee.ms.server.CommandDispatcher;
import dev.imagineforgee.ms.server.Frames;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.ProtocolMoshi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

import java.lang.reflect.Type;
import java.util.Map;

public final class WebSocketHandler {

    private final static Moshi MOSHI = ProtocolMoshi.moshi();
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);

    private static final Type frameType = Types.newParameterizedType(Frame.class, Map.class);
    private static final JsonAdapter<Frame<Map<String, Object>>> frameAdapter = MOSHI.adapter(frameType);

    public static Mono<Void> handle(
            WebsocketInbound in,
            WebsocketOutbound out
    ) {
        WsSession session = new WsSession(out, MOSHI);

        return in.receive()
                .asString()
                .flatMap(json -> handleFrame(session, json))
                .then();
    }

    public static Mono<Void> handleFrame(WsSession session, String json) {
        try {
            Frame<Map<String, Object>> rawFrame = frameAdapter.fromJson(json);

            if (rawFrame == null) {
                return session.send(Frames.error(null, null, "INVALID_PAYLOAD", "Could not parse frame"));
            }

            return switch (rawFrame.type()) {
                case COMMAND -> CommandDispatcher.dispatch(session, rawFrame);
                default -> Mono.empty();
            };

        } catch (Exception e) {
            LOGGER.error("Failed to handle frame", e);
            return Mono.error(e);
        }
    }
}
