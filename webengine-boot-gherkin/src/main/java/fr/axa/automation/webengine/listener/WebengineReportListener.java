package fr.axa.automation.webengine.listener;

import fr.axa.automation.webengine.exception.WebEngineException;
import fr.axa.automation.webengine.generated.Result;
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
        try {
            reportHelperGherkin.createReport();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * This event is triggered when feature file is read
     */
    private void runFinished(TestRunFinished event) {
        System.out.println("Run finished");
        try {
            reportHelperGherkin.closeReport();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WebEngineException e) {
            e.printStackTrace();
        }
    }

    /**
     * TestRunFinished event is triggered when all feature file executions are completed
     */
    private void featureRead(TestSourceRead testSourceRead) {
        System.out.println("feature read");
    }


    private void scenarioStarted(TestCaseStarted testCaseStarted) {
        System.out.println("scenario read");
        reportHelperGherkin.addTestCaseReport(testCaseStarted.getTestCase().getName());
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
        String stepName = getTestStepName(testStepStarted.getTestStep());
        reportHelperGherkin.addTestStepReport(testStepStarted.getTestCase().getName(), stepName);
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