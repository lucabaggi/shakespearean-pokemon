package it.lucabaggi.shakespeareanpokemon.unit;

import it.lucabaggi.shakespeareanpokemon.client.funtranslator.FuntranslatorApiClient;
import it.lucabaggi.shakespeareanpokemon.client.funtranslator.model.Contents;
import it.lucabaggi.shakespeareanpokemon.client.funtranslator.model.Success;
import it.lucabaggi.shakespeareanpokemon.client.funtranslator.model.Translation;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.PokeapiClient;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.model.FlavorTextEntry;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.model.Language;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.model.PokemonSpecies;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.model.Version;
import it.lucabaggi.shakespeareanpokemon.common.ErrorCode;
import it.lucabaggi.shakespeareanpokemon.common.ShakespeareanPokemonException;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PokemonService unit test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PokemonServiceTest {

    private static final String VALID_POKEMON_NAME = "charizard";
    private static final String WITHOUT_DESCRIPTION_POKEMON_NAME = "random_pokemon_name";
    private static final String FLAVOR_TEXT = "Charizard flies around the sky in search of powerful opponents.\nIt breathes fire of such great heat that it melts anything.\nHowever, it never turns its fiery breath on any opponent\nweaker than itself.";
    private static final String FLAVOR_TEXT_ESCAPED = "Charizard flies around the sky in search of powerful opponents. It breathes fire of such great heat that it melts anything. However, it never turns its fiery breath on any opponent weaker than itself.";
    private static final String FLAVOR_LANGUAGE = "en";
    private static final String FLAVOR_VERSION = "omega-ruby";
    private static final String TRANSLATED_TEXT = "Charizard flies 'round the sky in search of powerful opponents. it breathes fire of such most wondrous heat yond 't melts aught. however,  't nev'r turns its fiery breath on any opponent weaker than itself.";


    //sut
    private PokemonService pokemonService;

    @Mock
    private PokeapiClient pokeapiClient;
    @Mock
    private FuntranslatorApiClient funtranslatorApiClient;

    @BeforeEach
    public void setUp() {
        pokemonService = new PokemonServiceImpl(pokeapiClient, funtranslatorApiClient);
    }

    @Test
    @DisplayName("Should return shakespearean Pokemon information")
    public void shouldReturnShakespeareanPokemonInfo() {
        mockSuccessPokemonSpeciesCall();
        mockSuccessTranslation();

        Pokemon pokemon = pokemonService.getShakespeareanPokemon(VALID_POKEMON_NAME);
        assertEquals(VALID_POKEMON_NAME, pokemon.getName());
        assertEquals(TRANSLATED_TEXT, pokemon.getDescription());
    }

    @Test
    @DisplayName("Should throw ShakespeareanPokemonException when Pokemon description is not found")
    public void shouldThrowShakespeareanPokemonException() {
        mockPokemonSpeciesCallWithoutDescription();

        ShakespeareanPokemonException exception = assertThrows(ShakespeareanPokemonException.class,
                () -> pokemonService.getShakespeareanPokemon(WITHOUT_DESCRIPTION_POKEMON_NAME));

        assertEquals(ErrorCode.INTERNAL.getCode(), exception.getCode());
    }

    private void mockSuccessPokemonSpeciesCall() {
        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        pokemonSpecies.setFlavorTextEntries(Arrays.asList(buildFlavorTextEntry()));
        Mockito.when(pokeapiClient.getPokemonSpecies(VALID_POKEMON_NAME))
                .thenReturn(pokemonSpecies);
    }

    private void mockPokemonSpeciesCallWithoutDescription() {
        PokemonSpecies pokemonSpecies = new PokemonSpecies();
        pokemonSpecies.setFlavorTextEntries(new ArrayList<>());
        Mockito.when(pokeapiClient.getPokemonSpecies(WITHOUT_DESCRIPTION_POKEMON_NAME))
                .thenReturn(pokemonSpecies);
    }

    private void mockSuccessTranslation() {
        Mockito.when(funtranslatorApiClient.getShakespeareanTranslation(FLAVOR_TEXT_ESCAPED))
                .thenReturn(buildTranslation());
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

    private Translation buildTranslation() {
        Success success = new Success();
        success.setTotal(1);
        Contents contents = new Contents();
        contents.setText(FLAVOR_TEXT_ESCAPED);
        contents.setTranslated(TRANSLATED_TEXT);
        contents.setTranslation("shakespeare");
        Translation translation = new Translation();
        translation.setContents(contents);
        translation.setSuccess(success);
        return translation;
    }
}
