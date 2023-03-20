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
public class IfCommandDataDriveByExcel extends CommandDataDriveByExcel{

//    <else if> @uid
//    <else if> @uid
//    <else > @uid
//    <end if> @uid

}
