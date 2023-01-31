package fr.axa.automation.webengine.core;

import fr.axa.automation.webengine.generated.Variable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseAdditionalInformation {
    String testCaseName;
    List<Variable> additionalDataList;
    List<Variable> missingDataList;
    boolean canRun;
}
