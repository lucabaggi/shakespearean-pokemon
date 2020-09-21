package it.lucabaggi.shakespeareanpokemon.controller;

import it.lucabaggi.shakespeareanpokemon.model.Pokemon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pokemon")
public class PokemonController {

    private static final Logger log = LoggerFactory.getLogger(PokemonController.class);

    @GetMapping("/{name}")
    public ResponseEntity<Pokemon> getPokemonInfo(@PathVariable("name") String name) {
        Pokemon pokemon = new Pokemon.PokemonBuilder()
                .withName(name)
                .withDescription("test")
                .build();
        return ResponseEntity.ok(pokemon);
    }
}
