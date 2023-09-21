package fr.axa.automation.webengine.util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.collections4.CollectionUtils;
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
        WebDriverManager.edgedriver().setup();
        if(CollectionUtils.isNotEmpty(edgeOptionList)){
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments(edgeOptionList);
            return new EdgeDriver(edgeOptions);
        }
        return new EdgeDriver();
    }
}
