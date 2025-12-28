package dev.imagineforgee.ms.server.commands;

import dev.imagineforgee.ms.server.CommandHandler;
import dev.imagineforgee.ms.server.Frames;
import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.*;
import dev.imagineforgee.ms.shared.protocol.payload.StopPayload;
import reactor.core.publisher.Mono;

public final class StopCommand implements CommandHandler<StopPayload> {

    @Override
    public String op() {
        return "stop";
    }

    @Override
    public Class<StopPayload> payloadType() {
        return StopPayload.class;
    }

    @Override
    public Mono<Void> handle(WsSession session, StopPayload payload, Frame<?> frame) {

        return session.send(
                Frames.ok(frame.id(), frame.op())
        );
    }
}
