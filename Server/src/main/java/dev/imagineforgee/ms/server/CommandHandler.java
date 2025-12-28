package dev.imagineforgee.ms.server;

import dev.imagineforgee.ms.server.ws.WsSession;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.payload.Payload;
import reactor.core.publisher.Mono;

public interface CommandHandler<T extends Payload> {

    String op();

    Class<T> payloadType();

    Mono<Void> handle(
            WsSession session,
            T payload,
            Frame<?> frame
    );
}
