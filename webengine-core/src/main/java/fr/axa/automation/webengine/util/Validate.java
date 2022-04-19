package fr.axa.automation.webengine.util;

public class Validate {

    /*
    public List<Variable> getAdditionalData(GlobalApplicationContext globalApplicationContext) throws WebEngineException {
        Set<Class<? extends ITestSuiteDetail>> testSuiteDetailList = globalApplicationContext.getTestSuiteDetailList();
        Map<String,List<Variable>> map = new HashMap<>();
        for (Class clazz:testSuiteDetailList) {//For your information, now we can have only one class of ITestSuiteDetail on this List
            try {
                ITestSuiteDetail instance = ClassUtil.create(clazz, ITestSuiteDetail.class);
                List<ITestCaseDetail> testCaseList = instance.getTestCaseDetailList();
                for (ITestCaseDetail testCaseDetail:testCaseList) {
                    List<ITestStepDetail> testStepList = testCaseDetail.getTestStepDetailList();
                    getAdditionalData()
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                throw new WebEngineException("Error during instantiation of testSuiteDetail",e);
            }
        }
        return null;
    }

    public List<Variable> getAdditionalData(GlobalApplicationContext globalApplicationContext, ITestCaseDetail testCaseDetail, ITestStepDetail testStepDetail) throws WebEngineException {
        List<Variable> additionalDataList = new ArrayList<>();
        List<Variable> requiredParametersList = testStepDetail.getAction().getRequiredParameters();
        if (!CollectionUtils.isEmpty(requiredParametersList)) {
            List<TestSuiteData.TestData> testDataList = globalApplicationContext.getTestSuiteData().getTestData();
            String testCaseName = testCaseDetail.getTestCaseName();
            for (Variable variable : requiredParametersList) {
                TestSuiteData.TestData.Data.Variable variableFound = TestDataUtil.getVariableOfTestCase(testDataList, testCaseName, variable.getName());
                if (variableFound == null && variable.getValue() != null) {
                    additionalDataList.add(variable);
                } else {
                    throw new WebEngineException("Required parameters are missing in XML file data, example : " + variable.getName());
                }
            }
        }
        return additionalDataList;
    }
    */
}
