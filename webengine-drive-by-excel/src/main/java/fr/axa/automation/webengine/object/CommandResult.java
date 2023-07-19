package fr.axa.automation.webengine.object;

import fr.axa.automation.webengine.generated.ActionReport;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Builder
public class CommandResult {
    CommandDataNoCode commandData;
    ActionReport actionReport;
    String savedData;
    WebDriver webDriver ;
    List<CommandResult> subCommandResultList = new ArrayList<>();

    public void addSubCommandResult(List<CommandResult> commandResult) {
        if (subCommandResultList == null) {
            subCommandResultList = new ArrayList<>();
        }
        subCommandResultList.addAll(commandResult);
    }
}
