package fr.axa.automation.webengine.object;

import fr.axa.automation.webengine.generated.ActionReport;
import fr.axa.automation.webengine.global.DriverContext;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Builder
public class CommandResult {
    CommandDataNoCode commandData;
    ActionReport actionReport;
    DriverContext driverContext;
    String savedData;
    List<CommandResult> subCommandResultList;
}
