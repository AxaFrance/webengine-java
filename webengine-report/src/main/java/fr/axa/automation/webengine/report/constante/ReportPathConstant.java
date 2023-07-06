package fr.axa.automation.webengine.report.constante;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum ReportPathConstant {
    REPORT_DIRECTORY_NAME("report-test-result"),
    HTML_REPORT_DIRECTORY_NAME("html-report"),
    IMAGE_DIRECTORY_NAME("assets/img"),
    CSS_DIRECTORY_NAME("assets/css"),
    JS_DIRECTORY_NAME("assets/js"),
    XSLT_DIRECTORY_NAME(HTML_REPORT_DIRECTORY_NAME.getValue()+"/"+"xslt");

    final String value;
}
