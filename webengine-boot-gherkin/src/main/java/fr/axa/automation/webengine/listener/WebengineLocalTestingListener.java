package fr.axa.automation.webengine.listener;

import fr.axa.automation.webengine.localtesting.LocalTestingRunner;
import fr.axa.automation.webengine.localtesting.LocalTestingUtil;
import fr.axa.automation.webengine.util.PropertiesUtilV2;
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

    LocalTestingRunner localTestingRunner = LocalTestingRunner.getInstance();

    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestRunStarted.class, this::runStarted);
        eventPublisher.registerHandlerFor(TestRunFinished.class, this::runFinished);
    }

    private void runStarted(TestRunStarted event) {
        localTestingRunner.started(getApplicationFileName());
    }

    protected String getApplicationFileName() {
        return PropertiesUtilV2.APPLICATION_FILE_NAME;
    }

    private void runFinished(TestRunFinished event) {
        localTestingRunner.finished();
    }
}