package fr.axa.automation.feature.model;

import fr.axa.automation.webengine.core.AbstractPageModel;
import fr.axa.automation.webengine.core.WebElementDescription;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;


@FieldDefaults(level = AccessLevel.PUBLIC)
public class WebEngineFourthStepPage extends AbstractPageModel {

    @Getter
    @Setter
    WebElementDescription doneTitle = WebElementDescription.builder().tagName("h1").xPath(".//h1[contains(text(),\"DONE\")]").build();

    public WebEngineFourthStepPage(WebDriver webDriver) throws Exception {
        populateDriver(webDriver);
    }
}
