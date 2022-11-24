package fr.axa.automation.webengine.helper;

import fr.axa.automation.webengine.core.AbstractTestSuite;
import fr.axa.automation.webengine.core.ITestSuite;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.util.ClassUtil;
import fr.axa.automation.webengine.util.CommonClassUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Optional;
import java.util.Set;

public class TestSuiteHelper {

    public static ITestSuite filterTestSuite(Set<Class<? extends ITestSuite>> testSuiteList) throws WebEngineException {
        ITestSuite testSuite = null;
        if(CollectionUtils.isNotEmpty(testSuiteList)){
            Optional<Class<? extends ITestSuite>> clazz = testSuiteList.stream().filter(ts -> !ts.getSimpleName().equalsIgnoreCase(AbstractTestSuite.class.getSimpleName())).findFirst();
            if(clazz.isPresent()) {
                testSuite = CommonClassUtil.create(clazz.get());
            }
        }
        return testSuite;
    }
}
