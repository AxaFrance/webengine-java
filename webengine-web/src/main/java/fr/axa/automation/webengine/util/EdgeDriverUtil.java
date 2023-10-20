package fr.axa.automation.webengine.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.util.Collections;
import java.util.List;

public final class EdgeDriverUtil {

    private EdgeDriverUtil() {
    }

    public static WebDriver getEdgeDriver()  {
        return getEdgeDriver(Collections.emptyList());
    }

    public static WebDriver getEdgeDriver(List<String> edgeOptionList)  {
        return getEdgeDriver(null,edgeOptionList);
    }

    public static WebDriver getEdgeDriver(String browserVersion,List<String> edgeOptionList)  {
        if(StringUtils.isEmpty(browserVersion)){
            WebDriverManager.edgedriver().setup();
        }else{
            WebDriverManager.edgedriver().browserVersion(browserVersion).setup();
        }
        if(CollectionUtils.isNotEmpty(edgeOptionList)){
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments(edgeOptionList);
            return new EdgeDriver(edgeOptions);
        }
        return new EdgeDriver();
    }
}
