package fr.axa.automation.webengine.exception;

import org.openqa.selenium.WebDriverException;

public class MultipleElementException extends WebDriverException {

    public MultipleElementException(String message) {
        super(message);
    }

    public MultipleElementException(String message, Throwable cause) {
        super(message, cause);
    }
}
