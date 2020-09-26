package it.lucabaggi.shakespeareanpokemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@EnableCircuitBreaker
@SpringBootApplication
public class ShakespeareanPokemonApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShakespeareanPokemonApplication.class, args);
	}

}
