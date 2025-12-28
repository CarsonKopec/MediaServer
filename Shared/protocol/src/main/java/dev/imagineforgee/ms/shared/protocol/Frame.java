package dev.imagineforgee.ms.shared.protocol;

public record Frame<T>(
        int v,
        String id,
        FrameType type,
        String op,
        T payload,
        Long ts
) {}
