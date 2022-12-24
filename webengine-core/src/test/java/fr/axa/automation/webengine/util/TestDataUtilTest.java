package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.generated.TestSuiteData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TestDataUtilTest {
    private static final String TEST_CASE_1 = "TEST_CASE_1";
    private static final String TEST_CASE_2 = "TEST_CASE_2";

    @Test
    void testGetDataOfTestCase() throws URISyntaxException, WebEngineException {
        TestSuiteData testSuiteData = XmlUtil.unmarshall(FileUtil.getFileFromResource("data/data.xml").getAbsolutePath(), TestSuiteData.class);
        Optional<TestData> testData = TestDataUtil.getDataOfTestCase(testSuiteData.getTestDatas(),TEST_CASE_1);
        if(testData.isPresent()){
            Assertions.assertEquals(TEST_CASE_1,testData.get().getTestName());
        }
    }

    @Test
    void getVariableOfTestCase() {
    }

    @Test
    void testGetVariableOfTestCase() {
    }
}