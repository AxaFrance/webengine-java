package fr.axa.automation.webengine.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.Map;


@JsonPropertyOrder({
        "uid",
        "id",
        "comment",
        "command",
        "targetList",
        "optional",
        "dataTestReference",
        "dataTestList"
})
@Data
@Builder
public class CommandData {
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("id")
    private String id;
    @JsonProperty("command")
    private String command;
    @JsonProperty("targetList")
    private Map<String,String> targetList;
    @JsonProperty("optional")
    private String optional;
    @JsonProperty("dataTestReference")
    private String dataTestReference;
    @JsonProperty("dataTestList")
    private Map<String,String> dataTestList;
}
