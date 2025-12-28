package dev.imagineforgee.ms.server.ws;

import dev.imagineforgee.ms.server.media.MediaStreamer;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public class StreamerHandler {

    private static final String STREAM_URL = "http://localhost:8081/stream.ts";

    public static Mono<Void> handle(HttpServerRequest req, HttpServerResponse res) {

        // Start streamer once
        if (!MediaStreamer.INSTANCE.isRunning()) {
            MediaStreamer.INSTANCE.start(
                    "C:\\Users\\kopec\\Downloads\\index(3).m3u8",
                    true
            );
        }

        boolean isCustomClient = req.requestHeaders()
                .contains("X-Media-Client");

        boolean wantsHtml = req.requestHeaders()
                .get("Accept", "")
                .contains("text/html");

        // 🧠 Custom program → JSON
        if (isCustomClient || !wantsHtml) {
            res.header("Content-Type", "application/json");
            return res.sendString(
                    Mono.just("{\"streamUrl\":\"" + STREAM_URL + "\"}")
            ).then();
        }

        // 🌐 Browser → HTML player page
        res.header("Content-Type", "text/html");
        return res.sendString(
                Mono.just(htmlPage(STREAM_URL))
        ).then();
    }

    private static String htmlPage(String streamUrl) {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Live Stream</title>
                <style>
                    body {
                        margin: 0;
                        background: black;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                    }
                    video {
                        width: 100%%;
                        height: 100%%;
                    }
                </style>
            </head>
            <body>
                <video controls autoplay>
                    <source src="%s" type="video/mp2t">
                    Your browser does not support the video tag.
                </video>
            </body>
            </html>
            """.formatted(streamUrl);
    }
}
