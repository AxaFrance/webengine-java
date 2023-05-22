package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.MultipleElementException;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.util.ListUtil;
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
        findElementsMap.put("id", getInternalFindElementsById(this.id));
        findElementsMap.put("name", getInternalFindElementsByName(this.name));
        findElementsMap.put("className", getInternalFindElementByClassName(this.className));
        findElementsMap.put("linkText", getInternalFindElementByLinkText(this.linkText));
        findElementsMap.put("tagName", getInternalFindElementByTagName(this.tagName));
        findElementsMap.put("cssSelector", getInternalFindElementByCssSelector(this.cssSelector));
        findElementsMap.put("xPath", getInternalFindElementByXpath(this.xPath));
        findElementsMap.put("attributeList", getInternalFindElementByAttributeList(this.attributeList));

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

    public void scrollIntoView() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement findWebElement = findElement();
            JavascriptExecutor js = (JavascriptExecutor) useDriver;
            js.executeScript("arguments[0].scrollIntoView(true);", findWebElement);
            return null;
        };
        retry(fun,null);
    }

    public void scrollIntoViewAndclick() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement webElement = findElement();
            JavascriptExecutor js = (JavascriptExecutor) useDriver;
            js.executeScript("arguments[0].scrollIntoView(true);", webElement);
            webElement.click();
            return null;
        };
        retry(fun,null);
    }

    public void scrollIntoCenterView() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement findWebElement = findElement();
            JavascriptExecutor js = (JavascriptExecutor) useDriver;
            js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", findWebElement);
            return null;
        };
        retry(fun,null);
    }

    public void highLight() throws Exception {
        IFunction<Void, Void> fun = (x) -> {
            WebElement findWebElement = findElement();
            JavascriptExecutor js = (JavascriptExecutor) useDriver;
            js.executeScript("arguments[0].style.border='3px solid red'", findWebElement);
            return null;
        };
        retry(fun,null);
    }



    public Select asSelect() throws Exception {
        IFunction<Void, Select> fun = (x) -> {
            WebElement element = findElement();
            return new Select(element);
        };
        return retry(fun,null);
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
            WebElement element = this.findElement();
            new Actions(getUseDriver()).moveToElement(element).perform();
            return null;
        };
        retry(fun,null);
    }
}
