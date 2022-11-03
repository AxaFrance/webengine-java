package fr.axa.automation.webengine.listener;

import fr.axa.automation.webengine.localtesting.LocalListenerRunner;
import fr.axa.automation.webengine.localtesting.LocalTestingUtil;
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

    LocalListenerRunner localListenerRunner = LocalListenerRunner.getInstance();

    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        eventPublisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
    }

    private void runStarted(TestRunStarted event) {
        localListenerRunner.started();
    }

    protected String getApplicationFileName() {
        return LocalTestingUtil.APPLICATION_FILE_NAME;
    }

    private void runFinished(TestRunFinished event) {
        localListenerRunner.finished();
    }
}