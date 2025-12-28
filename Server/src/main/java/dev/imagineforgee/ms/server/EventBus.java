package dev.imagineforgee.ms.server;

import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.EventType;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.FrameType;
import dev.imagineforgee.ms.shared.protocol.payload.Payload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public final class EventBus {

    private static final List<WsSession> CLIENTS = new java.util.concurrent.CopyOnWriteArrayList<>();

    public static void register(WsSession session) {
        CLIENTS.add(session);
    }

    public static void unregister(WsSession session) {
        CLIENTS.remove(session);
    }

    public static Mono<Void> broadcast(EventType type, String op, Payload payload) {
        Frame<Payload> frame = new Frame<>(
                1,
                null,
                FrameType.EVENT,
                op,
                payload,
                System.currentTimeMillis()
        );

        return Flux.fromIterable(CLIENTS)
                .flatMap(session -> session.send(frame))
                .then();
    }
}
