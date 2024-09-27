package fr.axa.automation.webengine.global;


import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.util.StringUtil;
import fr.axa.automation.webengine.util.UriUtil;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;

import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class DriverContext {
    String currentUrl;
    Map<String,String> sessionIdAndUrlMap;
    WebDriver webDriver;

    public String getWindowHandle() throws WebEngineException {
        for (Map.Entry<String,String> entry : sessionIdAndUrlMap.entrySet()) {
            String url1 = UriUtil.getHostFromURI(entry.getValue())==null? entry.getValue() :UriUtil.getHostFromURI(entry.getValue());
            String url2 = UriUtil.getHostFromURI(currentUrl)==null? currentUrl : UriUtil.getHostFromURI(currentUrl);
            if (StringUtil.equalsIgnoreCase(url1, url2)) {
                return entry.getKey();
            }
        }
        return sessionIdAndUrlMap.entrySet().stream().findFirst().get().getKey();
    }
}
