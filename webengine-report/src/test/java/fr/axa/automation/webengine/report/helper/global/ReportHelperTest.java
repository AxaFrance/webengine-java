package fr.axa.automation.webengine.report.helper.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestSuiteReport;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.report.constante.ReportPathKey;
import fr.axa.automation.webengine.report.helper.frmk.WebengineHtmlReportHelper;
import fr.axa.automation.webengine.report.helper.frmk.WebengineXmlReportHelper;
import fr.axa.automation.webengine.report.helper.junit.JunitReportHelper;
import fr.axa.automation.webengine.util.FileUtil;
import fr.axa.automation.webengine.util.XmlValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public class ReportHelperTest {

    public static final String REPORT_TEST_RESULT_DIRECTORY = "report-test-result";

    @Test
    void generateAllReport() throws URISyntaxException, IOException, WebEngineException {
        TestSuiteReport testSuiteReport = getTestSuiteReport();
        IReportHelper reportHelper = new ReportHelper(new WebengineXmlReportHelper(new LoggerService()), new WebengineHtmlReportHelper(new LoggerService()), new JunitReportHelper(new LoggerService()),new LoggerService());
        String path = FileUtil.createDirectoryInTarget(REPORT_TEST_RESULT_DIRECTORY);

        Map<ReportPathKey,String> reportMap =  reportHelper.generateReports(testSuiteReport,"TestSuiteName",path);
        boolean resultCompareWebengineReportFile = FileUtil.assertContent(FileUtil.getFileFromResource("report-test-result/webengine-report.xml"), new File(reportMap.get(ReportPathKey.XML_REPORT_PATH_KEY)));
        FileUtil.displayContent(reportMap.get(ReportPathKey.XML_REPORT_PATH_KEY));
        Assertions.assertTrue(resultCompareWebengineReportFile);

        boolean resultValidateJunitReportFile = XmlValidator.validateXMLSchema(FileUtil.getFileFromResource("xsd/junit-report-schema.xsd"),new File(reportMap.get(ReportPathKey.JUNIT_REPORT_PATH_KEY)));
        FileUtil.displayContent(reportMap.get(ReportPathKey.JUNIT_REPORT_PATH_KEY));
        Assertions.assertTrue(resultValidateJunitReportFile);
    }

    private TestSuiteReport getTestSuiteReport() throws URISyntaxException, IOException {
        File file = FileUtil.getFileFromResource("json/test-suite-json.json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new FileReader(file), TestSuiteReport.class);
    }
}