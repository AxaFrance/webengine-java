package fr.axa.automation.webengine.argument;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum ArgumentOption {

    PROJECT                 ("a",           true,true, "Project to run"),
    ENVIRONMENT_VARIABLE("env",         true,true, "Environnement variable"),
    TEST_DATA               ("data",        true,true, "Data for project"),
    PROPERTIES_FILE_LIST    ("pfl",  true,false, "properties file list for configuration (separated by ';')"),
    PLATFORM                ("platform",    true,false, "Platform"),
    BROWSER                 ("browser",     true,false, "Data for project"),
    JUNIT                   ("junit",       true,false, "Generate à Junit test report"),
    OUTPUT_DIR              ("outputDir",   true,false, "Output directory"),
    MANUAL_DEBUG            ("m",           false,false, "Manual debug"),
    SHOW_REPORT             ("showreport",  false,false, "Launch report viewer after execution"),
    TEST_CASE_TO_RUN        ("tc",  true,false, "Test case to run (separated by ';')");

    final String option;
    final Boolean hasArg;
    final Boolean required;
    final String description;

    public static boolean isOptionForProject(String option){
        return Arrays.stream(ArgumentOption.values()).anyMatch(argumentOption -> option.startsWith("-"+argumentOption.getOption()));

    }
}
