package fr.axa.automation.webengine.argument;

import fr.axa.automation.webengine.constante.IConstant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum ArgumentOption {

    PROJECT("a",true,true, "Project to run"),
    ENVIRONNEMENT_VARIABLE("env",true,true, "Environnement variable"),
    TEST_DATA("data",true,true, "Data for project"),
    BROWSER("browser",true,true, "Data for project"),
    PLATFORM("platform",true,false, "Platform"),
    OUTPUT_DIR("outputDir",true,false, "Output directory"),
    MANUAL_DEBUG("m",false,false, "Manual debug"),
    JUNIT("junit",true,false, "Generate à Junit test report"),
    SHOW_REPORT("showreport",false,false, "Launch report viewer after execution");

    final String option;
    final Boolean hasArg;
    final Boolean required;
    final String description;
}
