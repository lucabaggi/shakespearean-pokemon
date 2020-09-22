package it.lucabaggi.shakespeareanpokemon.controller;

import it.lucabaggi.shakespeareanpokemon.model.Pokemon;
import it.lucabaggi.shakespeareanpokemon.service.PokemonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private static final Logger log = LoggerFactory.getLogger(PokemonController.class);

    private PokemonService pokemonService;

    @Autowired
    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Pokemon> getShakespeareanPokemonInfo(@PathVariable("name") String name) {
        log.info("Getting Shakespearean Pokemon info", kv("pokemon_name", name));
        Pokemon pokemon = pokemonService.getShakespeareanPokemon(name);
        return ResponseEntity.ok(pokemon);
    }
}
