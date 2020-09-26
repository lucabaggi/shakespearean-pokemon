package it.lucabaggi.shakespeareanpokemon.unit;

import com.netflix.hystrix.contrib.javanica.command.GenericCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
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
    private static final String REMOTE_ERROR_NOT_FOUND = "NOT_FOUND";
    private static final String REMOTE_ERROR_SERVICE_UNAVAILABLE = "Service Unavailable";

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
        assertEquals(REMOTE_ERROR_NOT_FOUND, responseEntity.getBody().getRemoteError().get());
    }

    @Test
    @DisplayName("Should handle hystrix runtime exception and return proper error response")
    public void shouldHandleHystrixRuntimeException() {
        HystrixRuntimeException exception = new HystrixRuntimeException(HystrixRuntimeException.FailureType.TIMEOUT,
                GenericCommand.class,
                ERROR_MESSAGE,
                new RuntimeException(),
                new RuntimeException());
        ResponseEntity<ErrorResponse> responseEntity = restExceptionHandler.hystrixRuntimeException(exception);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, responseEntity.getStatusCode());
        assertEquals(ErrorCode.REMOTE.getCode(), responseEntity.getBody().getCode());
        assertTrue(responseEntity.getBody().getRemoteError().isPresent());
        assertEquals(REMOTE_ERROR_SERVICE_UNAVAILABLE, responseEntity.getBody().getRemoteError().get());
    }
}
