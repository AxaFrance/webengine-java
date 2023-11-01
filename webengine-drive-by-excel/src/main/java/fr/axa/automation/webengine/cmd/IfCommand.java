package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.ConstantNoCode;
import fr.axa.automation.webengine.constante.PredefinedTagValue;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
import fr.axa.automation.webengine.global.AssertContentResult;
import fr.axa.automation.webengine.global.TestCaseNoCodeContext;
import fr.axa.automation.webengine.object.CommandDataNoCode;
import fr.axa.automation.webengine.object.CommandResult;
import fr.axa.automation.webengine.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

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
            String errorMessage;
            if(webElementDescription.isSelect()){
                assertContentResult = webElementDescription.assertContentSelect(expectedValue);
                errorMessage = "The element is not selected";
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.CHECKED.getTagValue(), expectedValue) && webElementDescription.isInputTypeRadio()){
                assertContentResult = webElementDescription.assertRadioChecked();
                errorMessage = "The input radio is not checked";
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_CHECKED.getTagValue(), expectedValue) && webElementDescription.isInputTypeRadio()){
                assertContentResult = webElementDescription.assertRadioNotChecked();
                errorMessage = "The input radio is checked";
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.EXISTS.getTagValue(), expectedValue) && webElementDescription.isNotExists()) {
                throw new WebEngineException("The element doesn't exist");
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_EXISTS.getTagValue(), expectedValue) && webElementDescription.exists()) {
                throw new WebEngineException("The element exist");
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.EMPTY.getTagValue(), expectedValue)) {
                assertContentResult = webElementDescription.assertContentEmpty();
                errorMessage = "The content of the element is not empty";
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_EMPTY.getTagValue(), expectedValue)) {
                assertContentResult = webElementDescription.assertContentNotEmpty();
                errorMessage = "The content of the element is empty";
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.DISPLAYED.getTagValue(), expectedValue) && webElementDescription.isNotDisplayed()) {
                throw new WebEngineException("The element is not displayed");
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_DISPLAYED.getTagValue(), expectedValue) && webElementDescription.isDisplayed()) {
                throw new WebEngineException("The element is displayed");
            } else{
                assertContentResult = webElementDescription.assertContentByElementType(expectedValue);
                errorMessage = "The value are not the same";
            }
            if(!assertContentResult.isResult()){
                String expectedSentence = "The expected value is : '" + expectedValue + "'";
                getLogReport().append(ConstantNoCode.CR_LF.getValue()).append(expectedSentence);
                String actualSentence = "The actual value is : '" + assertContentResult.getActualValue() + "'";
                getLogReport().append(ConstantNoCode.CR_LF.getValue()).append(actualSentence);
                throw new WebEngineException(errorMessage);
            }

        }
    }
}
