package fr.axa.automation.feature.model;

import fr.axa.automation.webengine.core.AbstractPageModel;
import fr.axa.automation.webengine.core.WebElementDescription;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;


@FieldDefaults(level = AccessLevel.PUBLIC)
public class WebEngineSecondStepPage extends AbstractPageModel {

    @Getter
    @Setter
    WebElementDescription resultTextarea = WebElementDescription.builder().tagName("textarea").id("customid-1").build();

    @Getter
    WebElementDescription scalesCheckbox = WebElementDescription.builder().tagName("input").id("scales").build();

    @Getter
    WebElementDescription hornsCheckbox = WebElementDescription.builder().tagName("input").id("horns").build();

    @Getter
    WebElementDescription nextStep = WebElementDescription.builder().tagName("button").xPath(".//button[contains(text(),\"Next (5-second-delay)\")]").build();

    public WebEngineSecondStepPage(WebDriver webDriver) throws Exception {
        populateDriver(webDriver);
    }
}
