package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BrowserTypeHelper {

    public static Browser getBrowser(String browserFill) throws WebEngineException {
        List<Browser> browserEnumList = Arrays.asList(Browser.values());
        Browser browser = foundBrowser(browserFill, getBrowserPredicateWithName(browserFill));
        if (browser != null) {
            return browser;
        }

        browser = foundBrowser(browserFill, getBrowserPredicateWithValue(browserFill));
        if (browser != null) {
            return browser;
        }
        throw new WebEngineException("unrecognized Browser value. Possible values are : " + browserEnumList.stream().map(b -> b.getValue()).collect(Collectors.toList()));
    }


    private static Predicate<Browser> getBrowserPredicateWithName(String browserFill) {
        return browser -> browser.name().equalsIgnoreCase(browserFill);
    }

    private static Predicate<Browser> getBrowserPredicateWithValue(String browserFill) {
        return browser -> browser.getValue().equalsIgnoreCase(browserFill);
    }

    private static Browser foundBrowser(String browserFill, Predicate predicate) {
        List<Browser> browserList = Arrays.asList(Browser.values());
        List<Browser> browserFoundList = (List<Browser>) browserList.stream().filter(predicate).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(browserFoundList)) {
            return browserFoundList.get(0);
        }
        return null;
    }

}
