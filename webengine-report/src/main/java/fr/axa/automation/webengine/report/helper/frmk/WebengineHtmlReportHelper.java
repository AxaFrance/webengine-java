package fr.axa.automation.webengine.report.helper.frmk;


import fr.axa.automation.webengine.builder.HtmlBuilder;
import fr.axa.automation.webengine.constant.FileExtensionConstant;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.report.constante.HtmlFileConstant;
import fr.axa.automation.webengine.report.constante.ReportConstant;
import fr.axa.automation.webengine.report.helper.ImageReportHelper;
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


    public void buildHtmlReport(TestSuiteReport testSuiteReport, String outputPath, String xmlFileName) throws WebEngineException {
        String assetsSourceDirectory = ReportConstant.HTML_REPORT_DIRECTORY_NAME.getValue() + "/" + ReportConstant.ASSETS_DIRECTORY_NAME.getValue() + "/";
        String cssSourceDirectory = assetsSourceDirectory + ReportConstant.CSS_DIRECTORY_NAME.getValue();
        String jsSourceDirectory = assetsSourceDirectory + ReportConstant.JS_DIRECTORY_NAME.getValue();

        Path htmlReportTargetDirectoryPath = FileUtil.createDirectories(outputPath + File.separator + ReportConstant.HTML_REPORT_DIRECTORY_NAME.getValue());
        Path cssTargetDirectoryPath = FileUtil.createDirectories(htmlReportTargetDirectoryPath.toAbsolutePath() + File.separator + ReportConstant.ASSETS_DIRECTORY_NAME.getValue() + File.separator + ReportConstant.CSS_DIRECTORY_NAME.getValue());
        Path jsTargetDirectoryPath = FileUtil.createDirectories(htmlReportTargetDirectoryPath.toAbsolutePath() + File.separator +ReportConstant.ASSETS_DIRECTORY_NAME.getValue() + File.separator + ReportConstant.JS_DIRECTORY_NAME.getValue());

        String basePathXslt = ReportConstant.HTML_REPORT_DIRECTORY_NAME.getValue() + "/" + ReportConstant.XSLT_DIRECTORY_NAME.getValue() + "/";
        String htmlIndexFilePath = outputPath + File.separator + ReportConstant.HTML_REPORT_DIRECTORY_NAME.getValue() + File.separator + "index.html";
        try {
            copyFilesFromResource2(cssSourceDirectory,cssTargetDirectoryPath.toAbsolutePath().toString(),HtmlFileConstant.CSS_FILE_LIST.getValue());
            copyFilesFromResource2(jsSourceDirectory,jsTargetDirectoryPath.toAbsolutePath().toString(),HtmlFileConstant.JS_FILE_LIST.getValue());
            generateImageReport(testSuiteReport,htmlReportTargetDirectoryPath.toString());
            loggerService.info("Xml file name : " + xmlFileName);
            loggerService.info("html file name : " + htmlIndexFilePath);
            loggerService.info("base xslt path : " + basePathXslt);
            loggerService.info("xslt path : " + basePathXslt + ReportConstant.XSLT_INDEX_NAME.getValue());
            HtmlBuilder.build(xmlFileName,htmlIndexFilePath,basePathXslt, basePathXslt + ReportConstant.XSLT_INDEX_NAME.getValue());
        }catch (IOException | WebEngineException e  ){
            loggerService.error("Erreur lors de la génération du rapport html",e);
        }
        loggerService.info("Create webengine html report in : " + htmlIndexFilePath);
    }

    private void copyFilesFromResource2( String sourceDirectoryName, String targetDirectoryName, List<String> fileNameList) throws IOException {
        InputStream inputStream;
        for (String fileName : fileNameList) {
            inputStream = FileUtil.getInputStreamByPathOrResource(sourceDirectoryName + "/" + fileName);
            FileUtil.copyFileFromResource(inputStream, targetDirectoryName + File.separator + fileName);
        }
    }

    private void generateImageReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException {
        List<ScreenshotReport> screenshotReportList = ImageReportHelper.getScreenShotReport(testSuiteReport);
        Path directoryPath = FileUtil.createDirectories(outputPath + File.separator + ReportConstant.ASSETS_DIRECTORY_NAME.getValue() + File.separator + ReportConstant.IMAGE_DIRECTORY_NAME.getValue());
        for (ScreenshotReport screenshotReport :screenshotReportList) {
            String fileName = screenshotReport.getId() + FileExtensionConstant.PNG;
            Path completePath = Paths.get(directoryPath.toString(),fileName);
            FileUtil.saveAsImage(completePath,screenshotReport.getData());
        }
    }
}
