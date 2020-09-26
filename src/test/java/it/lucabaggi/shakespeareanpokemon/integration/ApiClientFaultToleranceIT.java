package it.lucabaggi.shakespeareanpokemon.integration;

import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.PokeapiClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.stream.IntStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static it.lucabaggi.shakespeareanpokemon.common.Configuration.POKEMON_SPECIES_RESPONSE_PATH;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ApiClient fault tolerance integration test")
public class ApiClientFaultToleranceIT extends BaseIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ApiClientFaultToleranceIT.class);

    private static final String POKEMON_SPECIES_ENDPOINT = "/pokemon-species/%s";
    private static final String POKEMON_NAME = "charizard";
    private static final String INVALID_POKEMON_NAME = "an_invalid_pokemon_name";

    @Autowired
    private PokeapiClient pokeapiClient;

    @Value("${hystrix.command.getPokemonSpecies.execution.isolation.thread.timeoutInMilliseconds}")
    private Integer hystrixTimeout;

    @Value("${hystrix.command.getPokemonSpecies.circuitBreaker.requestVolumeThreshold}")
    private Integer hystrixRequestVolumeThreshold;

    @Value("${hystrix.command.getPokemonSpecies.metrics.healthSnapshot.intervalInMilliseconds}")
    private Integer hystrixHealthSnapshotInterval;

    @Test
    @DisplayName("Should throw HystrixRuntimeException after configured timeout")
    public void shouldThrowHystrixRuntimeExceptionOnTimeout() {
        stubDelayedPokemonSpeciesResponse();
        assertThrows(HystrixRuntimeException.class, () -> pokeapiClient.getPokemonSpecies(POKEMON_NAME));
    }

    @Test
    @DisplayName("Should open circuit after threshold of failure calls is reached")
    public void shouldOpenCircuitAfterThreshold() throws InterruptedException {
        stubFailurePokemonSpeciesResponse();
        HystrixCircuitBreaker circuitBreaker = getCircuitBreaker();

        assertFalse(circuitBreaker.isOpen());
        assertTrue(circuitBreaker.allowRequest());

        //fail pokemonSpecies call as many times as configured threshold
        IntStream.range(0, hystrixRequestVolumeThreshold)
                .forEach(value -> failRemoteCallIgnoringException());

        waitUntilCircuitBreakerOpens();
        assertTrue(circuitBreaker.isOpen());
        assertFalse(circuitBreaker.allowRequest());
    }

    /**
     * The property hystrixHealthSnapshotInterval is the time to wait, in milliseconds, between allowing
     * health snapshots to be taken that calculate success and error percentages and affect circuit breaker status
     * @throws InterruptedException
     */
    private void waitUntilCircuitBreakerOpens() throws InterruptedException {
        Thread.sleep(hystrixHealthSnapshotInterval);
    }

    private void stubDelayedPokemonSpeciesResponse() {
        mockServer.stubFor(get(urlEqualTo(String.format(POKEMON_SPECIES_ENDPOINT, POKEMON_NAME)))
                .willReturn(ok().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withFixedDelay(hystrixTimeout)
                        .withBodyFile(POKEMON_SPECIES_RESPONSE_PATH)));
    }

    private void stubFailurePokemonSpeciesResponse() {
        mockServer.stubFor(get(urlEqualTo(String.format(POKEMON_SPECIES_ENDPOINT, INVALID_POKEMON_NAME)))
                .willReturn(notFound()));
    }

    private void failRemoteCallIgnoringException() {
        try {
            pokeapiClient.getPokemonSpecies(INVALID_POKEMON_NAME);
        } catch (Throwable e) {
            //log exception and skip it
            log.debug("Ignoring exception for testing purposes");
        }
    }

    private static HystrixCircuitBreaker getCircuitBreaker() {
        return HystrixCircuitBreaker.Factory.getInstance(getCommandKey());
    }

    private static HystrixCommandKey getCommandKey() {
        return HystrixCommandKey.Factory.asKey("getPokemonSpecies");
    }
}
