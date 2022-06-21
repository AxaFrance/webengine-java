package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.BrowserType;

import java.util.Arrays;
import java.util.List;

public class BrowserTypeHelper {

    public static BrowserType getBrowser(String browserFill) throws WebEngineException {
        List<BrowserType> browserTypesList = Arrays.asList(BrowserType.values());
        for (BrowserType browserType:browserTypesList) {
            if(browserType.getValue().equalsIgnoreCase(browserFill)){
                return browserType;
            }
        }
        throw new WebEngineException("unrecognized Browser value");
    }

}
