package dev.imagineforgee.ms.shared.protocol.payload;


public record PlayPayload(
        String mediaId,
        Double position
) implements Payload {}

