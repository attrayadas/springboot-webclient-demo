package com.attraya.client;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/bookmyshow-client")
public class BookMyShowWebclientApplication {

    Logger logger = LoggerFactory.getLogger(BookMyShowWebclientApplication.class);

    WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder().baseUrl("http://localhost:9090/bookmyshow-provider")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    @PostMapping("/book")
    public Mono<String> bookNow(@RequestBody BookRequest bookRequest) {
        return webClient.post().uri("/book").syncBody(bookRequest).retrieve().bodyToMono(String.class);
    }

    @GetMapping("/trackBookings")
    public Flux<BookRequest> trackAllBooking() {
        return webClient.get().uri("/getAllBookings").retrieve().bodyToFlux(BookRequest.class);
    }

    @GetMapping("trackBooking/{bookingId}")
    public Mono<BookRequest> getBookingById(@PathVariable int bookingId) {
        return webClient.get().uri("/getBooking/" + bookingId).retrieve().bodyToMono(BookRequest.class);
    }

    @DeleteMapping("deleteBooking/{bookingId}")
    public Mono<String> deleteBookingById(@PathVariable int bookingId) {
        return webClient.delete().uri("/cancelBooking/" + bookingId).retrieve().bodyToMono(String.class);
    }

    @PutMapping("/changeBooking/{bookingId}")
    public Mono<BookRequest> updateBooking(@PathVariable int bookingId, @RequestBody BookRequest bookRequest) {
        return webClient.put().uri("/updateBooking/" + bookingId).syncBody(bookRequest).retrieve().bodyToMono(BookRequest.class);
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            logger.info("Request {} {}", clientRequest.method(), clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logger.info("Response status code {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(BookMyShowWebclientApplication.class, args);
    }

}
