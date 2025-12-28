package dev.imagineforgee.ms.server.commands;

import dev.imagineforgee.ms.server.CommandHandler;
import dev.imagineforgee.ms.server.EventBus;
import dev.imagineforgee.ms.server.MediaServerState;
import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.*;
import dev.imagineforgee.ms.shared.protocol.payload.PausePayload;
import dev.imagineforgee.ms.shared.protocol.payload.StateUpdatePayload;
import reactor.core.publisher.Mono;

public final class PauseCommand implements CommandHandler<PausePayload> {

    @Override
    public String op() {
        return "pause";
    }

    @Override
    public Class<PausePayload> payloadType() {
        return PausePayload.class;
    }

    @Override
    public Mono<Void> handle(WsSession session, PausePayload payload, Frame<?> frame) {

        StateUpdatePayload statePayload = new StateUpdatePayload(
                false,
                MediaServerState.STATE.getPosition(),
                MediaServerState.STATE.getDuration(),
                MediaServerState.STATE.getVolume(),
                MediaServerState.STATE.getSpeed()
        );

        Frame<PausePayload> response = new Frame<>(
                frame.v(),
                frame.id(),
                FrameType.RESPONSE,
                frame.op(),
                payload,
                System.currentTimeMillis()
        );

        return EventBus.broadcast(EventType.STATE_UPDATE, "state_update", statePayload)
                .then(session.send(response));
    }
}
