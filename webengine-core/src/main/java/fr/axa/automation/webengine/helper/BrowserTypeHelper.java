package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.general.Browser;
import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.util.SharedContext;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BrowserTypeHelper {

    public static Browser getBrowser(String browserFill) throws WebEngineException {
        List<Browser> browserTypesList = Arrays.asList(Browser.values());
        List<Browser> browserFoundList = browserTypesList.stream().filter(browser -> browser.getValue().equalsIgnoreCase(browserFill)).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(browserFoundList)){
            return browserFoundList.get(0);
        }
        throw new WebEngineException("unrecognized Browser value. Possible values are : "+browserTypesList.toString());
    }

}
