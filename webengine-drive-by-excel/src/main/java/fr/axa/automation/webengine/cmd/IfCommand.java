package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.PredefinedTagValue;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.TestCaseDriveByExcelContext;
import fr.axa.automation.webengine.object.CommandDataDriveByExcel;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class IfCommand extends AbstractDriverCommand{

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataDriveByExcel commandData, List<CommandResult> commandResultList)throws Exception{
        webElementDescription = populateWebElement(testCaseContext,commandData,commandResultList);
        String value = getValue((TestCaseDriveByExcelContext) testCaseContext, commandData, commandResultList);
        if(StringUtils.isEmpty(value)){
            if(webElementDescription.isNotExists()){
                throw new WebEngineException("The element doesn't exist");
            }
        } else {
            if(StringUtil.equalsIgnoreCase(PredefinedTagValue.CHECKED.getTagValue(), value) && webElementDescription.isNotSelected()) {
                throw new WebEngineException("The element isn't checked");
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.UNCHECKED.getTagValue(), value) && webElementDescription.isSelected()) {
                throw new WebEngineException("The element is checked");
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.DISPLAYED.getTagValue(), value) && webElementDescription.isNotDisplayed()) {
                throw new WebEngineException("The element is not displayed");
            }else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_DISPLAYED.getTagValue(), value) && webElementDescription.isDisplayed()) {
                throw new WebEngineException("The element is displayed");
            } else {
                webElementDescription.assertContentByElementType(value);
            }
        }
    }
}
