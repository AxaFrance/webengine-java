package fr.axa.automation.model;

import fr.axa.automation.webengine.core.AbstractPageModel;
import fr.axa.automation.webengine.core.WebElementDescription;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class Page extends AbstractPageModel {

    @Getter
    WebElementDescription language = WebElementDescription.builder().tagName("select").id("language").build();

    @Getter
    WebElementDescription coffeeRadio = WebElementDescription.builder().tagName("input").id("coffee").build();

    @Getter
    WebElementDescription teaRadio = WebElementDescription.builder().tagName("input").id("tea").build();

    @Getter
    WebElementDescription waterRadio = WebElementDescription.builder().tagName("input").id("water").build();

    @Getter
    WebElementDescription nextStep = WebElementDescription.builder().tagName("button").xPath(".//button[contains(text(),\"Next (3-second-delay)\")]").build();

    public Page(WebDriver webDriver) throws Exception {
        populateDriver(webDriver);
    }
}
