package fr.axa.automation.webengine.report.constante;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum ReportConstant {
    REPORT_DIRECTORY_NAME("report-test-result"),
    HTML_REPORT_DIRECTORY_NAME("html-report"),
    ASSETS_DIRECTORY_NAME("assets"),
    IMAGE_DIRECTORY_NAME("img"),
    CSS_DIRECTORY_NAME("css"),
    JS_DIRECTORY_NAME("js"),
    XSLT_DIRECTORY_NAME("xslt"),
    XSLT_INDEX_NAME("index.xslt");

    final String value;
}
