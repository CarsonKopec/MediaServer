package dev.imagineforgee.ms.server.commands;

import dev.imagineforgee.ms.server.CommandHandler;
import dev.imagineforgee.ms.server.EventBus;
import dev.imagineforgee.ms.server.Frames;
import dev.imagineforgee.ms.server.MediaServerState;
import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.EventType;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.FrameType;
import dev.imagineforgee.ms.shared.protocol.payload.SeekPayload;
import dev.imagineforgee.ms.shared.protocol.payload.StateUpdatePayload;
import reactor.core.publisher.Mono;

public final class SeekCommand implements CommandHandler<SeekPayload> {

    @Override
    public String op() {
        return "seek";
    }

    @Override
    public Class<SeekPayload> payloadType() {
        return SeekPayload.class;
    }

    @Override
    public Mono<Void> handle(WsSession session, SeekPayload payload, Frame<?> frame) {
		MediaServerState.STATE.setPosition(payload.position());
        StateUpdatePayload statePayload = new StateUpdatePayload(
                MediaServerState.STATE.isPlaying(),
                MediaServerState.STATE.getPosition(),
                MediaServerState.STATE.getDuration(),
                MediaServerState.STATE.getVolume(),
                MediaServerState.STATE.getSpeed()
        );

        Frame<SeekPayload> response = new Frame<>(
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
