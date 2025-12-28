package dev.imagineforgee.ms.server.commands;

import dev.imagineforgee.ms.server.CommandHandler;
import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.FrameType;
import dev.imagineforgee.ms.shared.protocol.payload.ListMediaPayload;
import reactor.core.publisher.Mono;

public final class ListMediaCommand implements CommandHandler<ListMediaPayload> {

    @Override
    public String op() {
        return "list_media";
    }

    @Override
    public Class<ListMediaPayload> payloadType() {
        return ListMediaPayload.class;
    }

    @Override
    public Mono<Void> handle(WsSession session, ListMediaPayload payload, Frame<?> frame) {

        Frame<ListMediaPayload> response = new Frame<>(
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
