package dev.imagineforgee.ms.server.commands;

import dev.imagineforgee.ms.server.CommandHandler;
import dev.imagineforgee.ms.server.EventBus;
import dev.imagineforgee.ms.server.MediaServerState;
import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.*;
import dev.imagineforgee.ms.shared.protocol.payload.PlayPayload;
import dev.imagineforgee.ms.shared.protocol.payload.StateUpdatePayload;
import reactor.core.publisher.Mono;

public final class PlayCommand implements CommandHandler<PlayPayload> {

    @Override
    public String op() {
        return "play";
    }

    @Override
    public Class<PlayPayload> payloadType() {
        return PlayPayload.class;
    }

    @Override
    public Mono<Void> handle(WsSession session, PlayPayload payload, Frame<?> frame) {
        MediaServerState.STATE.setPlaying(true);
        MediaServerState.STATE.setPosition(payload.position());
        System.out.println("Playing: " + payload.mediaId() + " from " + payload.position() + "ms");
        StateUpdatePayload statePayload = new StateUpdatePayload(
                MediaServerState.STATE.isPlaying(),
                MediaServerState.STATE.getPosition(),
                MediaServerState.STATE.getDuration(),
                MediaServerState.STATE.getVolume(),
                MediaServerState.STATE.getSpeed()
        );

        Frame<PlayPayload> response = new Frame<>(
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
