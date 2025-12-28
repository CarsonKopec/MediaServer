package dev.imagineforgee.ms.server;

import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.FrameType;
import dev.imagineforgee.ms.shared.protocol.payload.ErrorPayload;
import dev.imagineforgee.ms.shared.protocol.payload.OkResponsePayload;

public final class Frames {

    public static Frame<OkResponsePayload> ok(String id, String op) {
        return new Frame<>(
                1,
                id,
                FrameType.RESPONSE,
                op,
                OkResponsePayload.ok(),
                null
        );
    }

    public static Frame<ErrorPayload> error(
            String id,
            String op,
            String code,
            String message
    ) {
        return new Frame<>(
                1,
                id,
                FrameType.ERROR,
                op,
                new ErrorPayload(code, message),
                null
        );
    }
}
