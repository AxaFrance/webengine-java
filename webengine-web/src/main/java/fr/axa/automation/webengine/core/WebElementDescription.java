package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.exception.MultipleElementException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
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
            throw new MultipleElementException("Multiple element has found with the given selection criteria for this web element : "+toString());
        } else {
            return elements.iterator().next();
        }
    }

    private Collection<WebElement> calculWebElementByCriteria(Collection<WebElement> webElementList, Collection<WebElement> webElementListToJoin) {
        Collection<WebElement> newWebElementList;
        if (CollectionUtils.isEmpty(webElementList) && CollectionUtils.isEmpty(webElementListToJoin)) {
            return null;
        } else if (CollectionUtils.isEmpty(webElementList)) {
            newWebElementList = webElementListToJoin;
        } else {
            newWebElementList = webElementList.stream().filter(one -> webElementListToJoin.stream().anyMatch(two -> ((RemoteWebElement) two).getId().equals(((RemoteWebElement) one).getId()))).collect(Collectors.toList());
        }
        return newWebElementList;
    }


    @Override
    public Collection<WebElement> internalFindElements() {
        Collection<WebElement> webElementList = null;
        if (StringUtils.isNotEmpty(this.id)) {
            webElementList = getInternalFindElementsById(this.id);
        }
        if (StringUtils.isNotEmpty(this.name)) {
            Collection<WebElement> webElementByNameList = getInternalFindElementsByName(this.name);
            webElementList = calculWebElementByCriteria(webElementList, webElementByNameList);
        }
        if (StringUtils.isNotEmpty(this.className)) {
            Collection<WebElement> webElementByXpathList = getInternalFindElementByClassName(this.className);
            webElementList = calculWebElementByCriteria(webElementList, webElementByXpathList);
        }
        if (StringUtils.isNotEmpty(this.linkText)) {
            Collection<WebElement> webElementByLinkList = getInternalindElementByLinkText(this.linkText);
            webElementList = calculWebElementByCriteria(webElementList, webElementByLinkList);
        }
        if (StringUtils.isNotEmpty(this.tagName)) {
            Collection<WebElement> webElementByTagName = getInternalindElementByTagName(this.tagName);
            webElementList = calculWebElementByCriteria(webElementList, webElementByTagName);
        }
        if (StringUtils.isNotEmpty(this.cssSelector)) {
            Collection<WebElement> webElementByCssSelector = getInternalindElementByCssSelector(this.cssSelector);
            webElementList = calculWebElementByCriteria(webElementList, webElementByCssSelector);
        }
        if (StringUtils.isNotEmpty(this.xPath)) {
            Collection<WebElement> webElementByCssSelector = getInternalFindElementByXpath(this.xPath);
            webElementList = calculWebElementByCriteria(webElementList, webElementByCssSelector);
        }
        if (StringUtils.isNotEmpty(this.innerText)) {
            if (CollectionUtils.isNotEmpty(webElementList)) {
                webElementList = webElementList.stream().filter(x -> this.innerText.equalsIgnoreCase(x.getText())).collect(Collectors.toList());
            } else {
                webElementList = getInternalFindElementByXpath("//*[text()='{" + this.innerText + "}']");
            }
        }
        if (CollectionUtils.isNotEmpty(this.attributeList)) {
            List<String> attributes = new ArrayList<>();
            for (HtmlAttribute htmlAttribute : this.attributeList) {
                attributes.add("[{" + htmlAttribute.getName() + "}=\"{" + htmlAttribute.getValue() + "}\"]");
            }
            StringJoiner cssSelector = new StringJoiner("");
            for (String attribute : attributes) {
                cssSelector.add(attribute);
            }
            Collection<WebElement> webElementByAttributeList = getInternalindElementByCssSelector(cssSelector.toString());
            webElementList = calculWebElementByCriteria(webElementList, webElementByAttributeList);
        }
        if (CollectionUtils.isEmpty(webElementList)) {
            throw new NoSuchElementException("No such WebElement if found in the page");
        }

        return webElementList;
    }

    private List<WebElement> getInternalindElementByCssSelector(String cssSelector) {
        return useDriver.findElements(By.cssSelector(cssSelector));
    }

    private List<WebElement> getInternalindElementByTagName(String tagName) {
        return useDriver.findElements(By.tagName(tagName.toUpperCase()));

    }

    private List<WebElement> getInternalindElementByLinkText(String linkText) {
        return useDriver.findElements(By.linkText(linkText));
    }

    private Collection<WebElement> getInternalFindElementByClassName(String className) {
        String xPath = "//*[@class='{" + className + "}']";
        return useDriver.findElements(By.xpath(xPath));
    }

    private Collection<WebElement> getInternalFindElementByXpath(String xPath) {
        return useDriver.findElements(By.xpath(xPath));
    }


    private Collection<WebElement> getInternalFindElementsByName(String name) {
        return useDriver.findElements(By.name(name));
    }

    private Collection<WebElement> getInternalFindElementsById(String id) {
        return useDriver.findElements(By.id(this.id));
    }

    @Override
    protected Function<Void, byte[]> internalGetScreenshot() throws Exception {
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


    public void dragAndDropTo(AbstractElementDescription element) throws Exception {
        WebElement e1 = findElement();
        WebElement e2 = element.findElement();
        Actions act = new Actions(useDriver);
        act.dragAndDrop(e1, e2).build().perform();
    }

    public Select asSelect() throws Exception {
        WebElement element = findElement();
        return new Select(element);
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
                webElementFilter.click();
            }
            return null;
        };
        retry(fun,value);
    }

    public boolean waitUntilXpath(Long timeOutInSeconds) throws InterruptedException {
        By byXpath = By.xpath("//*[contains(text(),'"+this.innerText+"')]");
        WebElement webElement = (new WebDriverWait(getUseDriver(), timeOutInSeconds)
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
