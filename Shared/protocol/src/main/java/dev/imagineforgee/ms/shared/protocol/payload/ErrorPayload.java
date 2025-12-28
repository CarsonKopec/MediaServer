package dev.imagineforgee.ms.shared.protocol.payload;

public record ErrorPayload(String code, String message) implements Payload {}
