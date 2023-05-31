package fr.axa.automation.webengine.report.helper.frmk;


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

    @Override
    public void generateWebengineHtmlReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException {
        Path htmlDirectoryPath = FileUtil.createDirectories(outputPath + File.separator + ReportConstant.HTML_REPORT_DIRECTORY_NAME.getValue());
        try {
        copyDirectory(htmlDirectoryPath,ReportConstant.CSS_DIRECTORY_NAME.getValue(),ReportConstant.CSS_DIRECTORY_NAME.getValue());
        copyDirectory(htmlDirectoryPath,ReportConstant.JS_DIRECTORY_NAME.getValue(),ReportConstant.JS_DIRECTORY_NAME.getValue());
        generateImageReport(testSuiteReport,htmlDirectoryPath.toString());
        }catch (IOException | WebEngineException e  ){
            throw new WebEngineException("Erreur lors de la génération du rapport html",e);
        }
    }

    private void copyDirectory(Path parentDirectory, String sourceDirectoryName, String targetDirectoryName) throws IOException {
        String assetsDirectory = ReportConstant.ASSETS_DIRECTORY_NAME.getValue() + File.separator;
        String cssSourceDirectory = assetsDirectory + sourceDirectoryName;
        String cssTargetDirectory = parentDirectory + File.separator + targetDirectoryName;
        FileUtil.copyFileFromResource(cssSourceDirectory,cssTargetDirectory);
    }

    private void generateImageReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException {
        List<ScreenshotReport> screenshotReportList = ImageReportHelper.getScreenShotReport(testSuiteReport);
        Path directoryPath = FileUtil.createDirectories(outputPath + File.separator + ReportConstant.IMAGE_DIRECTORY_NAME.getValue() + FileExtensionConstant.JPG);
        for (ScreenshotReport screenshotReport :screenshotReportList) {
            String fileName = screenshotReport.getId();
            Path completePath = Paths.get(directoryPath.toString(),fileName);
            FileUtil.saveAsImage(completePath,screenshotReport.getData());
        }
    }
}
