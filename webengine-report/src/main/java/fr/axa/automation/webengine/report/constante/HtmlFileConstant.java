package fr.axa.automation.webengine.report.constante;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum HtmlFileConstant {
    CSS_FILE_LIST(Arrays.asList(ReportPathConstant.CSS_DIRECTORY_NAME.getValue()+"/"+"badge.css",
                                ReportPathConstant.CSS_DIRECTORY_NAME.getValue()+"/"+"body-content.css",
                                ReportPathConstant.CSS_DIRECTORY_NAME.getValue()+"/"+"content-view.css",
                                ReportPathConstant.CSS_DIRECTORY_NAME.getValue()+"/"+"global.css",
                                ReportPathConstant.CSS_DIRECTORY_NAME.getValue()+"/"+"header.css",
                                ReportPathConstant.CSS_DIRECTORY_NAME.getValue()+"/"+"modal.css",
                                ReportPathConstant.CSS_DIRECTORY_NAME.getValue()+"/"+"tab.css",
                                ReportPathConstant.CSS_DIRECTORY_NAME.getValue()+"/"+"table.css",
                                ReportPathConstant.CSS_DIRECTORY_NAME.getValue()+"/"+"tree.css")),
    JS_FILE_LIST(Arrays.asList(ReportPathConstant.JS_DIRECTORY_NAME.getValue()+"/"+"global.js")),

    INDEX_HTML_FILE(Arrays.asList("index.html"));

    final List<String> value;
}
