package fr.axa.automation.webengine.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InputMarshallDTO {
    String fileDestinationPath;
    Object objectToMarshall;
    @Builder.Default
    boolean upperCaseRootElement=true;
    @Builder.Default
    String namespace = "";
    @Builder.Default
    String prefix = "";
}
