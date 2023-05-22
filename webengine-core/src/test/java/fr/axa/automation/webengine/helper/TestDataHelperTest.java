package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.TestData;
import fr.axa.automation.webengine.generated.TestSuiteData;
import fr.axa.automation.webengine.generated.Variable;
import fr.axa.automation.webengine.util.FileUtil;
import fr.axa.automation.webengine.util.XmlUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.Optional;

class TestDataHelperTest {
    private static final String TEST_CASE_1 = "TEST_CASE_1";
    private static final String TEST_CASE_2 = "TEST_CASE_2";

    private static final String TEST_CASE_2_VARIABLE = "TEST_CASE_2_VARIABLE";
    private static final String TEST_CASE_2_VARIABLE_VALUE = "Test case 2 variable value";

    @Test
    void testGetDataOfTestCase() throws URISyntaxException, WebEngineException {
        Optional<TestData> testData = getDataOfTestCase(TEST_CASE_1);
        testData.ifPresent(data -> Assertions.assertEquals(TEST_CASE_1, data.getTestName()));
    }

    private Optional<TestData> getDataOfTestCase(String testCaseName) throws WebEngineException, URISyntaxException {
        TestSuiteData testSuiteData = XmlUtil.unmarshall(FileUtil.getFileFromResource("input/data.xml").getAbsolutePath(), TestSuiteData.class);
        return TestDataHelper.getDataOfTestCase(testSuiteData.getTestDatas(),testCaseName);
    }

    @Test
    void getVariableOfTestCase() throws URISyntaxException, WebEngineException {
        TestSuiteData testSuiteData = XmlUtil.unmarshall(FileUtil.getFileFromResource("input/data.xml").getAbsolutePath(), TestSuiteData.class);
        Variable variable = TestDataHelper.getVariableOfTestCase(testSuiteData.getTestDatas(),TEST_CASE_2,TEST_CASE_2_VARIABLE);
        if(variable!=null){
            Assertions.assertEquals(TEST_CASE_2_VARIABLE_VALUE,variable.getValue());
        }
    }

    @Test
    void testGetVariableOfTestCase() throws URISyntaxException, WebEngineException{
        Optional<TestData> testData = getDataOfTestCase(TEST_CASE_1);
        if(testData.isPresent()){
            Optional<Variable> variable = TestDataHelper.getVariableOfTestCase(testData.get(),TEST_CASE_2_VARIABLE);
            variable.ifPresent(value -> Assertions.assertEquals(TEST_CASE_2_VARIABLE_VALUE, value.getValue()));
        }
    }
}