package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.IFunction;
import fr.axa.automation.webengine.constante.Constant;
import fr.axa.automation.webengine.constante.HtmlAttributeConstant;
import fr.axa.automation.webengine.constante.HtmlTag;
import fr.axa.automation.webengine.constante.InputType;
import fr.axa.automation.webengine.constante.LocatingBy;
import fr.axa.automation.webengine.exception.MultipleElementException;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.global.AssertContentResult;
import fr.axa.automation.webengine.global.ElementContent;
import fr.axa.automation.webengine.global.ElementContentInputTypeRadio;
import fr.axa.automation.webengine.global.ElementContentSelect;
import fr.axa.automation.webengine.global.SettingsWeb;
import fr.axa.automation.webengine.util.FileUtil;
import fr.axa.automation.webengine.util.ListUtil;
import fr.axa.automation.webengine.util.StringUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@SuperBuilder
@AllArgsConstructor
public class WebElementDescription extends AbstractWebElement{

    public static final String INNER_HTML = "innerHTML";
    public static final String OUTER_HTML = "outerHTML";

    String id;

    String name;
    String innerText;
    Map<String,String> attributeList;
    String xPath;
    String cssSelector;
    String className;

    String tagName;
    String linkText;

    boolean shadowDom = false;
    PseudoElement pseudoElement;

    private final static String globalFunctionScript;
    private final static String shadowDomScript;
    private final static String cssSelectorGeneratorScript;

    static {
        try {
            globalFunctionScript = FileUtil.fileToText("js/global-function.js").toString();
            shadowDomScript = FileUtil.fileToText("js/shadow-dom-query-selector.js").toString();
            cssSelectorGeneratorScript = FileUtil.fileToText("js/css-selector-generator.js").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WebElementDescription() throws IOException {
        super();
    }

    public WebElementDescription(WebDriver webDriver) {
        super(webDriver);
    }

    public String getInnerHtml() throws Exception {
        return findElement().getAttribute(INNER_HTML);
    }

    public String getOuterHtml() throws Exception {
        return findElement().getAttribute(OUTER_HTML);
    }

    @Override
    public String toString() {
        return "WebElementDescription{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", innerText='" + innerText + '\'' +
                ", attributeList=" + attributeList +
                ", xPath='" + xPath + '\'' +
                ", cssSelector='" + cssSelector + '\'' +
                ", className='" + className + '\'' +
                ", tagName='" + tagName + '\'' +
                ", linkText='" + linkText + '\'' +
                ", pseudoElement='" + pseudoElement + '\'' +
                '}';
    }

    @Override
    public WebElement internalFindElement() throws Exception {
        Collection<WebElement> elements = internalFindElements();
        if (CollectionUtils.isNotEmpty(elements) && elements.size() > 1) {
            throw new MultipleElementException("Multiple element has found with the given selection criteria for this web element ");
        } else {
            return elements.iterator().next();
        }
    }

    public Collection<WebElement> internalFindElements() throws Exception {
        final List<WebElement> elements = new ArrayList<>();
        Map<String, Collection<WebElement>> findElementsMap = new HashMap<>();
        findElementsMap.put(LocatingBy.BY_ID.getValue(), getInternalFindElementsById(this.id));
        findElementsMap.put(LocatingBy.BY_NAME.getValue(), getInternalFindElementsByName(this.name));
        findElementsMap.put(LocatingBy.BY_CLASS_NAME.getValue(),  getInternalFindElementByClassName(this.className));
        findElementsMap.put(LocatingBy.BY_LINK_TEXT.getValue(), getInternalFindElementByLinkText(this.linkText));
        findElementsMap.put(LocatingBy.BY_TAG_NAME.getValue(), getInternalFindElementByTagName(this.tagName));
        findElementsMap.put(LocatingBy.BY_CSS_SELECTOR.getValue(), getInternalFindElementByCssSelector(this.cssSelector));
        findElementsMap.put(LocatingBy.BY_XPATH.getValue(), getInternalFindElementByXpath(this.xPath));
        findElementsMap.put(LocatingBy.BY_ATTRIBUTE_LIST.getValue(), getInternalFindElementByAttributeList(this.attributeList));

        findElementsMap.forEach((k, v) -> elements.addAll(findElementsMap.get(k)));

        List<WebElement> webElementBYInnerTextList = (List<WebElement>) getInternalFindElementByInnerText(this.innerText,elements);
        elements.addAll(webElementBYInnerTextList);

        if (CollectionUtils.isNotEmpty(elements)) {
            if(elements.size()==1){
                return elements;
            }else {
                Map<WebElement,Integer> duplicateElementMap = ListUtil.findNumberOfElements(elements);
                if(duplicateElementMap.size()>1 && duplicateElementMap.values().stream().distinct().count()==1){
                    throw new MultipleElementException("Multiple element has found with the given selection criteria for this web element ");
                }else{
                    Stream<Map.Entry<WebElement,Integer>> sorted = duplicateElementMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
                    return Arrays.asList(sorted.findFirst().get().getKey());
                }
            }
        }
        throw new NoSuchElementException("No such WebElement found in the page");
    }

    private Collection<WebElement> getInternalFindElementsById(String id) throws Exception {
        if(StringUtils.isNotEmpty(id)){
            String idTrimmed = StringUtils.trim(id);
            if(shadowDom){
                return Arrays.asList(getElementInShadowByXpath("//*[@id='"+idTrimmed+"']"));
            }else{
                return useDriver.findElements(By.id(idTrimmed));
            }
        }
        return new ArrayList<>();
    }

    private Collection<WebElement> getInternalFindElementsByName(String name) throws Exception {
        if(StringUtils.isNotEmpty(name)) {
            String nameTrimmed = StringUtils.trim(name);
            if(shadowDom){
                return Arrays.asList(getElementInShadowByXpath("//*[@name='"+nameTrimmed+"']"));
            }else {
                return useDriver.findElements(By.name(nameTrimmed));
            }
        }
        return new ArrayList<>();
    }

    private Collection<WebElement> getInternalFindElementByClassName(String className) throws Exception {
        if(StringUtils.isNotEmpty(className)) {
            String classNameTrimmed = StringUtils.trim(className);
            String xPath = "//*[contains(@class,'" + classNameTrimmed + "')]";
            if(shadowDom){
                return Arrays.asList(getElementInShadowByXpath(xPath));
            }else{
                return useDriver.findElements(By.xpath(xPath));
            }
        }
        return new ArrayList<>();
    }

    private List<WebElement> getInternalFindElementByLinkText(String linkText) throws Exception {
        if(StringUtils.isNotEmpty(linkText)) {
            String linkTextTrimmed = StringUtils.trim(linkText);
            if(shadowDom){
                String xpathLinkTextTrimmed = "//a[text()[normalize-space(.) = '"+linkTextTrimmed+"']]";
                return Arrays.asList(getElementInShadowByXpath(xpathLinkTextTrimmed));
            }else {
                return useDriver.findElements(By.linkText(linkTextTrimmed));
            }
        }
        return new ArrayList<>();
    }

    private List<WebElement> getInternalFindElementByTagName(String tagName) throws Exception {
        if(StringUtils.isNotEmpty(tagName)) {
            String tagNameTrimmed = StringUtils.trim(tagName).toUpperCase();
            if(shadowDom){
                return Arrays.asList(getElementInShadowByXpath("//"+tagNameTrimmed));
            }else{
                return useDriver.findElements(By.tagName(tagNameTrimmed));
            }
        }
        return new ArrayList<>();
    }

    private List<WebElement> getInternalFindElementByCssSelector(String cssSelector) throws Exception {
        if(StringUtils.isNotEmpty(cssSelector)) {
            String cssSelectorTrimmed = StringUtils.trim(cssSelector);
            if(shadowDom){
                return Arrays.asList(getElementInShadowByCssSelector(cssSelectorTrimmed));
            }else{
                return useDriver.findElements(By.cssSelector(cssSelectorTrimmed));
            }
        }
        return new ArrayList<>();
    }

    private Collection<WebElement> getInternalFindElementByAttributeList(Map<String,String> attributeList) throws Exception{
        if (MapUtils.isNotEmpty(attributeList)) {
            List<String> attributes = new ArrayList<>();
            attributeList.entrySet().stream().forEach(entry -> attributes.add("[" + entry.getKey() + "='" + entry.getValue() + "']"));
            String cssSelector = String.join("", attributes);
            return getInternalFindElementByCssSelector("*" + cssSelector);
        }
        return new ArrayList<>();
    }

    private Collection<WebElement> getInternalFindElementByInnerText(String innerText,Collection<WebElement> webElementList) throws Exception {
        if (StringUtils.isNotEmpty(innerText)) {
            if (CollectionUtils.isNotEmpty(webElementList)) {
                return webElementList.stream().filter(webElement -> innerText.equalsIgnoreCase(webElement.getText())).collect(Collectors.toList());
            }
            String innerTextTrimmed = StringEscapeUtils.escapeJava(StringUtils.trim(innerText));
            return getInternalFindElementByXpath("//*[text()='" + innerTextTrimmed + "']");
        }
        return new ArrayList<>();
    }

    private Collection<WebElement> getInternalFindElementByXpath(String xPath) throws Exception {
        if(StringUtils.isNotEmpty(xPath)) {
            String xPathTrimmed = StringUtils.trim(xPath);
            if(shadowDom){
                return Arrays.asList(getElementInShadowByXpath(xPathTrimmed));
            }else{
                return useDriver.findElements(By.xpath(xPathTrimmed));
            }
        }
        return new ArrayList<>();
    }

    @Override
    protected Function<Void, byte[]> internalGetScreenshot() {
        return (x) -> {
            try {
                WebElement element = findElement();
                if (element != null) {
                    return element.getScreenshotAs(OutputType.BYTES);
                } else {
                    throw new Exception("Don't find a instanceof a WebElement");
                }
            } catch (Exception e) {
                return null;
            }
        };
    }

    public void mouseHover() throws Exception {
        WebElement webElement = findElement();
        Actions actions = new Actions(useDriver);
        actions.moveToElement(webElement).build().perform();
    }

    public void rightClick() throws Exception {
        WebElement webElement = findElement();
        Actions actions = new Actions(useDriver);
        actions.moveToElement(webElement).contextClick().build().perform();
    }

    public void dragAndDropTo(AbstractWebElement element) throws Exception {
        WebElement e1 = findElement();
        WebElement e2 = element.findElement();
        Actions act = new Actions(useDriver);
        act.dragAndDrop(e1, e2).build().perform();
    }

    public void dragAndDropTo(WebElement element) throws Exception {
        WebElement findWebElement = findElement();
        Actions actions = new Actions(useDriver);
        actions.dragAndDrop(findWebElement, element).build().perform();
    }

    public Object executeJavascriptWithRetry(String script) throws Exception {
        IFunction<String, Object> fun = (scriptToExecute) -> {
            return executeJavascript(scriptToExecute);
        };
        return retry(fun,script);
    }

    public Object executeJavascript(String script) {
        JavascriptExecutor js = (JavascriptExecutor) useDriver;
        return js.executeScript(script);
    }

    public Object executeJavascript(String script, WebElement webElement) {
        JavascriptExecutor js = (JavascriptExecutor) useDriver;
        return js.executeScript(script, webElement);
    }

    /**
     *
     * @deprecated
     * Use method scrollToElement or focus()
     */
    @Deprecated
    public void scrollIntoView() throws Exception {
        scrollToElement();
    }

    /**
     *
     * @deprecated
     * Use method scrollToElementAndclick
     */
    public void scrollIntoViewAndclick() throws Exception {
        scrollToElementAndclick();
    }

    public void scrollToElement() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            scrollToElement(webElement);
            waitInMillisecondes(500L);
            return null;
        };
        retry(fun,null);
    }

    public void scrollToElement(WebElement webElement) throws Exception {
        IFunction<WebElement, Void> fun = (elt) -> {
            executeJavascript("arguments[0].scrollIntoView(true);", elt);
            return null;
        };
        retry(fun, webElement);
    }

    public void scrollToElementAndclick() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            executeJavascript("arguments[0].scrollIntoView(true);", webElement);
            click(webElement);
            return null;
        };
        retry(fun,null);
    }

    public void scrollIntoCenterView() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            executeJavascript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", webElement);
            return null;
        };
        retry(fun,null);
    }

    public void focusAndClick() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            focus(webElement);
            highLight(webElement);
            click(webElement);
            return null;
        };
        retry(fun,null);
    }

    public void focusAndDoubleClick() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            focus(webElement);
            highLight(webElement);
            doubleClick(webElement);
            return null;
        };
        retry(fun,null);
    }

    public void doubleClick(WebElement we) throws Exception {
        WebElement webElement = we;
        if(webElement==null){
            webElement = findElement();
        }
        Actions actions = new Actions(useDriver);
        actions.moveToElement(webElement).doubleClick(webElement).build().perform();
    }



    public void focusAndClickOnPseudoElement() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            focus(webElement);
            highLight(webElement);
            clickOnPseudoElement(webElement);
            return null;
        };
        retry(fun,null);
    }

    public void focusAndClickFromActions() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            focus(webElement);
            highLight(webElement);
            clickFromActions(webElement);
            return null;
        };
        retry(fun,null);
    }

    public void focusAndClickWithJS() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            focus(webElement);
            highLight(webElement);
            executeJavascript("arguments[0].click();", webElement);
            return null;
        };
        retry(fun,null);
    }

    private static void containsInputValue(WebElement webElement, String expectedValue) throws Exception {
        String actualValue = webElement.getAttribute(HtmlAttributeConstant.ATTRIBUTE_VALUE.getValue());
        if(!StringUtil.contains(actualValue,expectedValue)){
            throw new Exception("The expected value is :'" + expectedValue +"' and the actual value is :'" + actualValue + "'");
        }
    }

    public void sendKeysWithAssertion(String text) throws Exception {
        IFunction<String, Void> fun = (sendKeysValue) -> {
            sendKeysWithOption(false,true,sendKeysValue);
            return null;
        };
        retry(fun,text);
    }

    public void sendKeysWithoutAssertion(String text) throws Exception {
        IFunction<String, Void> fun = (sendKeysValue) -> {
            sendKeysWithOption(false,false,sendKeysValue);
            return null;
        };
        retry(fun,text);
    }

    public void sendKeysWithClearBefore(String text) throws Exception {
        IFunction<String, Void> fun = (sendKeysValue) -> {
            sendKeysWithOption(true,true,sendKeysValue);
            return null;
        };
        retry(fun,text);
    }

    public void sendKeysWithOption(boolean clearField, boolean assertion,String sendKeysValue)throws Exception {
        WebElement webElement = findElement();
        focus(webElement);
        highLight(webElement);
        if(clearField){
            clear();
        }
        webElement.sendKeys(sendKeysValue);
        waitInMillisecondes(SettingsWeb.RETRY_MILLISECONDS);
        if(assertion){
            containsInputValue(webElement, sendKeysValue);
        }
    }




    public void sendKeyboard(String text) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            WebElement webElement = findElement();
            focus(webElement);
            highLight(webElement);
            webElement.sendKeys(Keys.valueOf(x));
            return null;
        };
        retry(fun,text);
    }

    public void highLight() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            highLight(webElement);
            return null;
        };
        retry(fun,null);
    }

    private void highLight(WebElement webElement) {
        executeJavascript("arguments[0].style.border='1px solid red'", webElement);
    }

    public Boolean isSelect() throws Exception {
        IFunction<Void, Boolean> fun = (x) -> {
            WebElement webElement = findElement();
            return isSelect(webElement);
        };
        return retry(fun,null);
    }

    public Boolean isInput() throws Exception {
        IFunction<Void, Boolean> fun = (x) -> {
            WebElement webElement = findElement();
            return isInput(webElement);
        };
        return retry(fun,null);
    }

    public Boolean isTextarea() throws Exception {
        IFunction<Void, Boolean> fun = (x) -> {
            WebElement webElement = findElement();
            return isTextarea(webElement);
        };
        return retry(fun,null);
    }

    private Boolean isSelect(WebElement webElement) throws Exception {
        if(webElement.getTagName().equalsIgnoreCase(HtmlTag.SELECT.getValue())){
            return true;
        }
        return false;
    }

    public void focus() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = this.findElement();
            focus(webElement);
            return null;
        };
        retry(fun,null);
    }

    private Boolean isInputTypeRadio(WebElement webElement) {
        if(webElement.getTagName().equalsIgnoreCase(HtmlTag.INPUT.getValue()) &&  isInputTypeByAttribute(webElement, InputType.ATTRIBUTE_TYPE_RADIO)){
            return true;
        }
        return false;
    }

    public Boolean isInputTypeCheckbox(WebElement webElement) {
        if(webElement.getTagName().equalsIgnoreCase(HtmlTag.INPUT.getValue()) &&  isInputTypeByAttribute(webElement, InputType.ATTRIBUTE_TYPE_CHECKBOX)){
            return true;
        }
        return false;
    }

    public Boolean isInputTypeRadio() throws Exception {
        return isInputTypeByAttribute(InputType.ATTRIBUTE_TYPE_RADIO);
    }

    public Boolean isInputTypeText() throws Exception {
        return isInputTypeByAttribute(InputType.ATTRIBUTE_TYPE_TEXT);
    }

    public Boolean isInputTypeCheckbox() throws Exception {
        return isInputTypeByAttribute(InputType.ATTRIBUTE_TYPE_CHECKBOX);
    }

    public Boolean isInput(WebElement webElement){
        return StringUtil.equalsIgnoreCase(webElement.getTagName(),HtmlTag.INPUT.getValue());
    }

    public Boolean isTextarea(WebElement webElement) {
        return StringUtil.equalsIgnoreCase(webElement.getTagName(), HtmlTag.TEXTAREA.getValue());
    }

    private Boolean isInputTypeByAttribute(InputType inputType) throws Exception {
        IFunction<InputType, Boolean> fun = (attributeValueConstante) -> {
            WebElement webElement = findElement();
            return isInputTypeByAttribute(webElement, attributeValueConstante);
        };
        return retry(fun, inputType);
    }

    private Boolean isInputTypeByAttribute(WebElement webElement, InputType inputType) {
        String typeWebElement = webElement.getAttribute(HtmlAttributeConstant.ATTRIBUTE_TYPE.getValue());
        if(typeWebElement!=null && typeWebElement.equalsIgnoreCase(inputType.getValue())){
            return true;
        }
        return false;
    }

    public Select asSelect() throws Exception {
        IFunction<Void, Select> fun = (x) -> {
            WebElement webElement = findElement();
            return new Select(webElement);
        };
        return retry(fun,null);
    }

    public ElementContent getContentSelect() throws Exception {
        IFunction<String, ElementContent> fun = (param) ->{
            WebElement webElement = this.findElement();
            focus(webElement);
            return getTextAndValueContentInSelect(webElement);
        };
        return retry(fun,null);
    }

    public ElementContent getContentInputRadio() throws Exception {
        IFunction<String, ElementContent> fun = (param) -> {
            WebElement webElement = this.findElement();
            focus(webElement);
            return ElementContentInputTypeRadio.builder().attributeChecked(webElement.getAttribute(HtmlAttributeConstant.ATTRIBUTE_CHECKED.getValue())).build();
        };
        return retry(fun, null);
    }

    public ElementContent getContentInput() throws Exception {
        IFunction<String, ElementContent> fun = (param) -> {
            WebElement webElement = this.findElement();
            focus(webElement);
            return ElementContent.builder().value(webElement.getAttribute(HtmlAttributeConstant.ATTRIBUTE_VALUE.getValue())).build();
        };
        return retry(fun, null);
    }

    public ElementContent getContentTextarea() throws Exception {
        return  getContentInput();
    }

    public ElementContent getContentText() throws Exception {
        IFunction<String, ElementContent> fun = (param) -> {
            WebElement webElement = this.findElement();
            focus(webElement);
            return ElementContent.builder().value(webElement.getText()).build();
        };
        return retry(fun, null);
    }

    public AssertContentResult assertContentSelect(String expectedValue) throws Exception {
        IFunction<String, AssertContentResult> fun = (expectedValueParam) -> {
            ElementContentSelect elementContent = (ElementContentSelect) getContentSelect();
            Map<String, String> actualValueMap = elementContent.getValueAndTextMap()
                    .entrySet().stream()
                    .filter(entry -> StringUtil.equalsIgnoreCase(expectedValueParam, entry.getKey()) ||
                            (expectedValueParam.endsWith("****") && StringUtil.contains(entry.getKey(), expectedValueParam.split("\\*{4}")[0].trim())) ||
                            StringUtil.equalsIgnoreCase(expectedValueParam, entry.getValue()) ||
                            (expectedValueParam.endsWith("****") && StringUtil.contains(entry.getValue(), expectedValueParam.split("\\*{4}")[0].trim())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            String actualValue = elementContent.getValueAndTextMap().toString();
            boolean resultAssert;
            if (MapUtils.isEmpty(actualValueMap)) {
                throw new WebEngineException("The element is not selected. The expected value is : '" + expectedValueParam + "' and the actual value is : '" + actualValue + "'." );
            }else{
                resultAssert = true;
            }
            return AssertContentResult.builder().expectedValue(expectedValueParam).actualValue(actualValue).result(resultAssert).build();
        };
        return retry(fun, expectedValue);
    }

    public AssertContentResult assertRadioChecked() throws Exception {
        IFunction<String, AssertContentResult> fun = (expectedValueParam) -> {
            ElementContentInputTypeRadio elementContent = (ElementContentInputTypeRadio) getContentInputRadio();
            String actualValue = elementContent.getAttributeChecked();
            boolean resultAssert;
            if (actualValue.equalsIgnoreCase("false")) {
                throw new WebEngineException("The input radio is not checked. The expected value is : '" + expectedValueParam + "' and the actual value is : '" + actualValue + "'");
            } else {
                resultAssert = true;
            }
            return AssertContentResult.builder().expectedValue(expectedValueParam).actualValue(actualValue).result(resultAssert).build();
        };
        return retry(fun, "true");
    }

    public AssertContentResult assertRadioNotChecked() throws Exception {
        IFunction<String, AssertContentResult> fun = (expectedValueParam) -> {
            ElementContentInputTypeRadio elementContent = (ElementContentInputTypeRadio) getContentInputRadio();
            String actualValue = elementContent.getAttributeChecked();
            boolean resultAssert;
            if (actualValue.equalsIgnoreCase("true")) {
                throw new WebEngineException("The input radio is checked. The expected value is : '" + expectedValueParam + "' and the actual value is : '" + actualValue + "'");
            } else {
                resultAssert = true;
            }
            return AssertContentResult.builder().expectedValue(expectedValueParam).actualValue(actualValue).result(resultAssert).build();
        };
        return retry(fun, "false");
    }

    public AssertContentResult assertContentInput(String expectedValue) throws Exception {
        IFunction<String, AssertContentResult> fun = (expectedValueParam) -> {
            ElementContent elementContent = getContentInput();
            String actualValue = elementContent.getValue();
            boolean resultAssert = StringUtil.equalsIgnoreCase(actualValue, expectedValueParam) || (expectedValueParam.endsWith("****") && StringUtil.contains(actualValue, expectedValueParam.split("\\*{4}")[0].trim()));
            if (!resultAssert) {
                throw new WebEngineException("The expected value is : '" + expectedValueParam + "' and the actual value is : '" + actualValue + "'");
            }
            return AssertContentResult.builder().expectedValue(expectedValueParam).actualValue(actualValue).result(resultAssert).build();
        };
        return retry(fun, expectedValue);
    }

    public AssertContentResult assertNotEmptyContentInput() throws Exception {
        IFunction<String, AssertContentResult> fun = (param) -> {
            ElementContent elementContent = getContentInput();
            String actualValue = elementContent.getValue();
            boolean resultAssert = StringUtils.isEmpty(actualValue) ;
            if (resultAssert) {
                throw new WebEngineException("The content is empty. The actual value is : '" + actualValue + "'");
            }
            return AssertContentResult.builder().expectedValue(param).actualValue(actualValue).result(resultAssert).build();
        };
        return retry(fun, null);
    }


    public AssertContentResult assertContentTextarea(String expectedValue) throws Exception {
        return assertContentInput(expectedValue);
    }

    public AssertContentResult assertNotEmptyContentTextarea() throws Exception {
        return assertNotEmptyContentInput();
    }

    public AssertContentResult assertContentText(String expectedValue) throws Exception {
        IFunction<String, AssertContentResult> fun = (expectedValueParam) -> {
            ElementContent elementContent = getContentText();
            String actualValue = elementContent.getValue();
            boolean resultAssert = StringUtil.equalsIgnoreCase(actualValue, expectedValueParam) || (expectedValueParam.endsWith("****") && StringUtil.contains(actualValue, expectedValueParam.split("\\*{4}")[0].trim()));
            if (!resultAssert) {
                throw new WebEngineException("The expected value is : '" + expectedValueParam + "' and the actual value is : '" + actualValue + "'");
            }
            return AssertContentResult.builder().expectedValue(expectedValueParam).actualValue(actualValue).result(resultAssert).build();
        };
        return retry(fun, expectedValue);
    }

    public AssertContentResult assertNotEmptyContentText() throws Exception {
        IFunction<String, AssertContentResult> fun = (param) -> {
            ElementContent elementContent = getContentText();
            String actualValue = elementContent.getValue();
            boolean resultAssert = StringUtils.isEmpty(actualValue) ;
            if (resultAssert) {
                throw new WebEngineException("The text is empty. The actual value is : '" + actualValue + "'");
            }
            return AssertContentResult.builder().expectedValue(param).actualValue(actualValue).result(resultAssert).build();
        };
        return retry(fun, null);
    }


    public AssertContentResult assertContentByElementType(String expectedValue) throws Exception {
        if(isSelect()){
            return assertContentSelect(expectedValue);
        } else if (isInput() ) {
            return assertContentInput(expectedValue);
        } else if (isTextarea()) {
            return assertContentTextarea(expectedValue);
        }else{
            return assertContentText(expectedValue);
        }
    }

    public AssertContentResult assertContentEmpty() throws Exception {
        if (isInput() ) {
            return assertContentInput(StringUtils.EMPTY);
        } else if (isTextarea()) {
            return assertContentTextarea(StringUtils.EMPTY);
        }else{
            return assertContentText(StringUtils.EMPTY);
        }
    }

    public AssertContentResult assertContentNotEmpty() throws Exception {
        if (isInput() ) {
            return assertNotEmptyContentInput();
        } else if (isTextarea()) {
            return assertNotEmptyContentTextarea();
        }else{
            return assertNotEmptyContentText();
        }
    }

    public void selectByValueOrText(String text) throws Exception {
        IFunction<String, Void> fun = (value) -> {
            WebElement webElement = this.findElement();
            focus(webElement);
            click(webElement);
            Select select = new Select(webElement);
            List<WebElement> elementListInSelect = getElementByTextInSelect(select,value);
            if(CollectionUtils.isNotEmpty(elementListInSelect) && elementListInSelect.size()==1){
                select.selectByVisibleText(elementListInSelect.get(0).getText());
            }else if(isValueExistInSelect(select,value)){
                select.selectByValue(value);
            }else{
                throw new WebEngineException("The option value or the text : "+value+" doesn't exist");
            }
            return null;
        };
        retry(fun,text);
    }

    private boolean isValueExistInSelect(Select select, String expectedValue) {
        List<String> optionValueList = new ArrayList<>(getValueAndTextInInputSelect(select).keySet());
        return optionValueList.stream().anyMatch(optionValue -> StringUtil.equalsIgnoreCase(optionValue,expectedValue) || (expectedValue.endsWith("****") && StringUtil.contains(optionValue,expectedValue.split("\\*{4}")[0].trim())));
    }

    private boolean isTextExistInSelect(Select select, String expectedValue) {
        List<String> optionTextList = (List<String>) getValueAndTextInInputSelect(select).values();
        return optionTextList.stream().anyMatch(optionText -> StringUtil.equalsIgnoreCase(optionText,expectedValue) || (expectedValue.endsWith("****") && StringUtil.contains(optionText,expectedValue.split("\\*{4}")[0].trim())));
    }

    private List<WebElement> getElementByTextInSelect(Select select, String expectedValue) {
        return select.getOptions().stream().filter(webElement -> StringUtil.equalsIgnoreCase(webElement.getText(),expectedValue) || (expectedValue.endsWith("****") &&  StringUtil.contains(webElement.getText(),expectedValue.split("\\*{4}")[0].trim()))).collect(Collectors.toList());
    }

    public Map<String,String> getValueAndTextInInputSelect(Select select) {
        Map<String,String> valueAndText = new HashMap<>();
        select.getOptions().stream().forEach(webElement -> valueAndText.put(webElement.getAttribute(HtmlAttributeConstant.ATTRIBUTE_VALUE.getValue()),webElement.getText()));
        return valueAndText;
    }

    public String getTextByElement() throws Exception {
        IFunction<Void, String> fun = (value) ->{
            WebElement webElement = this.findElement();
            focus(webElement);
            if(isSelect(webElement)){
                return getSelectedOption(webElement).getValueAndTextMap().entrySet().stream().findFirst().get().getValue();
            }else if(isInputTypeByAttribute(webElement, InputType.ATTRIBUTE_TYPE_TEXT) || StringUtil.equalsIgnoreCase(webElement.getTagName(),HtmlTag.TEXTAREA.getValue())){
                return webElement.getAttribute(HtmlAttributeConstant.ATTRIBUTE_VALUE.getValue());
            }else{
                return webElement.getText();
            }
        };
        return retry(fun,null);
    }

    public ElementContent getSelectedOption() throws Exception {
        IFunction<String, ElementContent> fun = (value) ->{
            WebElement webElement = this.findElement();
            focus(webElement);
            if(isSelect(webElement)){
                return getSelectedOption(webElement);
            }else{
                throw new Exception("It's not a select element");
            }
        };
        return retry(fun,null);
    }



    public boolean isChecked() throws Exception {
        Boolean object = isCheckedByScript();
        if (object != null) return object;

        IFunction<Void, Boolean> fun = (value) ->{
            WebElement webElement = this.findElement();
            focus(webElement);
            if(isInputTypeRadio(webElement) || isInputTypeCheckbox(webElement)) {
                return StringUtil.equalsIgnoreCase(Constant.TRUE.getValue(), webElement.getAttribute(HtmlAttributeConstant.ATTRIBUTE_CHECKED.getValue()));
            }else{
                throw new Exception("It's not a radio or checkbox element");
            }
        };
        return retry(fun,null);
    }

    public Boolean isCheckedByScript() throws Exception {
        if (id != null) {
            String script = " return (document.getElementById('" + id + "').type == 'radio' || document.getElementById('" + id + "').type == 'checkbox') ";
            Boolean isRadioOrCheckbox = (Boolean) executeJavascript(script);
            if (isRadioOrCheckbox != null && !isRadioOrCheckbox) {
                throw new Exception("It's not a radio or checkbox element");
            }

            script = " return document.getElementById('" + id + "').checked; ";
            Boolean object = (Boolean) executeJavascript(script);
            if (object != null) {
                return object;
            }
        }
        return null;
    }

    private ElementContentSelect getSelectedOption(WebElement webElement) throws Exception{
        if(webElement!=null){
            Select select = new Select(webElement);
            if (!webElement.isEnabled()){
                throw new Exception("Select WebElement is not enabled for the while...");
            }
            List<WebElement> webElementList = select.getOptions().stream().filter(webElementOption -> webElementOption.isSelected()).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(webElementList)){
                if(webElementList.size()>1){
                    throw new Exception("Impossible case, many options are selected, only one element can be selected");
                }
                Optional<WebElement> webElementSelected = webElementList.stream().findFirst();
                if(webElementSelected.isPresent()){
                    Map<String, String> valueAndTextMap = new HashMap<>();
                    valueAndTextMap.put(webElementSelected.get().getAttribute(HtmlAttributeConstant.ATTRIBUTE_VALUE.getValue()), webElementSelected.get().getText());
                    return ElementContentSelect.builder().valueAndTextMap(valueAndTextMap).build();
                }
            }
        }
        return null;
    }

    private ElementContentSelect getTextAndValueContentInSelect(WebElement webElement){
        if(webElement!=null){
            Select select = new Select(webElement);
            return ElementContentSelect.builder().valueAndTextMap(getValueAndTextInInputSelect(select)).build();
        }
        return null;
    }

    private boolean assertContentInSelect(WebElement webElement,String expected){
        if(webElement!=null){
            Select select = new Select(webElement);
            if(isTextExistInSelect(select,expected) || isValueExistInSelect(select,expected)){
                return true;
            }
        }
        return false;
    }

    public void selectByText(String text) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            WebElement webElement = this.findElement();
            click(webElement);
            Select se = new Select(webElement);
            se.selectByVisibleText(x);
            return null;
        };
        retry(fun,text);
    }

    public void selectByIndex(Integer index) throws Exception {
        IFunction<Integer, Void> fun = (x) -> {
            WebElement element = this.findElement();
            click(element);
            Select se = new Select(element);
            se.selectByIndex(x);
            return null;
        };
        retry(fun,index);
    }

    public void selectByValue(String value) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            WebElement element = this.findElement();
            click(element);
            Select se = new Select(element);
            se.selectByValue(x);
            return null;
        };
        retry(fun,value);
    }

    public void checkByValue(String value) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            Collection<WebElement> elementCollection = this.internalFindElements();
            if (CollectionUtils.isNotEmpty(elementCollection)) {
                WebElement webElementFilter = elementCollection.stream().filter(webElt-> webElt.getAttribute(HtmlAttributeConstant.ATTRIBUTE_VALUE.getValue()).equalsIgnoreCase(x)).findFirst().orElse(null);
                if(webElementFilter!=null) {
                    focus(webElementFilter);
                    click(webElementFilter);
                }else{
                    throw new WebEngineException("Element is null");
                }
            }
            return null;
        };
        retry(fun,value);
    }

    public boolean waitUntilXpath(Long timeOutInSeconds) {
        By byXpath = By.xpath("//*[contains(text(),'"+this.innerText+"')]");
        WebElement webElement = (new WebDriverWait(getUseDriver(), Duration.ofSeconds(timeOutInSeconds))
                .ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class))
                .until(ExpectedConditions.presenceOfElementLocated(byXpath));
        return webElement!=null;
    }

    private void focus(WebElement webElement) {
        if(StringUtil.equalsIgnoreCase(webElement.getTagName(),HtmlTag.INPUT.getValue())
                && !StringUtil.equalsIgnoreCase(webElement.getAttribute(HtmlAttributeConstant.ATTRIBUTE_TYPE.getValue()), InputType.ATTRIBUTE_TYPE_FILE.getValue())){
            webElement.sendKeys("");
        } else{
            new Actions(getUseDriver()).moveToElement(webElement).perform();
        }
    }

    private void clickFromActions(WebElement webElement) {
        new Actions(getUseDriver()).moveToElement(webElement).click().build().perform();
    }

    private void clickOnPseudoElement(WebElement webElement) {
        Rectangle rectangle = webElement.getRect();
        int x = getPseudoElement().getX() != null ? getPseudoElement().getX() : 2;
        int xOffset = rectangle.getWidth()/2 - x;
        if(getPseudoElement().getValue() == "before"){
            xOffset = xOffset * (-1);
        }

        int yOffset = getPseudoElement().getY() != null ? getPseudoElement().getY() : 0;

        new Actions(getUseDriver()).moveToElement(webElement).moveByOffset(xOffset,yOffset).click().build().perform();
    }

    private Object executerGetObject(String libraryScript, String script) {
        return executerGetObject(libraryScript, script,null);
    }

    private Object executerGetObject(String libraryScript, String script, WebElement element) {
        String javascript = libraryScript;
        javascript += script;
        if(element==null){
            return executeJavascript(javascript);
        }
        return executeJavascript(javascript,element);
    }

    public WebElement getElementInShadowByXpath(String xPath) throws Exception {
        IFunction<String, WebElement> fun = (x) -> {
            WebElement webElement =  (WebElement) executerGetObject(shadowDomScript,String.format("return getXPathObject(\"%s\");", x));
            if(webElement==null){
                throw new Exception("The element is null");
            }
            return webElement;
        };
        String xpathWithDoubleSlashes = xPath.replaceAll("(?<!/)/(?!/)","//");
        return retry(fun,xpathWithDoubleSlashes);
    }

    public WebElement getElementInShadowByCssSelector(String cssSelector) throws Exception {
        IFunction<String, WebElement> fun = (x) -> {
            WebElement webElement =  (WebElement) executerGetObject(shadowDomScript,String.format("return getObject(\"%s\");", x));
            if(webElement==null){
                throw new Exception("The element is null");
            }
            return webElement;
        };
        return retry(fun,cssSelector);
    }
}
