package com.attraya.client;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/bookmyshow-client")
public class BookMyShowWebclientApplication {

	WebClient webClient;

	@PostConstruct
	public void init(){
		webClient= WebClient.builder().baseUrl("http://localhost:9090/bookmyshow-provider")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	@PostMapping("/book")
	public Mono<String> bookNow(@RequestBody BookRequest bookRequest){
		return webClient.post().uri("/book").syncBody(bookRequest).retrieve().bodyToMono(String.class);
	}

	@GetMapping("/trackBookings")
	public Flux<BookRequest> trackAllBooking(){
		return webClient.get().uri("/getAllBookings").retrieve().bodyToFlux(BookRequest.class);
	}

	@GetMapping("trackBooking/{bookingId}")
	public Mono<BookRequest> getBookingById(@PathVariable int bookingId){
		return webClient.get().uri("/getBooking/"+bookingId).retrieve().bodyToMono(BookRequest.class);
	}

	@DeleteMapping("deleteBooking/{bookingId}")
	public Mono<String> deleteBookingById(@PathVariable int bookingId){
		return webClient.delete().uri("/cancelBooking/"+bookingId).retrieve().bodyToMono(String.class);
	}

	@PutMapping("/changeBooking/{bookingId}")
	public Mono<BookRequest> updateBooking(@PathVariable int bookingId, @RequestBody BookRequest bookRequest){
		return webClient.put().uri("/updateBooking/"+bookingId).syncBody(bookRequest).retrieve().bodyToMono(BookRequest.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(BookMyShowWebclientApplication.class, args);
	}

}
