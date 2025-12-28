package dev.imagineforgee.ms.server.commands;

import dev.imagineforgee.ms.server.CommandHandler;
import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.FrameType;
import dev.imagineforgee.ms.shared.protocol.payload.SetSpeedPayload;
import reactor.core.publisher.Mono;

public final class SetSpeedCommand implements CommandHandler<SetSpeedPayload> {

    @Override
    public String op() {
        return "set_speed";
    }

    @Override
    public Class<SetSpeedPayload> payloadType() {
        return SetSpeedPayload.class;
    }

    @Override
    public Mono<Void> handle(WsSession session, SetSpeedPayload payload, Frame<?> frame) {

        Frame<SetSpeedPayload> response = new Frame<>(
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
