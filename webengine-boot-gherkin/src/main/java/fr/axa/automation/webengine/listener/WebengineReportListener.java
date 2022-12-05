package fr.axa.automation.webengine.listener;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.logger.ILoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceProvider;
import fr.axa.automation.webengine.report.IReportGherkinHelper;
import fr.axa.automation.webengine.report.ReportDetail;
import fr.axa.automation.webengine.report.ReportGherkinHelper;
import fr.axa.automation.webengine.status.StatusMapping;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.net.UnknownHostException;
import java.util.StringJoiner;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebengineReportListener implements EventListener {

    ILoggerService loggerService;
    IReportGherkinHelper reportGherkinHelper;

    public WebengineReportListener() {
        loggerService = LoggerServiceProvider.getInstance();
        reportGherkinHelper = ReportGherkinHelper.getInstance();
    }

    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        eventPublisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
        eventPublisher.registerHandlerFor(TestSourceRead.class, this::featureRead);
        eventPublisher.registerHandlerFor(TestCaseStarted.class, this::scenarioStarted);
        eventPublisher.registerHandlerFor(TestCaseFinished.class, this::scenarioFinished);
        eventPublisher.registerHandlerFor(TestStepStarted.class, this::stepStarted);
        eventPublisher.registerHandlerFor(TestStepFinished.class, this::stepFinished);
    }

    private void runStarted(TestRunStarted event) {
        loggerService.info("Run started");
        try {
            reportGherkinHelper.createReport();
        } catch (UnknownHostException e) {
            loggerService.error("Error during creating report : ", e);
        }
    }

    private void runFinished(TestRunFinished event) {
        loggerService.info("Run finished");
        try {
            reportGherkinHelper.closeReport();
        } catch (WebEngineException e) {
            loggerService.error("Error during closing report : ", e);
        }
    }

    private void featureRead(TestSourceRead testSourceRead) {
        String currentfeatureName = testSourceRead.getSource();
        reportGherkinHelper.setCurrentFeatureName(currentfeatureName);
        loggerService.info("feature : "+currentfeatureName+" read");
    }

    private void scenarioStarted(TestCaseStarted testCaseStarted) {
        loggerService.info("scenario read : "+testCaseStarted.getTestCase().getName());
        String currentScenarioName = testCaseStarted.getTestCase().getName();
        reportGherkinHelper.setCurrentScenarioName(currentScenarioName);
        reportGherkinHelper.addTestCaseReport(currentScenarioName);
    }

    private void scenarioFinished(TestCaseFinished testCaseFinished) {
        loggerService.info("scenario finished : "+testCaseFinished.getTestCase().getName());
        reportGherkinHelper.updateTestCaseReport(testCaseFinished.getTestCase().getName(),StatusMapping.MAPPING.get(testCaseFinished.getResult().getStatus()));
    }

    private String getTestStepName(TestStep testStep) {
        String stepName = "";
        if (testStep instanceof HookTestStep) {
            stepName = ((HookTestStep) testStep).getHookType().name();
        } else if (testStep instanceof PickleStepTestStep) {
            stepName = ((PickleStepTestStep) testStep).getStep().getText();
        }
        return stepName;
    }

    private void stepStarted(TestStepStarted testStepStarted) {
        String currentStepName = getTestStepName(testStepStarted.getTestStep());
        loggerService.info("step read : "+currentStepName);
        reportGherkinHelper.setCurrentStepName(currentStepName);
        reportGherkinHelper.setInformation(new StringJoiner("\n"));
        reportGherkinHelper.addTestStepReport(testStepStarted.getTestCase().getName(), currentStepName);
    }

    private void stepFinished(TestStepFinished testStepFinished) {
        String stepName = getTestStepName(testStepFinished.getTestStep());
        ReportDetail reportDetail = ReportDetail.builder().testCaseName(testStepFinished.getTestCase().getName())
                                                          .stepName(stepName)
                                                          .result(StatusMapping.MAPPING.get(testStepFinished.getResult().getStatus()))
                                                          .throwable(testStepFinished.getResult().getError()).build();
        reportGherkinHelper.updateTestStepReport(reportDetail);
    }
}