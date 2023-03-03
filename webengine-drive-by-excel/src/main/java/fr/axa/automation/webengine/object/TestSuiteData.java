package fr.axa.automation.webengine.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;


@JsonPropertyOrder({
        "uid",
        "version",
        "name",
        "testCaseList"
})
@Data
public class TestSuiteData extends AbstractTestSuiteData{
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("version")
    private String version;
    @JsonProperty("name")
    private String name;
    @JsonProperty("testCaseList")
    private List<TestCaseData> testCaseList;
}