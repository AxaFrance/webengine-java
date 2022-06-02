package fr.axa.automation.webengine.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Builder
@AllArgsConstructor
public class WebElementDescription extends AbstractElementDescription {

    public static final String INNER_HTML = "innerHTML";
    public static final String OUTER_HTML = "outerHTML";

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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isNotEmpty(id)) {
            sb.append("id = ").append(id);
        }
        if (!StringUtils.isNotEmpty(name)) {
            sb.append("name = ").append(name);
        }
        if (!StringUtils.isNotEmpty(innerText)) {
            sb.append("innerText = ").append(innerText);
        }
        if (!StringUtils.isNotEmpty(xPath)) {
            sb.append("xPath = ").append(xPath);
        }
        if (!StringUtils.isNotEmpty(tagName)) {
            sb.append("xPath = ").append(xPath);
        }
        return sb.toString();
    }


    @Override
    public WebElement internalFindElement() throws Exception {
        Collection<WebElement> elements = internalFindElements();
        if (CollectionUtils.isNotEmpty(elements) && elements.size() > 1) {
            throw new Exception("Multiple element has found with the given selection criteria");
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
    public Collection<WebElement> internalFindElements() throws Exception {
        Collection<WebElement> webElementList = null;
        if (StringUtils.isNotEmpty(this.id)) {
            webElementList = getInternalFindElementsById();
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
            throw new Exception("No such WebElement");
        }

        return webElementList;
    }

    private List<WebElement> getInternalindElementByCssSelector(String cssSelector) {
        return webDriver.findElements(By.cssSelector(cssSelector));
    }

    private List<WebElement> getInternalindElementByTagName(String tagName) {
        return webDriver.findElements(By.tagName(tagName.toUpperCase()));

    }

    private List<WebElement> getInternalindElementByLinkText(String linkText) {
        return webDriver.findElements(By.linkText(linkText));
    }

    private Collection<WebElement> getInternalFindElementByClassName(String className) {
        String xPath = "//*[@class='{" + className + "}']";
        return webDriver.findElements(By.xpath(xPath));
    }

    private Collection<WebElement> getInternalFindElementByXpath(String xPath) {
        return webDriver.findElements(By.xpath(xPath));
    }


    private Collection<WebElement> getInternalFindElementsByName(String name) {
        return webDriver.findElements(By.name(name));
    }

    private Collection<WebElement> getInternalFindElementsById() {
        return webDriver.findElements(By.id(this.id));
    }

    @Override
    protected Function<Void, Byte[]> internalGetScreenshot() throws Exception {
        Function<Void, Byte[]> fun = (x) -> {
            try {
                WebElement element = findElement();
                if (element instanceof WebElement) {
                    return (Byte[]) ArrayUtils.toPrimitive(element.getScreenshotAs(OutputType.BASE64).getBytes());
                } else {
                    throw new Exception("Don't find a instanceof a WebElement");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return fun;
    }

    public void mouseHover() throws Exception {
        WebElement webElement = findElement();
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webElement).build().perform();
    }

    public void rightClick() throws Exception {
        WebElement webElement = findElement();
        Actions actions = new Actions(webDriver);
        actions.moveToElement(webElement).contextClick().build().perform();
    }

    public void dragAndDropTo(WebElement element) throws Exception {
        WebElement findWebElement = findElement();
        Actions actions = new Actions(webDriver);
        actions.dragAndDrop(findWebElement, element).build().perform();
    }

    public void scrollIntoView() throws Exception {
        WebElement findWebElement = findElement();
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].scrollIntoView(true);", findWebElement);
    }

    public void dragAndDropTo(AbstractElementDescription element) throws Exception {
        WebElement e1 = findElement();
        WebElement e2 = element.findElement();
        Actions act = new Actions(webDriver);
        act.dragAndDrop(e1, e2).build().perform();
    }

    public Select asSelect() throws Exception {
        WebElement element = findElement();
        return new Select(element);
    }

    public void selectByText(String text) throws Exception {
        perform(getFunctionInternalSelectByText(), text);
    }

    private Function<String, Void> getFunctionInternalSelectByText() {
        Function<String, Void> fun = (x) -> {
            try {
                WebElement element = this.internalFindElement();
                element.click();
                Select se = new Select(element);
                se.selectByVisibleText(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return fun;
    }

    public void selectByIndex(Integer index) throws Exception {
        perform(getFunctionInternalSelectByIndex(), index);
    }

    private Function<Integer, Void> getFunctionInternalSelectByIndex() {
        Function<Integer, Void> fun = (x) -> {
            try {
                WebElement element = this.internalFindElement();
                element.click();
                Select se = new Select(element);
                se.selectByIndex(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return fun;
    }

    public void selectByValue(String value) throws Exception {
        perform(getFunctionInternalSelectByValue(), value);
    }

    private Function<String, Void> getFunctionInternalSelectByValue() {
        Function<String, Void> fun = (x) -> {
            try {
                WebElement element = this.internalFindElement();
                element.click();
                Select se = new Select(element);
                se.selectByValue(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return fun;
    }

    public void checkByValue(String value) throws Exception {
        perform(getFunctionInternalCheckByValue(), value);
    }

    private Function<String, Void> getFunctionInternalCheckByValue() {
        Function<String, Void> fun = (x) -> {
            try {
                Collection<WebElement> elementCollection = this.internalFindElements();
                if (CollectionUtils.isNotEmpty(elementCollection)) {
                    for (WebElement webElt : elementCollection) {
                        if (webElt.getAttribute("value").equalsIgnoreCase(x)) {
                            webElt.click();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        return fun;
    }


}
