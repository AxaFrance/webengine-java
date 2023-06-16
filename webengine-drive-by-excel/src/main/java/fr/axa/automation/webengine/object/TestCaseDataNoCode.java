package fr.axa.automation.webengine.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;


@JsonPropertyOrder({
        "uid",
        "name",
        "commandList"
})
@FieldDefaults(level = AccessLevel.PROTECTED)
@Data
@Builder
public class TestCaseDataNoCode {
    @JsonProperty("uid")
    String uid;
    @JsonProperty("name")
    String name;
    @JsonProperty("commandList")
    List<CommandDataNoCode> commandList;
}