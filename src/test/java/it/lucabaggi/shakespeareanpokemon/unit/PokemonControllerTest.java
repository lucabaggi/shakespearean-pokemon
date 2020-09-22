package it.lucabaggi.shakespeareanpokemon.unit;

import it.lucabaggi.shakespeareanpokemon.controller.PokemonController;
import it.lucabaggi.shakespeareanpokemon.model.Pokemon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PokemonController unit test")
@ExtendWith(MockitoExtension.class)
public class PokemonControllerTest {

    private static final String POKEMON_NAME = "Charizard";

    //sut
    private PokemonController pokemonController;

    @BeforeEach
    public void setUp() {
        pokemonController = new PokemonController();
    }

    @Test
    @DisplayName("Should return pokemon name and description")
    public void shouldReturnOkWithPokemonInfo() {
        ResponseEntity<Pokemon> response = pokemonController.getShakespeareanPokemonInfo(POKEMON_NAME);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        Pokemon pokemon = response.getBody();
        assertEquals(POKEMON_NAME, pokemon.getName());
        assertEquals("test", pokemon.getDescription());
    }
}
