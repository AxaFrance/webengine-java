package fr.axa.automation.webengine.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.Set;


@JsonPropertyOrder({
        "uid",
        "name",
        "commandList"
})
@Data
@Builder
public class TestCaseData {
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("commandList")
    private Set<CommandData> commandList;
}