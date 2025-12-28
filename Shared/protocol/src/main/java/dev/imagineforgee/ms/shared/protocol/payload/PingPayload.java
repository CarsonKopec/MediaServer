package dev.imagineforgee.ms.shared.protocol.payload;

public record PingPayload(String device, String deviceName, long timestamp) implements Payload {}
