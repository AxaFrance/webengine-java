package fr.axa.automation.webengine.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@JsonPropertyOrder({
        "uid",
        "version",
        "name",
        "testCaseList"
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSuiteDataDriveByExcel {
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("version")
    private String version;
    @JsonProperty("name")
    private String name;
    @JsonProperty("testCaseList")
    private List<TestCaseDataDriveByExcel> testCaseList;
}