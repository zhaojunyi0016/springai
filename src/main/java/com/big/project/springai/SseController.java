package com.big.project.springai;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

/**
 *
 */
@RestController
public class SseController {

    @GetMapping ("/sse")
    public SseEmitter post() {
        SseEmitter sse = new SseEmitter(60_000L);
        Executors.newFixedThreadPool(10).submit(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    sse.send("i==== " + i);
                }
            } catch (IOException e) {
                sse.completeWithError(e);
            } finally {
                sse.complete();
            }
        });
        return sse;
    }

    @GetMapping("/sse/streaming")
    public ResponseEntity<StreamingResponseBody> chat() {
        StreamingResponseBody body = outputStream -> {
            for (int i = 0; i < 10; i++) {
                String data = "data chunk " + i + "\n";
                outputStream.write(data.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                try {
                    Thread.sleep(500); // 模拟延迟
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
                .body(body);
    }
}
