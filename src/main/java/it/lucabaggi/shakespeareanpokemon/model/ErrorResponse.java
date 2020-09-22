package it.lucabaggi.shakespeareanpokemon.model;

import java.util.Optional;

public class ErrorResponse {

    private String code;
    private String message;
    private Optional<String> remoteError;

    public ErrorResponse(String code, String message, String remoteError) {
        this.code = code;
        this.message = message;
        this.remoteError = Optional.ofNullable(remoteError);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Optional<String> getRemoteError() {
        return remoteError;
    }

    public void setRemoteError(Optional<String> remoteError) {
        this.remoteError = remoteError;
    }

    public static final class ErrorResponseBuilder {

        private String code;
        private String message;
        private String remoteError;

        public ErrorResponseBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public ErrorResponseBuilder withMessage(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder withRemoteError(String remoteError) {
            this.remoteError = remoteError;
            return this;
        }

        public ErrorResponse build() {
            ErrorResponse errorResponse = new ErrorResponse(code, message, remoteError);
            return errorResponse;
        }
    }
}
