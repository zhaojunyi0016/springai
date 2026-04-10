package com.big.project.springai;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/webFlux")
public class WebFluxController {


    @GetMapping(value = "/flux")
    public Flux<String> fluxStream() {
        return Flux.interval(Duration.ofSeconds(1)).map(seq -> "Stream element - " + seq);
    }
}
