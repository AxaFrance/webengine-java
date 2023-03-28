package fr.axa.automation.webengine.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@JsonPropertyOrder({
        "uid",
        "name",
        "commandList"
})
@Data
@Builder
public class TestCaseDataDriveByExcel {
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("commandList")
    private List<CommandDataDriveByExcel> commandList;
}