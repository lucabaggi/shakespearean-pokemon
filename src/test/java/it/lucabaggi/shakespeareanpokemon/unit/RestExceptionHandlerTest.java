package it.lucabaggi.shakespeareanpokemon.unit;

import it.lucabaggi.shakespeareanpokemon.common.ErrorCode;
import it.lucabaggi.shakespeareanpokemon.common.RestExceptionHandler;
import it.lucabaggi.shakespeareanpokemon.common.ShakespeareanPokemonException;
import it.lucabaggi.shakespeareanpokemon.model.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RestExceptionHandler unit test")
public class RestExceptionHandlerTest {

    private static final String ERROR_MESSAGE = "An error message";
    private static final String EXPECTED_REMOTE_ERROR = "NOT_FOUND";

    //sut
    private RestExceptionHandler restExceptionHandler;

    @BeforeEach
    public void setUp() {
        restExceptionHandler = new RestExceptionHandler();
    }

    @Test
    @DisplayName("Should handle ShakespeareanPokemonException and return proper error response")
    public void shouldHandleShakespeareanPokemonException() {
        ShakespeareanPokemonException exception = new ShakespeareanPokemonException(ErrorCode.INTERNAL.getCode(), ERROR_MESSAGE);
        ResponseEntity<ErrorResponse> responseEntity = restExceptionHandler.shakespeareanPokemonRuntimeException(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(ErrorCode.INTERNAL.getCode(), responseEntity.getBody().getCode());
        assertEquals(ERROR_MESSAGE, responseEntity.getBody().getMessage());
    }

    @Test
    @DisplayName("Should handle remote service exception and return proper error response")
    public void shouldHandleRemoteServiceException() {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        ResponseEntity<ErrorResponse> responseEntity = restExceptionHandler.remoteServiceException(exception);
        assertEquals(HttpStatus.BAD_GATEWAY, responseEntity.getStatusCode());
        assertEquals(ErrorCode.REMOTE.getCode(), responseEntity.getBody().getCode());
        assertTrue(responseEntity.getBody().getRemoteError().isPresent());
        assertEquals(EXPECTED_REMOTE_ERROR, responseEntity.getBody().getRemoteError().get());
    }
}
