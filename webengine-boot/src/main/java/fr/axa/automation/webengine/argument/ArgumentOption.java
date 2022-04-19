package fr.axa.automation.webengine.argument;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum ArgumentOption {
    PROJECT("project",true,true,"project to run"),
    ENVIRONNEMENT_VARIABLE("env",true,true,"Environnement variable"),
    TEST_DATA("testData",true,true,"Data for project"),
    BROWSER("browser",true,true,"Data for project"),
    PLATFORM("platform",true,false,"Platform"),
    OUTPUT_DIR("outputDir",true,false,"output directory");

    final String option;
    final Boolean hasArg;
    final Boolean required;
    final String description;
}
