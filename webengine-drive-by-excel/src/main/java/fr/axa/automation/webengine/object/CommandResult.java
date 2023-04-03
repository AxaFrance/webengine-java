package fr.axa.automation.webengine.object;

import fr.axa.automation.webengine.generated.ActionReport;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Builder
public class CommandResult {
    ActionReport actionReport;
    String savedData;
}
