package it.lucabaggi.shakespeareanpokemon.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BeanUtil {

    private ClientConfiguration clientConfiguration;
    private RestTemplateErrorHandler restTemplateErrorHandler;

    @Autowired
    public BeanUtil(ClientConfiguration clientConfiguration,
                    RestTemplateErrorHandler restTemplateErrorHandler) {
        this.clientConfiguration = clientConfiguration;
        this.restTemplateErrorHandler = restTemplateErrorHandler;
    }

    @Bean(value = "pokeapiRestTemplate")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RestTemplate pokeapiRestTemplate() {
        return buildRestTemplate(clientConfiguration.getPokeapi().getBasePath());
    }

    @Bean(value = "funtranslatorRestTemplate")
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RestTemplate funtranslatorRestTemplate() {
        return buildRestTemplate(clientConfiguration.getFuntranslator().getBasePath());
    }

    private RestTemplate buildRestTemplate(String basePath) {
        return new RestTemplateBuilder()
                .rootUri(basePath)
                .errorHandler(restTemplateErrorHandler)
                .build();
    }
}
