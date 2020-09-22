package it.lucabaggi.shakespeareanpokemon.service;

import it.lucabaggi.shakespeareanpokemon.client.funtranslator.FuntranslatorApiClient;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.PokeapiClient;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.model.PokemonSpecies;
import it.lucabaggi.shakespeareanpokemon.common.ErrorCode;
import it.lucabaggi.shakespeareanpokemon.common.ShakespeareanPokemonException;
import it.lucabaggi.shakespeareanpokemon.model.Pokemon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PokemonServiceImpl implements PokemonService {

    private static final String DEFAULT_LANGUAGE = "en";
    private static final String DEFAULT_VERSION = "omega-ruby";

    private PokeapiClient pokeapiClient;
    private FuntranslatorApiClient funtranslatorApiClient;

    @Autowired
    public PokemonServiceImpl(PokeapiClient pokeapiClient, FuntranslatorApiClient funtranslatorApiClient) {
        this.pokeapiClient = pokeapiClient;
        this.funtranslatorApiClient = funtranslatorApiClient;
    }

    @Override
    public Pokemon getShakespeareanPokemon(String pokemonName) {
        PokemonSpecies pokemonSpecies = pokeapiClient.getPokemonSpecies(pokemonName);
        String pokemonDescription = pokemonSpecies.getFlavorTextEntries().stream()
                .filter(textEntry -> textEntry.getVersion().getName().equalsIgnoreCase(DEFAULT_VERSION))
                .filter(textEntry -> textEntry.getLanguage().getName().equalsIgnoreCase(DEFAULT_LANGUAGE))
                .findFirst()
                .orElseThrow(() -> new ShakespeareanPokemonException(ErrorCode.REMOTE.getCode(),
                        "Unable to find description for the given Pokemon"))
                .getFlavorText();

        String shakespeareanDescription = funtranslatorApiClient
                .getShakespeareanTranslation(pokemonDescription.replaceAll("\\n", " "))
                .getContents().getTranslated();

        return new Pokemon.PokemonBuilder()
                .withName(pokemonName)
                .withDescription(shakespeareanDescription)
                .build();

    }
}
