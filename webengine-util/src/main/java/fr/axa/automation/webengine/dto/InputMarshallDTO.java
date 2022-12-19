package fr.axa.automation.webengine.dto;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.xml.namespace.QName;

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
