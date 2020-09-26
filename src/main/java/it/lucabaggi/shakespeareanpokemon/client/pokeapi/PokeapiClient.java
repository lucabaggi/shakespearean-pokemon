package it.lucabaggi.shakespeareanpokemon.client.pokeapi;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import it.lucabaggi.shakespeareanpokemon.client.pokeapi.model.PokemonSpecies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static it.lucabaggi.shakespeareanpokemon.client.pokeapi.PokeapiUri.SPECIES_URI;

@Component
public class PokeapiClient {

    private RestTemplate restTemplate;

    @Autowired
    public PokeapiClient(@Qualifier("pokeapiRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(commandKey = "getPokemonSpecies")
    public PokemonSpecies getPokemonSpecies(String name) {
        String speciesUri = String.format(SPECIES_URI, name);
        final ResponseEntity<PokemonSpecies> response = restTemplate.getForEntity(speciesUri, PokemonSpecies.class);
        return response.getBody();
    }
}
