package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;

import java.util.Arrays;
import java.util.List;

public class BrowserTypeHelper {

    public static Browser getBrowser(String browserFill) throws WebEngineException {
        List<Browser> browserTypesList = Arrays.asList(Browser.values());
        for (Browser browser :browserTypesList) {
            if(browser.getValue().equalsIgnoreCase(browserFill)){
                return browser;
            }
        }
        throw new WebEngineException("unrecognized Browser value");
    }

}
