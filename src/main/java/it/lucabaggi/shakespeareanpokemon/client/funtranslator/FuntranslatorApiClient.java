package it.lucabaggi.shakespeareanpokemon.client.funtranslator;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import it.lucabaggi.shakespeareanpokemon.client.funtranslator.model.Translation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static it.lucabaggi.shakespeareanpokemon.client.funtranslator.FuntranslatorUri.TRANSLATE_URI;

@Component
public class FuntranslatorApiClient {

    private RestTemplate restTemplate;

    @Autowired
    public FuntranslatorApiClient(@Qualifier("funtranslatorRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(commandKey = "getShakespeareanTranslation")
    public Translation getShakespeareanTranslation(String text) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("text", text);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Translation> response =
                restTemplate.exchange(TRANSLATE_URI,
                        HttpMethod.POST,
                        request,
                        Translation.class);
        return response.getBody();
    }
}
