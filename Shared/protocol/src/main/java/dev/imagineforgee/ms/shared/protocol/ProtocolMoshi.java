package dev.imagineforgee.ms.shared.protocol;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;
import dev.imagineforgee.ms.shared.protocol.payload.*;

public final class ProtocolMoshi {

    private static final Moshi MOSHI = new Moshi.Builder()
            .add(
                    PolymorphicJsonAdapterFactory.of(Payload.class, "op")
                            .withSubtype(PlayPayload.class, "play")
                            .withSubtype(PausePayload.class, "pause")
                            .withSubtype(SeekPayload.class, "seek")
                            .withSubtype(StopPayload.class, "stop")
                            .withSubtype(SetVolumePayload.class, "set_volume")
                            .withSubtype(SetSpeedPayload.class, "set_speed")
                            .withSubtype(ResumePayload.class, "resume")
                            .withSubtype(GetStatePayload.class, "get_state")
                            .withSubtype(ListMediaPayload.class, "list_media")
                            .withSubtype(StateUpdatePayload.class, "state_update")
                            .withSubtype(MediaChangedPayload.class, "media_changed")
                            .withSubtype(VolumeChangedPayload.class, "volume_changed")
                            .withSubtype(OkResponsePayload.class, "ok")
                            .withSubtype(ErrorPayload.class, "error")
                            .withSubtype(PingPayload.class, "ping")
            )
            .add(new KotlinJsonAdapterFactory())
            .build();

    public static Moshi moshi() {
        return MOSHI;
    }
}
