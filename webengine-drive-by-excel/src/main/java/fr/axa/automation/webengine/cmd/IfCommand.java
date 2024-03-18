package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.PredefinedTagValue;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.AssertContentResult;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.helper.VariableHelper;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.List;

public class IfCommand extends AbstractDriverCommand {

    @Override
    public void executeCmd(AbstractGlobalApplicationContext globalApplicationContext, AbstractTestCaseContext testCaseContext, CommandDataNoCode commandData, List<CommandResult> commandResultList) throws Exception {
        webElementDescription = populateWebElement(globalApplicationContext,testCaseContext, commandData, commandResultList);
        String expectedValue = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        if (StringUtils.isEmpty(expectedValue)) {
            if (webElementDescription.isNotExists()) {
                throw new WebEngineException("The element doesn't exist");
            }
        } else {
            AssertContentResult assertContentResult;
            try{
                if(webElementDescription.isSelect()){
                    assertContentResult = webElementDescription.assertContentSelect(expectedValue);
                } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.CHECKED.getTagValue(), expectedValue) && webElementDescription.isInputTypeRadio()){
                    assertContentResult = webElementDescription.assertRadioChecked();
                } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_CHECKED.getTagValue(), expectedValue) && webElementDescription.isInputTypeRadio()){
                    assertContentResult = webElementDescription.assertRadioNotChecked();
                } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.EXISTS.getTagValue(), expectedValue) && webElementDescription.isNotExists()) {
                    throw new WebEngineException("The element doesn't exist");
                } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_EXISTS.getTagValue(), expectedValue) && webElementDescription.exists()) {
                    throw new WebEngineException("The element exist");
                } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.EMPTY.getTagValue(), expectedValue)) {
                    assertContentResult = webElementDescription.assertContentEmpty();
                } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_EMPTY.getTagValue(), expectedValue)) {
                    assertContentResult = webElementDescription.assertContentNotEmpty();
                } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.DISPLAYED.getTagValue(), expectedValue) && webElementDescription.isNotDisplayed()) {
                    throw new WebEngineException("The element is not displayed");
                } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_DISPLAYED.getTagValue(), expectedValue) && webElementDescription.isDisplayed()) {
                    throw new WebEngineException("The element is displayed");
                } else{
                    assertContentResult = webElementDescription.assertContentByElementType(expectedValue);
                }
            }catch (Exception e){
                getLogReport().getVariables().add(VariableHelper.getVariable("Cause", e.getMessage()));
                getLogReport().getVariables().add(VariableHelper.getVariable("Exception", ExceptionUtils.getStackTrace(e)));
                throw new WebEngineException(e.getMessage());
            }

            if(assertContentResult!=null && assertContentResult.isResult()){
                getLogReport().getVariables().add(VariableHelper.getVariable("Expected value", expectedValue));
                getLogReport().getVariables().add(VariableHelper.getVariable("Actual value", assertContentResult.getActualValue()));
            }

        }
    }
}
