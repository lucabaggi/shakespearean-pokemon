package it.lucabaggi.shakespeareanpokemon.unit;

import it.lucabaggi.shakespeareanpokemon.client.RestTemplateErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RestTemplateErrorHandler unit test")
public class RestTemplateErrorHandlerTest {

    //sut
    private RestTemplateErrorHandler restTemplateErrorHandler;

    @BeforeEach
    public void setUp() {
        restTemplateErrorHandler = new RestTemplateErrorHandler();
    }

    @Test
    @DisplayName("Should return true when response contains http error status code")
    public void shouldReturnTrueWithHttpError() throws IOException {
        MockClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.NOT_FOUND);
        boolean hasError = restTemplateErrorHandler.hasError(response);
        assertTrue(hasError);
    }

    @Test
    @DisplayName("Should return false when response contains http ok")
    public void shouldReturnFalseWithHttpOk() throws IOException {
        MockClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.OK);
        boolean hasError = restTemplateErrorHandler.hasError(response);
        assertFalse(hasError);
    }

    @Test
    @DisplayName("Should throw HttpClientErrorException in case of error")
    public void shouldThrowHttpClientErrorException() {
        MockClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class,
                () -> restTemplateErrorHandler.handleError(response));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getStatusText());
    }
}
