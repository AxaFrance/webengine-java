package fr.axa.automation.webengine.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;


@JsonPropertyOrder({
        "uid",
        "name",
        "commandList"
})
@Data
public class TestCaseData {
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("commandList")
    private List<CommandData> commandList;
}