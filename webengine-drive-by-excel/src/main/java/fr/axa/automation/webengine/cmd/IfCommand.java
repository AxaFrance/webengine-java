package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.PredefinedTagValue;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.AbstractTestCaseContext;
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
        String value = getValue(globalApplicationContext,(TestCaseNoCodeContext) testCaseContext, commandData, commandResultList);
        if (StringUtils.isEmpty(value)) {
            if (webElementDescription.isNotExists()) {
                throw new WebEngineException("The element doesn't exist");
            }
        } else {
            boolean resultAssert;
            if(webElementDescription.isInputSelect()){
                resultAssert = webElementDescription.assertContentByElementType(value);
                if (!resultAssert) {
                    throw new WebEngineException("The element is not selected");
                }
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.CHECKED.getTagValue(), value) && webElementDescription.isInputRadio()){
                resultAssert = webElementDescription.assertContentByElementType(value);
                if (!resultAssert) {
                    throw new WebEngineException("The input radio is not checked");
                }
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_CHECKED.getTagValue(), value) && webElementDescription.isInputRadio()){
                resultAssert = webElementDescription.assertContentByElementType(value);
                if (resultAssert) {
                    throw new WebEngineException("The input radio is checked");
                }
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.EXISTS.getTagValue(), value) && webElementDescription.isNotExists()) {
                throw new WebEngineException("The element doesn't exist");
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_EXISTS.getTagValue(), value) && webElementDescription.exists()) {
                throw new WebEngineException("The element exist");
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.EMPTY.getTagValue(), value)) {
                resultAssert = webElementDescription.assertContentByElementType(StringUtils.EMPTY);
                if (!resultAssert) {
                    throw new WebEngineException("The content of the element is not empty");
                }
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_EMPTY.getTagValue(), value)) {
                resultAssert = webElementDescription.assertContentByElementType(StringUtils.EMPTY);
                if (resultAssert) {
                    throw new WebEngineException("The content of the element is empty");
                }
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.DISPLAYED.getTagValue(), value) && webElementDescription.isNotDisplayed()) {
                throw new WebEngineException("The element is not displayed");
            } else if (StringUtil.equalsIgnoreCase(PredefinedTagValue.NOT_DISPLAYED.getTagValue(), value) && webElementDescription.isDisplayed()) {
                throw new WebEngineException("The element is displayed");
            } else{
                resultAssert = webElementDescription.assertContentByElementType(value);
                if (!resultAssert) {
                    throw new WebEngineException("The value are not the same :" + value);
                }
            }
        }
    }
}
