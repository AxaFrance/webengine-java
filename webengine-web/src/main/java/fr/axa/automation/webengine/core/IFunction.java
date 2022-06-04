package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.MultipleElementException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;

public interface IFunction<R> {
    R call() throws Exception;
}
