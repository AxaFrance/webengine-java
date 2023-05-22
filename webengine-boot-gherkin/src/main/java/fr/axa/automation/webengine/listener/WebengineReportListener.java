package fr.axa.automation.webengine.listener;

import fr.axa.automation.webengine.context.ExecutionDetail;
import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.report.IReportGherkinHelper;
import fr.axa.automation.webengine.report.ReportDetail;
import fr.axa.automation.webengine.report.ReportGherkinHelper;
import fr.axa.automation.webengine.status.StatusMapping;
import fr.axa.automation.webengine.util.StringUtil;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.HookTestStep;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.TestCaseEvent;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import io.cucumber.plugin.event.TestSourceRead;
import io.cucumber.plugin.event.TestStep;
import io.cucumber.plugin.event.TestStepFinished;
import io.cucumber.plugin.event.TestStepStarted;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.net.UnknownHostException;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebengineReportListener implements ConcurrentEventListener {

    ILoggerService loggerService;
    IReportGherkinHelper reportGherkinHelper;

    public WebengineReportListener() {
        loggerService = LoggerServiceProvider.getInstance();
    }

    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        eventPublisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
        eventPublisher.registerHandlerFor(TestSourceRead.class, this::featureReadStarted);
        eventPublisher.registerHandlerFor(TestCaseStarted.class, this::scenarioStarted);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, this::scenarioFinished);
        eventPublisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        eventPublisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
    }

    private void runStarted(TestRunStarted event) {
        loggerService.info("Test Run started");
        try {
            reportGherkinHelper = new ReportGherkinHelper();
            reportGherkinHelper.createReport();
        } catch (UnknownHostException e) {
            loggerService.error("[Gherkin report listener] Error during creating report : ", e);
        }
    }

    private void runFinished(TestRunFinished event) {
        loggerService.info("Test Run finished");
        try {
            reportGherkinHelper.closeReport();
        } catch (WebEngineException e) {
            loggerService.error("[Gherkin report listener] Error during closing report : ", e);
        }
    }

    private void featureReadStarted(TestSourceRead testSourceRead) {
        String currentfeatureName = testSourceRead.getUri().toString();
        loggerService.info("Feature read started :"+currentfeatureName);
    }

    private void scenarioStarted(TestCaseStarted testCaseStarted) {
        loggerService.info("Scenario read started : "+testCaseStarted.getTestCase().getName());
        String currentFeatureName = getFeatureNameByTestCase(testCaseStarted);
        String currentScenarioName = testCaseStarted.getTestCase().getName();
        reportGherkinHelper.addTestCaseReport(currentFeatureName,currentScenarioName);
    }

    private void scenarioFinished(TestCaseFinished testCaseFinished) {
        loggerService.info("Scenario read finished : "+testCaseFinished.getTestCase().getName());
        String currentFeatureName = getFeatureNameByTestCase(testCaseFinished);
        String currentScenarioName = testCaseFinished.getTestCase().getName();
        reportGherkinHelper.updateTestCaseReport(currentFeatureName, currentScenarioName,StatusMapping.MAPPING.get(testCaseFinished.getResult().getStatus()));
    }

    private String getTestStepNameByTestStep(TestStep testStep) {
        String stepName = "";
        if (testStep instanceof HookTestStep) {
            stepName = ((HookTestStep) testStep).getHookType().name();
        } else if (testStep instanceof PickleStepTestStep) {
            stepName = ((PickleStepTestStep) testStep).getStep().getText();
        }
        return stepName;
    }

    private String getFeatureNameByTestStep(TestStep testStep) {
        String stepName = "";
        if (testStep instanceof HookTestStep) {
            stepName = testStep.getCodeLocation();
        } else if (testStep instanceof PickleStepTestStep) {
            stepName = ((PickleStepTestStep) testStep).getUri().toString();
        }
        return stepName;
    }

    private String getFeatureNameByTestCase(TestCaseEvent testCase) {
        return testCase.getTestCase().getUri().toString();
    }

    private void stepStarted(TestStepStarted testStepStarted) {
        if(isNotHookStep(testStepStarted)){
            String currentFeatureName = getFeatureNameByTestStep(testStepStarted.getTestStep());
            String currentTestCaseName = testStepStarted.getTestCase().getName();
            String currentStepName = getTestStepNameByTestStep(testStepStarted.getTestStep());
            loggerService.info("Step read started : "+currentStepName);
            reportGherkinHelper.addTestStepReport(currentFeatureName,testStepStarted.getTestCase().getName(), currentStepName);
            ExecutionDetail.STEP_IN_PROGRESS.add(StringUtil.getNormalizeString(new String[]{currentFeatureName,currentTestCaseName,currentStepName}, StringUtil.DOUBLE_TWO_POINTS));
        }
    }

    private void stepFinished(TestStepFinished testStepFinished) {
        if(isNotHookStep(testStepFinished)) {
            String currentStepName = getTestStepNameByTestStep(testStepFinished.getTestStep());
            loggerService.info("Step read finished : " + currentStepName);
            ReportDetail reportDetail = ReportDetail.builder().featureName(getFeatureNameByTestStep(testStepFinished.getTestStep()))
                    .testCaseName(testStepFinished.getTestCase().getName())
                    .stepName(currentStepName)
                    .result(StatusMapping.MAPPING.get(testStepFinished.getResult().getStatus()))
                    .throwable(testStepFinished.getResult().getError()).build();
            reportGherkinHelper.updateTestStepReport(reportDetail);
        }
    }

    private static boolean isHookStep(TestCaseEvent testCaseEvent) {
        if(testCaseEvent instanceof TestStepStarted){
            return (((TestStepStarted)testCaseEvent).getTestStep() instanceof HookTestStep);
        } else if (testCaseEvent instanceof TestStepFinished) {
            return (((TestStepFinished)testCaseEvent).getTestStep() instanceof HookTestStep);
        }
        return false;
    }

    private static boolean isNotHookStep(TestCaseEvent testCaseEvent) {
        return !isHookStep(testCaseEvent);
    }
}