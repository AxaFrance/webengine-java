package fr.axa.automation.webengine.listener;

import fr.axa.automation.webengine.logger.LoggerService;
import fr.axa.automation.webengine.logger.LoggerServiceDecorator;
import fr.axa.automation.webengine.util.LocalTestingUtil;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestRunStarted;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebengineLocalTestingListener implements EventListener {

    LoggerService loggerService;
    LocalTestingUtil localTestingUtil;

    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        eventPublisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
    }

    private void runStarted(TestRunStarted event) {
        loggerService = LoggerServiceDecorator.getInstance();
        localTestingUtil = LocalTestingUtil.getInstance();
        loggerService.info("Run started");
        try {
            localTestingUtil.startLocalTesting(getApplicationFileName());
        } catch (Exception e) {
            loggerService.error("Error when start local testing",e);
        }
    }

    protected String getApplicationFileName() {
        return LocalTestingUtil.APPLICATION_FILE_NAME;
    }

    private void runFinished(TestRunFinished event) {
        try {
            localTestingUtil.stopLocalTesting();
        } catch (Exception e) {
            loggerService.error("Error when stop local testing",e);
        }
    }
}