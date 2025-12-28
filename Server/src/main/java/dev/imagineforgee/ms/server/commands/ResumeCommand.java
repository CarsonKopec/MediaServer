package dev.imagineforgee.ms.server.commands;

import dev.imagineforgee.ms.server.CommandHandler;
import dev.imagineforgee.ms.server.EventBus;
import dev.imagineforgee.ms.server.Frames;
import dev.imagineforgee.ms.server.MediaServerState;
import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.EventType;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.FrameType;
import dev.imagineforgee.ms.shared.protocol.payload.ResumePayload;
import dev.imagineforgee.ms.shared.protocol.payload.StateUpdatePayload;
import reactor.core.publisher.Mono;

public final class ResumeCommand implements CommandHandler<ResumePayload> {

    @Override
    public String op() {
        return "resume";
    }

    @Override
    public Class<ResumePayload> payloadType() {
        return ResumePayload.class;
    }

    @Override
    public Mono<Void> handle(WsSession session, ResumePayload payload, Frame<?> frame) {

        StateUpdatePayload statePayload = new StateUpdatePayload(
                true,
                MediaServerState.STATE.getPosition(),
                MediaServerState.STATE.getDuration(),
                MediaServerState.STATE.getVolume(),
                MediaServerState.STATE.getSpeed()
        );

        Frame<ResumePayload> response = new Frame<>(
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
