package fr.axa.automation.webengine.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fr.axa.automation.webengine.cmd.CommandName;
import fr.axa.automation.webengine.constante.OptionalConstante;
import fr.axa.automation.webengine.constante.TargetKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
@NoArgsConstructor
@AllArgsConstructor
public class CommandDataNoCode {
    @JsonProperty("uid")
    private String uid;
    @JsonProperty("name")
    private String name;
    @JsonProperty("command")
    private CommandName command;
    @JsonProperty("targetList")
    private Map<TargetKey,String> targetList;
    @JsonProperty("optional")
    private String optional;
    @JsonProperty("dataTestReference")
    private String dataTestReference;
    @JsonProperty("dataTestList")
    private Map<String,String> dataTestMap;

    public boolean isOptional(){
        if(this!=null && StringUtils.isNotEmpty(this.getOptional()) && this.getOptional().equalsIgnoreCase(OptionalConstante.OPTIONAL.getValue())){
            return true;
        }
        return false;
    }

    public boolean isOptionalEmpty(){
        if(this!=null && StringUtils.isEmpty(this.getOptional())){
            return true;
        }
        return false;
    }

    public boolean isOptionalAndDependsOnPrevious(){
        if(this!=null && StringUtils.isNotEmpty(this.getOptional()) && this.getOptional().equalsIgnoreCase(OptionalConstante.OPTIONAL_AND_DEPENDS_ON_PREVIOUS.getValue())){
            return true;
        }
        return false;
    }

    public List<String> getDataTestReferenceList(){
        if(StringUtils.isNotEmpty(dataTestReference)){
            return Arrays.asList(dataTestReference.split(";"));
        }
        return new ArrayList<>();
    }


}
