package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.constante.LocatingBy;
import fr.axa.automation.webengine.core.WebElementDescription;
import fr.axa.automation.webengine.object.CommandData;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
public abstract class AbstractDriverCommand extends AbstractCommand{

    WebElementDescription webElementDescription;

    protected void populateWebElement(CommandData commandData){
        commandData.getTargetList().forEach((by,value)-> {
            webElementDescription = WebElementDescription.builder()
                    .id(populateBySelector(by,value))
                    .name(populateBySelector(by,value))
                    .className(populateBySelector(by,value))
                    .linkText(populateBySelector(by,value))
                    .tagName(populateBySelector(by,value))
                    .cssSelector(populateBySelector(by,value))
                    .xPath(populateBySelector(by,value))
                    .build();
        });
    }

    protected String populateBySelector(String by, String value){
        switch (LocatingBy.valueOf(by)){
            case BY_ID:
            case BY_NAME:
            case BY_CLASS_NAME:
            case BY_LINK_TEXT:
            case BY_TAG_NAME:
            case BY_CSS_SELECTOR :
            case BY_XPATH:
                return StringUtils.trim(value);

            default:
                return StringUtils.EMPTY;
        }
    }


}
