package fr.axa.automation.webengine.report.helper.frmk;


import fr.axa.automation.webengine.HtmlBuilder;
import fr.axa.automation.webengine.constant.FileExtensionConstant;
import fr.axa.automation.webengine.dto.InputMarshallDTO;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.ScreenshotReport;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.report.constante.ReportConstant;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class WebengineReportHelper implements IWebengineReportHelper {

    public static final String WEBENGINE_REPORT_NAME = "webengine-report";
    public static final String NAMESPACE_WEBENGINE_REPORT = "http://www.axa.fr/WebEngine/2022";
    public static final String NS = "ns";
    final ILoggerService loggerService;

    @Autowired
    public WebengineReportHelper(ILoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @Override
    public String generateWebengineXmlReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException {
        Path directoryPath = FileUtil.createDirectories(outputPath);
        String fileName = ReportFileNameHelper.getFileName(WEBENGINE_REPORT_NAME);
        Path completePath = Paths.get(directoryPath.toString(),fileName);
        String webengineReportPath = FileUtil.saveAsXml(getInputMarshallDTO(testSuiteReport,completePath));
        loggerService.info("Create webengine report in : "+webengineReportPath);
        return webengineReportPath;
    }

    private InputMarshallDTO getInputMarshallDTO(TestSuiteReport testSuiteReport, Path completePath) {
        return InputMarshallDTO.builder().fileDestinationPath(completePath.toAbsolutePath().toString())
                .objectToMarshall(testSuiteReport)
                .namespace(NAMESPACE_WEBENGINE_REPORT)
                .prefix(NS).build();
    }

    public void generateWebengineHtmlReport(TestSuiteReport testSuiteReport, String outputPath, String xmlFileName) throws WebEngineException {
        String assetsSourceDirectory = ReportConstant.HTML_REPORT_DIRECTORY_NAME.getValue() + File.separator + ReportConstant.ASSETS_DIRECTORY_NAME.getValue() + File.separator;
        String cssSourceDirectory = assetsSourceDirectory + ReportConstant.CSS_DIRECTORY_NAME.getValue();
        String jsSourceDirectory = assetsSourceDirectory + ReportConstant.JS_DIRECTORY_NAME.getValue();

        Path htmlReportTargetDirectoryPath = FileUtil.createDirectories(outputPath + File.separator + ReportConstant.HTML_REPORT_DIRECTORY_NAME.getValue());
        Path cssTargetDirectoryPath = FileUtil.createDirectories(htmlReportTargetDirectoryPath.toAbsolutePath() + File.separator + ReportConstant.CSS_DIRECTORY_NAME.getValue());
        Path jsTargetDirectoryPath = FileUtil.createDirectories(htmlReportTargetDirectoryPath.toAbsolutePath() + File.separator + ReportConstant.JS_DIRECTORY_NAME.getValue());
        String htmlIndexFilePath = outputPath + File.separator + ReportConstant.HTML_REPORT_DIRECTORY_NAME.getValue() + File.separator + "index.html";
        try {
            copyFilesFromResource(cssSourceDirectory,cssTargetDirectoryPath.toAbsolutePath().toString());
            copyFilesFromResource(jsSourceDirectory,jsTargetDirectoryPath.toAbsolutePath().toString());
            generateImageReport(testSuiteReport,htmlReportTargetDirectoryPath.toString());
            HtmlBuilder.build(xmlFileName,htmlIndexFilePath,FileUtil.getInputStreamFromResource(ReportConstant.HTML_REPORT_DIRECTORY_NAME.getValue() + File.separator + ReportConstant.XSLT_DIRECTORY_NAME.getValue() + File.separator + ReportConstant.XSLT_INDEX_NAME.getValue()));
        }catch (IOException | WebEngineException e  ){
            throw new WebEngineException("Erreur lors de la génération du rapport html",e);
        }
    }

    private void copyFilesFromResource( String sourceDirectoryName, String targetDirectoryName) throws IOException {
        List<String> cssFileList = FileUtil.getResourceFiles(sourceDirectoryName);
        for (String cssFileName:cssFileList) {
            FileUtil.copyFileFromResource(sourceDirectoryName + File.separator + cssFileName, targetDirectoryName + File.separator + cssFileName);
        }
    }

    private void generateImageReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException {
        List<ScreenshotReport> screenshotReportList = ImageReportHelper.getScreenShotReport(testSuiteReport);
        Path directoryPath = FileUtil.createDirectories(outputPath + File.separator + ReportConstant.IMAGE_DIRECTORY_NAME.getValue());
        for (ScreenshotReport screenshotReport :screenshotReportList) {
            String fileName = screenshotReport.getId() + FileExtensionConstant.JPG;
            Path completePath = Paths.get(directoryPath.toString(),fileName);
            FileUtil.saveAsImage(completePath,screenshotReport.getData());
        }
    }
}
