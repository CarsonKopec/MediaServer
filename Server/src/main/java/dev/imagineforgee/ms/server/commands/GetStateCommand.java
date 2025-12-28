package dev.imagineforgee.ms.server.commands;

import dev.imagineforgee.ms.server.CommandHandler;
import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.FrameType;
import dev.imagineforgee.ms.shared.protocol.payload.GetStatePayload;
import reactor.core.publisher.Mono;

public final class GetStateCommand implements CommandHandler<GetStatePayload> {

    @Override
    public String op() {
        return "get_state";
    }

    @Override
    public Class<GetStatePayload> payloadType() {
        return GetStatePayload.class;
    }

    @Override
    public Mono<Void> handle(WsSession session, GetStatePayload payload, Frame<?> frame) {

        Frame<GetStatePayload> response = new Frame<>(
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