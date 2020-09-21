package it.lucabaggi.shakespeareanpokemon.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("PokemonController integration test")
public class PokemonControllerIT extends BaseIntegrationTest {

    private static final String POKEMON_INFO_ENDPOINT = "/pokemon/%s";
    private static final String POKEMON_NAME = "charizard";

    @Test
    @DisplayName("Should return pokemon name and description")
    public void shouldReturnOkWithPokemonInfo() throws Exception {
        mockMvc.perform(get(String.format(POKEMON_INFO_ENDPOINT, POKEMON_NAME)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(POKEMON_NAME))
                .andExpect(jsonPath("$.description").value("test"));
    }

}
