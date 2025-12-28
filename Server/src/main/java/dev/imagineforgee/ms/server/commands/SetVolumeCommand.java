package dev.imagineforgee.ms.server.commands;

import dev.imagineforgee.ms.server.CommandHandler;
import dev.imagineforgee.ms.server.EventBus;
import dev.imagineforgee.ms.server.MediaServerState;
import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.EventType;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.FrameType;
import dev.imagineforgee.ms.shared.protocol.payload.SetVolumePayload;
import dev.imagineforgee.ms.shared.protocol.payload.StateUpdatePayload;
import reactor.core.publisher.Mono;

public final class SetVolumeCommand implements CommandHandler<SetVolumePayload> {

    @Override
    public String op() {
        return "set_volume";
    }

    @Override
    public Class<SetVolumePayload> payloadType() {
        return SetVolumePayload.class;
    }

    @Override
    public Mono<Void> handle(WsSession session, SetVolumePayload payload, Frame<?> frame) {
        MediaServerState.STATE.setVolume(payload.volume());

        StateUpdatePayload statePayload = new StateUpdatePayload(
                MediaServerState.STATE.isPlaying(),
                MediaServerState.STATE.getPosition(),
                MediaServerState.STATE.getDuration(),
                MediaServerState.STATE.getVolume(),
                MediaServerState.STATE.getSpeed()
        );

        Frame<SetVolumePayload> response = new Frame<>(
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
