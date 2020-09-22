package it.lucabaggi.shakespeareanpokemon.common;

public enum ErrorCode {

    INTERNAL("E:INTERNAL"),
    REMOTE("E:REMOTE_SERVICE");

    private ErrorCode(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
