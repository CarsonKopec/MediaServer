package dev.imagineforgee.ms.server;

import dev.imagineforgee.ms.server.ws.StreamerHandler;
import dev.imagineforgee.ms.server.ws.WebSocketHandler;
import io.netty.channel.ChannelOption;
import reactor.netty.http.server.HttpServer;

public class MediaServer {
    public static void main(String[] args) {
        HttpServer.create()
                .port(8080)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .route(routes ->
                        routes.ws("/ws", WebSocketHandler::handle)
                                .get("/stream", StreamerHandler::handle)
                )
                .bindNow()
                .onDispose()
                .block();
    }
}
