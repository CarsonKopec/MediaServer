package dev.imagineforgee.ms.server.ws;


import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import dev.imagineforgee.ms.shared.protocol.Frame;
import dev.imagineforgee.ms.shared.protocol.payload.Payload;
import reactor.core.publisher.Mono;
import reactor.netty.http.websocket.WebsocketOutbound;

import java.lang.reflect.Type;

public final class WsSession {

    private final WebsocketOutbound outbound;
    private final Moshi moshi;
    private final JsonAdapter<Frame<Payload>> frameAdapter;

    public WsSession(WebsocketOutbound outbound, Moshi moshi) {
        this.outbound = outbound;
        this.moshi = moshi;

        Type frameType = Types.newParameterizedType(Frame.class, Payload.class);
        this.frameAdapter = moshi.adapter(frameType);
    }

    public Mono<Void> send(Frame<?> frame) {
        try {
            @SuppressWarnings("unchecked")
            Frame<Payload> castFrame = (Frame<Payload>) frame;

            String json = frameAdapter.toJson(castFrame);
            return outbound.sendString(Mono.just(json)).then();
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
