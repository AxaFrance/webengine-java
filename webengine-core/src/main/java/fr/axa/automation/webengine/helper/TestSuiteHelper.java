package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.core.ITestSuite;
import fr.axa.automation.webengine.util.ClassUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Optional;
import java.util.Set;

public class TestSuiteHelper {

    public static ITestSuite getTestSuite(Set<Class<? extends ITestSuite>> testSuiteList) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ITestSuite testSuite = null;
        if(CollectionUtils.isNotEmpty(testSuiteList)){
            Optional<Class<? extends ITestSuite>> clazz = testSuiteList.stream().findFirst();
            if(clazz.isPresent()) {
                testSuite = (ITestSuite) ClassUtil.create(clazz.get());
            }
        }
        return testSuite;
    }
}
