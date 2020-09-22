package it.lucabaggi.shakespeareanpokemon.unit;

import it.lucabaggi.shakespeareanpokemon.client.pokeapi.PokeapiClient;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.model.FlavorTextEntry;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.model.Language;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.model.PokemonSpecies;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.model.Version;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PokeapiClient unit test")
@ExtendWith(MockitoExtension.class)
public class PokeapiClientTest {

    private static final String POKEMON_NAME = "charizard";
    private static final String SPECIES_URI = "/pokemon-species/%s";
    private static final String FLAVOR_TEXT = "Charizard flies around the sky in search of powerful opponents.\\nIt breathes fire of such great heat that it melts anything.\\nHowever, it never turns its fiery breath on any opponent\\nweaker than itself.";
    private static final String FLAVOR_LANGUAGE = "en";
    private static final String FLAVOR_VERSION = "omega-ruby";

    //sut
    private PokeapiClient pokeapiClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        pokeapiClient = new PokeapiClient(restTemplate);
    }

    @Test
    @DisplayName("Should return pokemon species info")
    public void shouldReturnPokemonSpecies() {
        mockPokemonSpeciesCall();
        PokemonSpecies pokemonSpecies = pokeapiClient.getPokemonSpecies(POKEMON_NAME);
        assertTrue(pokemonSpecies.getFlavorTextEntries().size() == 1);
        FlavorTextEntry flavorTextEntry = pokemonSpecies.getFlavorTextEntries().get(0);
        assertEquals(FLAVOR_LANGUAGE, flavorTextEntry.getLanguage().getName());
        assertEquals(FLAVOR_TEXT, flavorTextEntry.getFlavorText());
        assertEquals(FLAVOR_VERSION, flavorTextEntry.getVersion().getName());
    }


    private void mockPokemonSpeciesCall() {
        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        pokemonSpecies.setFlavorTextEntries(Arrays.asList(buildFlavorTextEntry()));
        ResponseEntity<PokemonSpecies> result = ResponseEntity.ok(pokemonSpecies);
        Mockito.when(restTemplate.getForEntity(String.format(SPECIES_URI, POKEMON_NAME), PokemonSpecies.class))
                .thenReturn(result);
    }

    private FlavorTextEntry buildFlavorTextEntry() {
        Version version = new Version();
        version.setName(FLAVOR_VERSION);
        Language language = new Language();
        language.setName(FLAVOR_LANGUAGE);
        FlavorTextEntry flavorTextEntry = new FlavorTextEntry();
        flavorTextEntry.setFlavorText(FLAVOR_TEXT);
        flavorTextEntry.setLanguage(language);
        flavorTextEntry.setVersion(version);
        return flavorTextEntry;
    }
}
