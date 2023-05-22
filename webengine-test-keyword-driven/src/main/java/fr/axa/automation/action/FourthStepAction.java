package fr.axa.automation.action;

import fr.axa.automation.appmodels.WebEngineFourthStepPage;
import fr.axa.automation.webengine.core.AbstractActionWebBase;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FourthStepAction extends AbstractActionWebBase {

    WebEngineFourthStepPage webEngineFourthStepPage;

    public FourthStepAction() {
    }

    @Override
    public void doAction() throws Exception {
        webEngineFourthStepPage = new WebEngineFourthStepPage(getWebDriver());
        Assertions.assertEquals("DONE", webEngineFourthStepPage.getDoneTitle().getText());
        screenShot();
        addInformation("Fourth step succeed");
    }

    @Override
    public boolean doCheckpoint() throws Exception {
        return true;
    }
}