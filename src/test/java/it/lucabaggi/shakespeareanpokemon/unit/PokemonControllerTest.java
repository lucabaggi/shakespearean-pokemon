package it.lucabaggi.shakespeareanpokemon.unit;

import it.lucabaggi.shakespeareanpokemon.controller.PokemonController;
import it.lucabaggi.shakespeareanpokemon.model.Pokemon;
import it.lucabaggi.shakespeareanpokemon.service.PokemonService;
import it.lucabaggi.shakespeareanpokemon.service.PokemonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PokemonController unit test")
@ExtendWith(MockitoExtension.class)
public class PokemonControllerTest {

    private static final String POKEMON_NAME = "Charizard";
    private static final String POKEMON_DESCRIPTION = "Charizard flies 'round the sky in search of powerful opponents. it breathes fire of such most wondrous heat yond 't melts aught. however,  't nev'r turns its fiery breath on any opponent weaker than itself.";

    //sut
    private PokemonController pokemonController;

    @Mock
    private PokemonService pokemonService;

    @BeforeEach
    public void setUp() {
        pokemonController = new PokemonController(pokemonService);
    }

    @Test
    @DisplayName("Should return pokemon name and description")
    public void shouldReturnOkWithPokemonInfo() {
        mockPokemonService();
        ResponseEntity<Pokemon> response = pokemonController.getShakespeareanPokemonInfo(POKEMON_NAME);
        assertTrue(response.getStatusCode().is2xxSuccessful());

        Pokemon pokemon = response.getBody();
        assertEquals(POKEMON_NAME, pokemon.getName());
        assertEquals(POKEMON_DESCRIPTION, pokemon.getDescription());
    }

    private void mockPokemonService() {
        Mockito.when(pokemonService.getShakespeareanPokemon(POKEMON_NAME))
                .thenReturn(buildMockedPokemonInfo());
    }

    private Pokemon buildMockedPokemonInfo() {
        return new Pokemon.PokemonBuilder()
                .withName(POKEMON_NAME)
                .withDescription(POKEMON_DESCRIPTION)
                .build();
    }
}
