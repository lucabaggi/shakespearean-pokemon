package it.lucabaggi.shakespeareanpokemon.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "client.basepath")
public class ClientConfiguration {

    private ApiClientConfig pokeapi;
    private ApiClientConfig funtranslator;

    public ApiClientConfig getPokeapi() {
        return pokeapi;
    }

    public void setPokeapi(ApiClientConfig pokeapi) {
        this.pokeapi = pokeapi;
    }

    public ApiClientConfig getFuntranslator() {
        return funtranslator;
    }

    public void setFuntranslator(ApiClientConfig funtranslator) {
        this.funtranslator = funtranslator;
    }

    public static final class ApiClientConfig {

        private String basePath;

        public String getBasePath() {
            return basePath;
        }

        public void setBasePath(String basePath) {
            this.basePath = basePath;
        }
    }
}
