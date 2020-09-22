package it.lucabaggi.shakespeareanpokemon.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "client.basepath")
public class ClientConfiguration {

    private Pokeapi pokeapi;

    public Pokeapi getPokeapi() {
        return pokeapi;
    }

    public void setPokeapi(Pokeapi pokeapi) {
        this.pokeapi = pokeapi;
    }

    public static final class Pokeapi {

        private String basePath;

        public String getBasePath() {
            return basePath;
        }

        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }
    }
}
