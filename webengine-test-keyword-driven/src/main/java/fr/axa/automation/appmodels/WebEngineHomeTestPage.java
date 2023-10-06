package fr.axa.automation.appmodels;

import fr.axa.automation.webengine.core.AbstractPageModel;
import fr.axa.automation.webengine.core.WebElementDescription;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;


@FieldDefaults(level = AccessLevel.PUBLIC)
public class WebEngineHomeTestPage extends AbstractPageModel {

    WebElementDescription okButton = WebElementDescription.builder().tagName("input").id("btnButtonOk").build();

    WebElementDescription confirmButton = WebElementDescription.builder().tagName("input").id("btnButtonConfirm").build();

    WebElementDescription inputButton = WebElementDescription.builder().tagName("input").id("btnButtonInput").build();

    WebElementDescription textInput = WebElementDescription.builder().tagName("input").id("inputValue").build();

    WebElementDescription passwordInput = WebElementDescription.builder().tagName("password").id("password").build();

    WebElementDescription startStep1Link = WebElementDescription.builder().tagName("a").linkText("Step1.html").build();

    public WebEngineHomeTestPage(WebDriver webDriver) throws Exception {
        populateDriver(webDriver);
    }
}
