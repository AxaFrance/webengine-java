package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.LocatingBy;
import fr.axa.automation.webengine.core.WebElementDescription;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractDriverCommand implements ICommand{

    WebElementDescription webElementDescription;

    protected WebElementDescription populateWebElement(CommandDataDriveByExcel commandData, AbstractTestCaseContext testCaseContext) {
        return WebElementDescription.builder()
                .useDriver((WebDriver) testCaseContext.getWebDriver())
                .id(populateBySelector(commandData, LocatingBy.BY_ID))
                .name(populateBySelector(commandData, LocatingBy.BY_NAME))
                .className(populateBySelector(commandData, LocatingBy.BY_CLASS_NAME))
                .linkText(populateBySelector(commandData, LocatingBy.BY_LINK_TEXT))
                .tagName(populateBySelector(commandData, LocatingBy.BY_TAG_NAME))
                .cssSelector(populateBySelector(commandData, LocatingBy.BY_CSS_SELECTOR))
                .xPath(populateBySelector(commandData, LocatingBy.BY_XPATH))
                .build();
    }

    protected String populateBySelector(CommandDataDriveByExcel commandData, LocatingBy locatingBy) {
        switch (locatingBy) {
            case BY_ID:
            case BY_NAME:
            case BY_CLASS_NAME:
            case BY_LINK_TEXT:
            case BY_TAG_NAME:
            case BY_CSS_SELECTOR:
            case BY_XPATH:
                String value = commandData.getTargetList().get(locatingBy.getValue());
                return StringUtils.isNotEmpty(value) ? value : StringUtils.EMPTY;

            default:
                return StringUtils.EMPTY;
        }
    }

    public Object execute(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData) throws Exception {
        String dataTestColumName = ((TestCaseDriveByExcelContext)testCaseContext).getDataTestColumnName();
        if(commandData.canExecuteDataTestColumn(dataTestColumName)){
            return executeCmd(globalApplicationContext,  testCaseContext,  commandData);
        }
        return null;
    }

    abstract Object executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData)throws Exception;
}
