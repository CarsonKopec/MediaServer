package dev.imagineforgee.ms.server.commands;

import dev.imagineforgee.ms.server.CommandHandler;
import dev.imagineforgee.ms.server.Frames;
import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.FrameType;
import dev.imagineforgee.ms.shared.protocol.payload.PingPayload;
import reactor.core.publisher.Mono;

public final class PingCommand implements CommandHandler<PingPayload> {

    @Override
    public String op() {
        return "ping";
    }

    @Override
    public Class<PingPayload> payloadType() {
        return PingPayload.class;
    }

    @Override
    public Mono<Void> handle(WsSession session, PingPayload payload, Frame<?> frame) {

        Frame<PingPayload> response = new Frame<>(
                frame.v(),
                frame.id(),
                FrameType.RESPONSE,
                frame.op(),
                payload,
                System.currentTimeMillis()
        );

        return session.send(response);
    }


}
