package fr.axa.automation.webengine.core;

import java.util.AbstractMap;
import java.util.List;

public interface ITestSuite {
   List<AbstractMap.SimpleEntry<String,? extends ITestCase>> getTestCaseList();
}
