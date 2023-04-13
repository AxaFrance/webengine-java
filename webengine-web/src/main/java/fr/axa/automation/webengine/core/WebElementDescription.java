package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.api.IFunction;
import fr.axa.automation.webengine.constante.HtmlAttributeConstante;
import fr.axa.automation.webengine.constante.HtmlTag;
import fr.axa.automation.webengine.constante.LocatingBy;
import fr.axa.automation.webengine.exception.MultipleElementException;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.util.ListUtil;
import fr.axa.automation.webengine.util.StringUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@SuperBuilder
@AllArgsConstructor
public class WebElementDescription extends AbstractElementDescription {

    public static final String INNER_HTML = "innerHTML";
    public static final String OUTER_HTML = "outerHTML";

    public static final String OK = "OK";

    String id;

    String name;
    String innerText;
    Collection<HtmlAttribute> attributeList;
    String xPath;
    String cssSelector;
    String className;
    String tagName;
    String linkText;

    public WebElementDescription() {
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
                '}';
    }

    @Override
    public WebElement internalFindElement() {
        Collection<WebElement> elements = internalFindElements();
        if (CollectionUtils.isNotEmpty(elements) && elements.size() > 1) {
            throw new MultipleElementException("Multiple element has found with the given selection criteria for this web element : ");
        } else {
            return elements.iterator().next();
        }
    }

    public Collection<WebElement> internalFindElements() {
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
                return ListUtil.findDuplicateElements(elements);
            }
        }
        throw new NoSuchElementException("No such WebElement found in the page");
    }

    private Collection<WebElement> getInternalFindElementByAttributeList(Collection<HtmlAttribute> attributeList) {
        if (CollectionUtils.isNotEmpty(attributeList)) {
            List<String> attributes = new ArrayList<>();
            attributeList.stream().forEach(htmlAttribute -> attributes.add("[{" + htmlAttribute.getName() + "}=\"{" + htmlAttribute.getValue() + "}\"]"));
            String cssSelector = String.join("", attributes);
            return getInternalFindElementByCssSelector(cssSelector);
        }
        return new ArrayList<>();
    }

    private Collection<WebElement> getInternalFindElementByInnerText(String innerText,Collection<WebElement> webElementList) {
        if (StringUtils.isNotEmpty(innerText)) {
            if (CollectionUtils.isNotEmpty(webElementList)) {
                return webElementList.stream().filter(webElement -> innerText.equalsIgnoreCase(webElement.getText())).collect(Collectors.toList());
            }
            return getInternalFindElementByXpath("//*[text()='" + innerText + "']");
        }
        return new ArrayList<>();
    }

    private List<WebElement> getInternalFindElementByCssSelector(String cssSelector) {
        if(StringUtils.isNotEmpty(cssSelector)) {
            return useDriver.findElements(By.cssSelector(cssSelector));
        }
        return new ArrayList<>();
    }

    private List<WebElement> getInternalFindElementByTagName(String tagName) {
        if(StringUtils.isNotEmpty(tagName)) {
            return useDriver.findElements(By.tagName(tagName.toUpperCase()));
        }
        return new ArrayList<>();
    }

    private List<WebElement> getInternalFindElementByLinkText(String linkText) {
        if(StringUtils.isNotEmpty(linkText)) {
            return useDriver.findElements(By.linkText(linkText));
        }
        return new ArrayList<>();
    }

    private Collection<WebElement> getInternalFindElementByClassName(String className) {
        if(StringUtils.isNotEmpty(className)) {
            String xPath = "//*[contains(@class, '{" + className + "}')]";
            return useDriver.findElements(By.xpath(xPath));
        }
        return new ArrayList<>();
    }

    private Collection<WebElement> getInternalFindElementByXpath(String xPath) {
        if(StringUtils.isNotEmpty(xPath)) {
            return useDriver.findElements(By.xpath(xPath));
        }
        return new ArrayList<>();
    }

    private Collection<WebElement> getInternalFindElementsByName(String name) {
        if(StringUtils.isNotEmpty(name)) {
            return useDriver.findElements(By.name(name));
        }
        return new ArrayList<>();
    }

    private Collection<WebElement> getInternalFindElementsById(String id) {
        if(StringUtils.isNotEmpty(id)){
            return useDriver.findElements(By.id(id));
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

    public void dragAndDropTo(AbstractElementDescription element) throws Exception {
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

    private void scrollToElement(String script, WebElement webElement) {
        JavascriptExecutor js = (JavascriptExecutor) useDriver;
        js.executeScript(script, webElement);
    }

    public void scrollIntoView() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            scrollToElement("arguments[0].scrollIntoView(true);", webElement);
            return null;
        };
        retry(fun,null);
    }


    public void scrollIntoViewAndclick() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            scrollToElement("arguments[0].scrollIntoView(true);", webElement);
            focus(webElement);
            webElement.click();
            return null;
        };
        retry(fun,null);
    }

    public void scrollIntoElementAndsendKeys(String text) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            WebElement webElement = findElement();
            scrollToElement("arguments[0].scrollIntoView(true);", webElement);
            focus(webElement);
            webElement.sendKeys(x);
            return null;
        };
        retry(fun,text);
    }


    public void scrollIntoCenterView() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            scrollToElement("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", webElement);
            return null;
        };
        retry(fun,null);
    }

    public void highLight() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            scrollToElement("arguments[0].style.border='3px solid red'", webElement);
            return null;
        };
        retry(fun,null);
    }

    public Boolean isInputSelect(WebElement webElement) throws Exception {
        IFunction<Void, Boolean> fun = (x) -> {
            if(webElement.getTagName().equalsIgnoreCase(HtmlTag.SELECT.getValue())){
                return true;
            }
            return false;
        };
        return retry(fun,null);
    }

    public Boolean isInputSelect() throws Exception {
        WebElement webElement = findElement();
        return isInputSelect(webElement);
    }

    public Boolean isInputRadio() throws Exception {
        return isTypeElementByAttribute(HtmlAttributeConstante.ATTRIBUTE_TYPE_RADIO);
    }

    public Boolean isInputText() throws Exception {
        return isTypeElementByAttribute(HtmlAttributeConstante.ATTRIBUTE_TYPE_TEXT);
    }

    public Boolean isInputCheckbox() throws Exception {
        return isTypeElementByAttribute(HtmlAttributeConstante.ATTRIBUTE_TYPE_CHECKBOX);
    }

    private Boolean isTypeElementByAttribute(HtmlAttributeConstante htmlAttributeConstante) throws Exception {

        IFunction<HtmlAttributeConstante, Boolean> fun = (x) -> {
            WebElement webElement = findElement();
            String typeWebElement = webElement.getAttribute("type");
            if(typeWebElement!=null && typeWebElement.equalsIgnoreCase(x.getValue())){
                return true;
            }
            return false;
        };
        return retry(fun, htmlAttributeConstante);
    }


    public Select asSelect() throws Exception {
        IFunction<Void, Select> fun = (x) -> {
            WebElement webElement = findElement();
            return new Select(webElement);
        };
        return retry(fun,null);
    }

    public void selectByValueOrText(String text) throws Exception {
        IFunction<String, Void> fun = (value) -> {
            WebElement webElement = this.findElement();
            scrollToElement("arguments[0].scrollIntoView(true);", webElement);
            focus(webElement);
            webElement.click();
            Select select = new Select(webElement);
            List<WebElement> elementListInSelect = getElementExistInSelect(select,value);
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

    private boolean isTextExistInSelect(Select select, String valueToSelect) {
        List<String> optionTextList = getOptionTextListInSelect(select);
        return optionTextList.stream().anyMatch(optionText -> StringUtil.equalsIgnoreCase(optionText,valueToSelect) || StringUtil.contains(optionText,valueToSelect.split("\\*{4}")[0].trim()));
    }

    private List<String> getOptionTextListInSelect(Select select) {
        return select.getOptions().stream().map(webElement ->  webElement.getText()).collect(Collectors.toList());
    }

    private List<WebElement> getElementExistInSelect(Select select, String valueToSelect) {
        List<WebElement> elementListInSelect = getElementListInSelect(select);
        return elementListInSelect.stream().filter(webElement -> StringUtil.equalsIgnoreCase(webElement.getText(),valueToSelect) || StringUtil.contains(webElement.getText(),valueToSelect.split("\\*{4}")[0].trim())).collect(Collectors.toList());
    }

    private List<WebElement> getElementListInSelect(Select select) {
        return select.getOptions().stream().map(webElement ->  webElement).collect(Collectors.toList());
    }

    private boolean isValueExistInSelect(Select select, String valueToSelect) {
        List<String> optionValueList = getOptionValueListInSelect(select);
        return optionValueList.stream().anyMatch(optionValue -> StringUtil.equalsIgnoreCase(optionValue,valueToSelect));
    }

    private List<String> getOptionValueListInSelect(Select select) {
        return select.getOptions().stream().map(webElement ->  webElement.getAttribute(HtmlAttributeConstante.ATTRIBUTE_VALUE.getValue())).collect(Collectors.toList());
    }

    public boolean assertContentByElementType(String text) throws Exception {
        IFunction<String, Boolean> fun = (value) ->{
            WebElement webElement = this.findElement();
            Boolean resultAssert;
            if(isInputSelect(webElement)){
                resultAssert = assertContentInSelect(webElement,value);
            }else if(StringUtil.equalsIgnoreCase(webElement.getTagName(),HtmlTag.INPUT.getValue()) || StringUtil.equalsIgnoreCase(webElement.getTagName(),HtmlTag.TEXTAREA.getValue())){
                resultAssert = StringUtil.contains(webElement.getAttribute(HtmlAttributeConstante.ATTRIBUTE_VALUE.getValue()),value);
            }else{
                resultAssert = StringUtil.contains(webElement.getText(),value);
            }
            if(!resultAssert){
                throw new WebEngineException("The value doesn't exist");
            }
            return resultAssert;
        };
        return retry(fun,text);
    }

    private boolean assertContentInSelect(WebElement webElement,String text){
        if(webElement!=null){
            Select select = new Select(webElement);
            if(isTextExistInSelect(select,text) || isValueExistInSelect(select,text)){
                return true;
            }
        }
        return false;
    }

    public void selectByText(String text) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            WebElement webElement = this.findElement();
            webElement.click();
            Select se = new Select(webElement);
            se.selectByVisibleText(x);
            return null;
        };
        retry(fun,text);
    }

    public void selectByIndex(Integer index) throws Exception {
        IFunction<Integer, Void> fun = (x) -> {
            WebElement element = this.findElement();
            element.click();
            Select se = new Select(element);
            se.selectByIndex(x);
            return null;
        };
        retry(fun,index);
    }

    public void selectByValue(String value) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            WebElement element = this.findElement();
            element.click();
            Select se = new Select(element);
            se.selectByValue(x);
            return null;
        };
        retry(fun,value);
    }

    public void scrollToElementAndcheckByValue(String value) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            Collection<WebElement> elementCollection = this.internalFindElements();
            if (CollectionUtils.isNotEmpty(elementCollection)) {
                WebElement webElement = elementCollection.stream().filter(webElt-> webElt.getAttribute("value").equalsIgnoreCase(x)).findFirst().orElse(null);
                scrollToElement("arguments[0].scrollIntoView(true);", webElement);
                focus(webElement);
                if(webElement!=null) {
                    webElement.click();
                }else{
                    throw new WebEngineException("Element is null");
                }
            }
            return null;
        };
        retry(fun,value);
    }

    public void checkByValue(String value) throws Exception {
        IFunction<String, Void> fun = (x) -> {
            Collection<WebElement> elementCollection = this.internalFindElements();
            if (CollectionUtils.isNotEmpty(elementCollection)) {
                WebElement webElementFilter = elementCollection.stream().filter(webElt-> webElt.getAttribute("value").equalsIgnoreCase(x)).findFirst().orElse(null);
                if(webElementFilter!=null) {
                    webElementFilter.click();
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


    public void focus() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = this.findElement();
            focus(webElement);
            return null;
        };
        retry(fun,null);
    }

    private void focus(WebElement webElement) throws Exception {
        new Actions(getUseDriver()).moveToElement(webElement).perform();
    }
}
