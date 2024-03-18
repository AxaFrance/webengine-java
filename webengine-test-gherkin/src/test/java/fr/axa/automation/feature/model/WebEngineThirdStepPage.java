package fr.axa.automation.feature.model;

import fr.axa.automation.webengine.core.AbstractPageModel;
import fr.axa.automation.webengine.core.WebElementDescription;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;


@FieldDefaults(level = AccessLevel.PUBLIC)
public class WebEngineThirdStepPage extends AbstractPageModel {

    WebElementDescription dateInput = WebElementDescription.builder().tagName("input").id("txtDate").build();

    WebElementDescription passwordInput = WebElementDescription.builder().tagName("input").id("txtPassword").build();

    WebElementDescription nextStep = WebElementDescription.builder().tagName("button").xPath(".//button[contains(text(),\"I'm done\")]").build();

    public WebEngineThirdStepPage(WebDriver webDriver) throws Exception {
        populateDriver(webDriver);
    }
}
