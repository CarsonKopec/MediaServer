package dev.imagineforgee.ms.shared.protocol.payload;

public sealed interface Payload permits ErrorPayload, GetStatePayload, ListMediaPayload, MediaChangedPayload, OkResponsePayload, PausePayload, PingPayload, PlayPayload, ResumePayload, SeekPayload, SetSpeedPayload, SetVolumePayload, StateUpdatePayload, StopPayload, VolumeChangedPayload {}
