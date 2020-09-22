package it.lucabaggi.shakespeareanpokemon.unit;

import it.lucabaggi.shakespeareanpokemon.client.funtranslator.FuntranslatorApiClient;
import it.lucabaggi.shakespeareanpokemon.client.funtranslator.model.Contents;
import it.lucabaggi.shakespeareanpokemon.client.funtranslator.model.Success;
import it.lucabaggi.shakespeareanpokemon.client.funtranslator.model.Translation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FuntranslatorClient unit test")
@ExtendWith(MockitoExtension.class)
public class FuntranslatorClientTest {

    private static final String TRANSLATE_URI = "/translate/shakespeare.json";
    private static final String ORIGINAL_TEXT = "You gave Mr. Tim a hearty meal, but unfortunately what he ate made him die.";
    private static final String TRANSLATED_TEXT = "Thee did giveth mr. Tim a hearty meal,  but unfortunately what he did doth englut did maketh him kicketh the bucket.";

    //sut
    private FuntranslatorApiClient funtranslatorApiClient;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        funtranslatorApiClient = new FuntranslatorApiClient(restTemplate);
    }

    @Test
    @DisplayName("Should return shakespearean translated text")
    public void shouldReturnTranslatedText() {
        mockTranslateCall();
        Translation translation = funtranslatorApiClient.getShakespeareanTranslation(ORIGINAL_TEXT);
        assertEquals(ORIGINAL_TEXT, translation.getContents().getText());
        assertEquals(TRANSLATED_TEXT, translation.getContents().getTranslated());
    }


    private void mockTranslateCall() {
        ResponseEntity<Translation> result = ResponseEntity.ok(buildTranslation());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("text", ORIGINAL_TEXT);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        Mockito.when(restTemplate.exchange(String.format(TRANSLATE_URI), HttpMethod.POST, request, Translation.class))
                .thenReturn(result);
    }

    private Translation buildTranslation() {
        Success success = new Success();
        success.setTotal(1);
        Contents contents = new Contents();
        contents.setText(ORIGINAL_TEXT);
        contents.setTranslated(TRANSLATED_TEXT);
        contents.setTranslation("shakespeare");
        Translation translation = new Translation();
        translation.setContents(contents);
        translation.setSuccess(success);
        return translation;
    }
}
