package fr.axa.automation.webengine.object;

import fr.axa.automation.webengine.generated.ActionReport;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;

import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Builder
public class CommandResult {
    CommandDataNoCode commandData;
    ActionReport actionReport;
    WebDriver webDriver;
    String savedData;
    List<CommandResult> subCommandResultList;
}
