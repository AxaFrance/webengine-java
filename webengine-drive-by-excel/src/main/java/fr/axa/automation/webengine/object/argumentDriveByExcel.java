package fr.axa.automation.webengine.object;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class argumentDriveByExcel {
    String testCaseName;
    List<String> jddList;
}
