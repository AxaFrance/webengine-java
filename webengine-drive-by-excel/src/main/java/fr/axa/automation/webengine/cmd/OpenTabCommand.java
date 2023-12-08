package fr.axa.automation.webengine.cmd;

import org.openqa.selenium.WindowType;

public class OpenTabCommand extends OpenCommand{

    protected WindowType getWindowType() {
        return WindowType.TAB;
    }
}
