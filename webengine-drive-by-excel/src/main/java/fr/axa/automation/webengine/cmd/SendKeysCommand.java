package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.object.CommandDataDriveByExcel;

public class SendKeysCommand extends AbstractDriverCommand{

    @Override
    public Object execute(CommandDataDriveByExcel commandData) throws Exception {
        webElementDescription = populateWebElement(commandData);
        webElementDescription.sendKeys("");
        return null;
    }
}
