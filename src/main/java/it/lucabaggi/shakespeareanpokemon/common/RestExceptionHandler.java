package it.lucabaggi.shakespeareanpokemon.common;

import it.lucabaggi.shakespeareanpokemon.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(ShakespeareanPokemonException.class)
    public ResponseEntity<ErrorResponse> shakespeareanPokemonRuntimeException(ShakespeareanPokemonException exception, WebRequest request) {
        log.error("An exception occurred: ", exception);
        ErrorResponse response = new ErrorResponse.ErrorResponseBuilder()
                .withCode(exception.getCode())
                .withMessage(exception.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> remoteServiceException(HttpClientErrorException exception, WebRequest request) {
        log.error("A remote service exception occurred: ", exception);
        ErrorResponse response = new ErrorResponse.ErrorResponseBuilder()
                .withCode(ErrorCode.REMOTE.getCode())
                .withMessage(exception.getLocalizedMessage())
                .withRemoteError(exception.getStatusText())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }
}
