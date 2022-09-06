package fr.axa.automation.webengine.listener;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceDecorator;
import fr.axa.automation.webengine.report.ReportHelperGherkin;
import fr.axa.automation.webengine.status.StatusMapping;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.net.UnknownHostException;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebengineReportListener implements EventListener {

    LoggerService loggerService;
    ReportHelperGherkin reportHelperGherkin;

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
        System.out.println("Run started");
        reportHelperGherkin = ReportHelperGherkin.getInstance();
        loggerService = LoggerServiceDecorator.getInstance();
        try {
            reportHelperGherkin.createReport();
        } catch (UnknownHostException e) {
            loggerService.error("Erreur lors de la création du rapport : ", e);
        }
    }

    private void runFinished(TestRunFinished event) {
        System.out.println("Run finished");
        try {
            reportHelperGherkin.closeReport();
        } catch (WebEngineException e) {
            loggerService.error("Erreur lors de la fermeture du rapport : ", e);
        }
    }

    private void featureRead(TestSourceRead testSourceRead) {
        String currentfeatureName = testSourceRead.getSource();
        reportHelperGherkin.setCurrentfeatureName(currentfeatureName);
        System.out.println("feature : "+currentfeatureName+" read");
    }

    private void scenarioStarted(TestCaseStarted testCaseStarted) {
        System.out.println("scenario read");
        String currentScenarioName = testCaseStarted.getTestCase().getName();
        reportHelperGherkin.setCurrentScenarioName(currentScenarioName);
        reportHelperGherkin.addTestCaseReport(currentScenarioName);
    }

    private void scenarioFinished(TestCaseFinished testCaseFinished) {
        System.out.println("scenario finished");
        reportHelperGherkin.updateTestCaseReport(testCaseFinished.getTestCase().getName(),StatusMapping.MAPPING.get(testCaseFinished.getResult().getStatus()));
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

    /**
     * Step started event
     * @param testStepStarted
     */
    private void stepStarted(TestStepStarted testStepStarted) {
        System.out.println("step read");
        String currentStepName = getTestStepName(testStepStarted.getTestStep());
        reportHelperGherkin.setCurrentStepName(currentStepName);
        reportHelperGherkin.getInformation().setLength(0);
        reportHelperGherkin.addTestStepReport(testStepStarted.getTestCase().getName(), currentStepName);
    }

    /**
     *
     * @param testStepFinished
     */
    private void stepFinished(TestStepFinished testStepFinished) {
        String stepName = getTestStepName(testStepFinished.getTestStep());
        reportHelperGherkin.updateTestStepReport(testStepFinished.getTestCase().getName(), stepName, StatusMapping.MAPPING.get(testStepFinished.getResult().getStatus()));
    }
}