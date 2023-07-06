package fr.axa.automation.webengine.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;


@JsonPropertyOrder({
        "uid",
        "version",
        "name",
        "testCaseList"
})
@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestSuiteDataNoCode {

    @JsonProperty("uid")
    String uid;

    @JsonProperty("version")
    String version;

    @JsonProperty("name")
    String name;

    @JsonProperty("testCaseList")
    List<TestCaseDataNoCode> testCaseList;

    List<TestCaseNodeNoCode> testCaseNodeList;
}