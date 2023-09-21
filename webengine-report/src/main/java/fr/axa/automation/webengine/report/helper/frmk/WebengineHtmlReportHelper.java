package fr.axa.automation.webengine.report.helper.frmk;


import fr.axa.automation.webengine.constant.FileExtensionConstant;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.report.builder.HtmlBuilder;
import fr.axa.automation.webengine.report.constante.HtmlFileConstant;
import fr.axa.automation.webengine.report.constante.ReportPathConstant;
import fr.axa.automation.webengine.report.constante.XsltFileConstant;
import fr.axa.automation.webengine.report.helper.ImageReportHelper;
import fr.axa.automation.webengine.report.helper.ReportFileNameHelper;
import fr.axa.automation.webengine.util.FileUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class WebengineHtmlReportHelper implements IWebengineHtmlReportHelper {

    final ILoggerService loggerService;

    @Autowired
    public WebengineHtmlReportHelper(ILoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public String buildHtmlReport(TestSuiteReport testSuiteReport, String outputPath, String xmlFileName) throws WebEngineException {

        Path htmlReportTargetDirectoryPath = FileUtil.createDirectories(outputPath + File.separator + ReportFileNameHelper.getDirectoryName(ReportPathConstant.HTML_REPORT_DIRECTORY_NAME.getValue()));
        Path cssTargetDirectoryPath = FileUtil.createDirectories(htmlReportTargetDirectoryPath.toAbsolutePath() + "/" + ReportPathConstant.CSS_DIRECTORY_NAME.getValue());
        Path jsTargetDirectoryPath = FileUtil.createDirectories(htmlReportTargetDirectoryPath.toAbsolutePath() + "/" + ReportPathConstant.JS_DIRECTORY_NAME.getValue());

        String basePathXslt = ReportPathConstant.XSLT_DIRECTORY_NAME.getValue() + "/"; //Don't use File.separator attribute because it's a path for xslt in the resource!!!!!!!
        String htmlIndexFilePath = htmlReportTargetDirectoryPath + File.separator + HtmlFileConstant.INDEX_HTML_FILE.getValue().get(0);
        try {
            copyFilesFromResource(ReportPathConstant.HTML_REPORT_DIRECTORY_NAME.getValue(),HtmlFileConstant.CSS_FILE_LIST.getValue(), cssTargetDirectoryPath.toAbsolutePath().toString());
            copyFilesFromResource(ReportPathConstant.HTML_REPORT_DIRECTORY_NAME.getValue(),HtmlFileConstant.JS_FILE_LIST.getValue(),jsTargetDirectoryPath.toAbsolutePath().toString());
            generateImageReport(testSuiteReport,htmlReportTargetDirectoryPath.toString());
            HtmlBuilder.build(xmlFileName,htmlIndexFilePath,basePathXslt, XsltFileConstant.XSLT_INDEX_FILE.getValue().get(0));
            loggerService.info("Create webengine html report in : " + htmlReportTargetDirectoryPath.toString());
        }catch (IOException | WebEngineException e  ){
            loggerService.error("Erreur lors de la génération du rapport html",e);
        }
        return Paths.get(htmlIndexFilePath).getParent().toString();
    }

    private void copyFilesFromResource(String basePath, List<String> fileNameList, String targetDirectoryName) throws IOException {
        InputStream inputStream;
        for (String fileName : fileNameList) {
            inputStream = FileUtil.getInputStreamByPathOrResource(basePath + "/" +fileName);
            FileUtil.copyFileFromResource(inputStream, targetDirectoryName + File.separator + Paths.get(fileName).getFileName().toString());
        }
    }

    private void generateImageReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException {
        List<ScreenshotReport> screenshotReportList = ImageReportHelper.getScreenShotReport(testSuiteReport);
        Path directoryPath = FileUtil.createDirectories(outputPath + File.separator + ReportPathConstant.IMAGE_DIRECTORY_NAME.getValue());
        for (ScreenshotReport screenshotReport :screenshotReportList) {
            String fileName = screenshotReport.getId() + FileExtensionConstant.PNG;
            Path completePath = Paths.get(directoryPath.toString(),fileName);
            FileUtil.saveAsImage(completePath,screenshotReport.getData());
        }
    }
}
