package fr.axa.automation.webengine.report.helper.frmk;


import fr.axa.automation.webengine.dto.InputMarshallDTO;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.report.helper.ReportFileNameHelper;
import fr.axa.automation.webengine.util.FileUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

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
    public String generateWebengineReport(TestSuiteReport testSuiteReport, String outputPath) throws WebEngineException {
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
}
