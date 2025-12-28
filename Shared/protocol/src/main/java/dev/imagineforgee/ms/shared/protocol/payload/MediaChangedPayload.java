package dev.imagineforgee.ms.shared.protocol.payload;

public record MediaChangedPayload(
        String mediaId,
        String title,
        double duration
) implements Payload {}
