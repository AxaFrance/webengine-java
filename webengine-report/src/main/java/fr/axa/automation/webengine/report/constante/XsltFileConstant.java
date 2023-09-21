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
public enum XsltFileConstant {
    XSLT_FILE_LIST(Arrays.asList(ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"panel-right/tab/tab-action-template.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"panel-right/general/array-view-template.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"index.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"panel-left/panel-left-template.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"panel-right/content-tab/content-tab-context-value-template.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"header/sub-header-template.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"header/chart-header-template.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"header/header-template.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"panel-left/status-template.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"panel-right/content-tab/content-tab-log-template.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"panel-right/panel-right-template.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"panel-right/content-tab/content-tab-information-template.xslt",
                                ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"panel-right/tab/tab-test-case-template.xslt")),

    XSLT_INDEX_FILE(Arrays.asList(ReportPathConstant.XSLT_DIRECTORY_NAME.getValue()+"/"+"index.xslt"));



    final List<String> value;
}
