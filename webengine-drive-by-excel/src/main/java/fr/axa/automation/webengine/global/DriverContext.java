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

    public String getWindowHandle(){
        return sessionIdAndUrlMap.entrySet().stream().filter(entry -> {
            try {
                return StringUtil.equalsIgnoreCase(UriUtil.getHostFromURI(entry.getValue()), UriUtil.getHostFromURI(currentUrl));
            } catch (WebEngineException e) {
                throw new RuntimeException(e);
            }
        }).findFirst().get().getKey();
    }
}
