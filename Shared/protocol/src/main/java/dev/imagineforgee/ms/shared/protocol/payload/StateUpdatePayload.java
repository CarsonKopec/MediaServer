package dev.imagineforgee.ms.shared.protocol.payload;

public record StateUpdatePayload(
        boolean playing,
        double position,
        double duration,
        int volume,
        double speed
) implements Payload {}
