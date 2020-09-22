package it.lucabaggi.shakespeareanpokemon.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.lucabaggi.shakespeareanpokemon.model.ErrorResponse;
import it.lucabaggi.shakespeareanpokemon.model.Pokemon;
import it.lucabaggi.shakespeareanpokemon.service.PokemonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Return Pokemon info with shakespearean description")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success", response = Pokemon.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse.class),
            @ApiResponse(code = 502, message = "Bad Gateway", response = ErrorResponse.class)
    })
    public ResponseEntity<Pokemon> getShakespeareanPokemonInfo(
            @ApiParam(name = "name", value = "the Pokemon name") @PathVariable("name") String name) {

        log.info("Getting Shakespearean Pokemon info", kv("pokemon_name", name));
        Pokemon pokemon = pokemonService.getShakespeareanPokemon(name);
        return ResponseEntity.ok(pokemon);
    }
}
