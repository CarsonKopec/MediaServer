package dev.imagineforgee.ms.shared.protocol.payload;

public record OkResponsePayload(String status, String result) implements Payload {
    public static OkResponsePayload ok() {
        return new OkResponsePayload("ok", null);
    }
}
