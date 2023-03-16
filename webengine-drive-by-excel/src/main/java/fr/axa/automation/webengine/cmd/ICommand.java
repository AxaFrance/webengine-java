package fr.axa.automation.webengine.cmd;

import fr.axa.automation.webengine.object.CommandData;

public interface ICommand {
    Object execute(CommandData commandData);
}
