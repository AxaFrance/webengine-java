package fr.axa.automation.webengine.checking.chain.impl;

import fr.axa.automation.webengine.global.AbstractGlobalApplicationContext;
import fr.axa.automation.webengine.global.SettingsNoCode;
import fr.axa.automation.webengine.object.TestSuiteDataNoCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class TestCaseArgChecking extends AbstractChecking {
    @Override
    public boolean check(AbstractGlobalApplicationContext globalApplicationContext, TestSuiteDataNoCode testSuiteData) {
        SettingsNoCode settingsNoCode = (SettingsNoCode) globalApplicationContext.getSettings();
        Set<String> testCaseParamList = settingsNoCode.getTestCaseAndDataTestColumName().keySet();
        Set<String> testCaseList = testSuiteData.getTestCaseList().stream().map(testCaseDataNoCode -> testCaseDataNoCode.getName()).collect(Collectors.toSet());
        Collection<String> intersectionSet = CollectionUtils.intersection(testCaseParamList,testCaseList);
        if(testCaseParamList.size() == intersectionSet.size()){
            return true;
        }else{
         testCaseParamList.stream().forEach(s -> {
             if(!testCaseList.contains(s)){
                 loggerService.error("This test case argument doesn't exist in the file"+ s );
             }
            });
            return false;
         }
    }


}