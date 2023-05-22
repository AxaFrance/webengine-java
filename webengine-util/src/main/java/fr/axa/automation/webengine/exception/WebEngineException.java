package fr.axa.automation.webengine.exception;

public class WebEngineException extends Exception {

    public WebEngineException(String message) {
        super(message);
    }

    public WebEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
