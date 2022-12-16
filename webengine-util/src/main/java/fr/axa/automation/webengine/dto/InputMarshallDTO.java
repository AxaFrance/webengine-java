package fr.axa.automation.webengine.dto;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
//import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
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
    String namespaceRoot = "";
    NamespacePrefixMapper namespacePrefixMapper;
}
