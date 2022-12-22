package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BrowserTypeHelper {

    public static Browser getBrowser(String browserFill) throws WebEngineException {
        List<Browser> browserEnumList = Arrays.asList(Browser.values());
        List<Browser> browserFoundList = browserEnumList.stream().filter(browser -> browser.getValue().equalsIgnoreCase(browserFill)).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(browserFoundList)){
            return browserFoundList.get(0);
        }
        throw new WebEngineException("unrecognized Browser value. Possible values are : " + browserEnumList.stream().map(b -> b.getValue()).collect(Collectors.toList()));
    }

}
