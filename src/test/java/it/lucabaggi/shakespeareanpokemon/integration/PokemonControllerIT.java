package it.lucabaggi.shakespeareanpokemon.integration;

import it.lucabaggi.shakespeareanpokemon.common.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static it.lucabaggi.shakespeareanpokemon.common.Configuration.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("PokemonController integration test")
public class PokemonControllerIT extends BaseIntegrationTest {

    private static final String POKEMON_INFO_ENDPOINT = "/pokemon/%s";
    private static final String POKEMON_SPECIES_ENDPOINT = "/pokemon-species/%s";
    private static final String TRANSLATOR_ENDPOINT = "/translate/shakespeare.json";
    private static final String POKEMON_NAME = "charizard";
    private static final String POKEMON_DESCRIPTION = "Charizard flies 'round the sky in search of powerful opponents. 't breathes fire of such most wondrous heat yond 't melts aught. However,  't nev'r turns its fiery breath on any opponent weaker than itself.";

    @Test
    @DisplayName("Should return pokemon name and description")
    public void shouldReturnOkWithPokemonInfo() throws Exception {
        stubValidPokemonSpeciesResponse();
        stubSuccessfulTranslatorResponse();
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(POKEMON_INFO_ENDPOINT, POKEMON_NAME)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(POKEMON_NAME))
                .andExpect(jsonPath("$.description").value(POKEMON_DESCRIPTION));
    }

    @Test
    @DisplayName("Should return internal server error if pokemon description is missing")
    public void shouldReturnInternalServerErrorWithoutPokemonDescription() throws Exception {
        stubPokemonSpeciesResponseWithoutDescription();
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(POKEMON_INFO_ENDPOINT, POKEMON_NAME)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(ErrorCode.INTERNAL.getCode()));
    }

    @Test
    @DisplayName("Should return bad gateway error if remote service is not available")
    public void shouldReturnBadGatewayWithRemoteServiceError() throws Exception {
        stubValidPokemonSpeciesResponse();
        stubTooManyRequestsErrorTranslatorResponse();
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(POKEMON_INFO_ENDPOINT, POKEMON_NAME)))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.code").value(ErrorCode.REMOTE.getCode()))
                .andExpect(jsonPath("$.remoteError").value(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase()));
    }


    private void stubValidPokemonSpeciesResponse() {
        mockServer.stubFor(get(urlEqualTo(String.format(POKEMON_SPECIES_ENDPOINT, POKEMON_NAME)))
                .willReturn(ok().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile(POKEMON_SPECIES_RESPONSE_PATH)));
    }

    private void stubPokemonSpeciesResponseWithoutDescription() {
        mockServer.stubFor(get(urlEqualTo(String.format(POKEMON_SPECIES_ENDPOINT, POKEMON_NAME)))
                .willReturn(ok().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile(POKEMON_SPECIES_WITHOUT_DESCRIPTIONRESPONSE_PATH)));
    }

    private void stubSuccessfulTranslatorResponse() {
        mockServer.stubFor(post(TRANSLATOR_ENDPOINT)
                .willReturn(ok().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile(TRANSLATOR_SUCCESS_RESPONSE_PATH)));

    }

    private void stubTooManyRequestsErrorTranslatorResponse() {
        mockServer.stubFor(post(TRANSLATOR_ENDPOINT)
                .willReturn(status(HttpStatus.TOO_MANY_REQUESTS.value())
                        .withBodyFile(TRANSLATOR_ERROR_RESPONSE_PATH)));

    }


}
