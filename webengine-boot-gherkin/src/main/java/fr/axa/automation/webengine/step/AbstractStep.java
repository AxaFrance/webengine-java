package fr.axa.automation.webengine.step;

import fr.axa.automation.webengine.helper.ScreenshotHelper;
import fr.axa.automation.webengine.logger.Logger;

public abstract class AbstractStep {

    protected void info(String info){
        Logger.info(info);
    }
    protected void warn(String warn){
        Logger.warn(warn);
    }
    protected void error(String error){
        Logger.error(error);
    }
    protected void fatal(String fatal){
        Logger.fatal(fatal);
    }

    protected void screenshot(){
        ScreenshotHelper.screenshot();
    }
}