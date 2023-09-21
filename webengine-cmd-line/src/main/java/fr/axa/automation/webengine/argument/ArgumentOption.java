package fr.axa.automation.webengine.argument;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum ArgumentOption {

    PROJECT                 ("a",           true,true, "Project to run"),
    ENVIRONMENT_VARIABLE("env",         true,true, "Environnement variable"),
    TEST_DATA               ("data",        true,true, "Data for project"), //Used in keyword and drive by excel project
    PROPERTIES_FILE_LIST    ("pfl",  true,false, "properties file list for configuration (separated by ';')"),
    PLATFORM                ("platform",    true,false, "Platform"),
    BROWSER                 ("browser",     true,false, "Data for project"),
    JUNIT                   ("junit",       true,false, "Generate à Junit test report"),
    OUTPUT_DIR              ("outputDir",   true,false, "Output directory"),
    MANUAL_DEBUG            ("m",           false,false, "Manual debug"),
    SHOW_REPORT             ("showReport",  true,false, "Launch report viewer after execution"),
    TEST_CASE_TO_RUN        ("tc",  true,false, "Test case to run (separated by ';'), example: \"-tc:firsttestcase;secondtestcase\" "), //For keyword project
    TEST_CASE_AND_DATA_TEST_COLUMN_NAME  ("tc",  true,false, "Test case to run (separated by ';'), example: \"-tc:firsttestcase[-dataColumName:jdd-rec-auto;jdd-rec-moto] ; secondtestcase[-dataColumName:jdd-rec-moto]\" "), //For drive by excel

    KEEPASS_FILE            ("keepassFile", true,false, "Keepass file"),
    KEEPASS_PASSWORD             ("keepassPassword",  true,false, "Keepass Password"),
    CLOSE_BROWSER_AFTER_EACH_SCENARIO ("closeBrowser",  true,false, "Close browser after each scenario"),
    DELETE_TEMP_FILE        ("deleteTempFile",  true,false, "Delete temp file after execution");

    final String option;
    final Boolean hasArg;
    final Boolean required;
    final String description;
}
