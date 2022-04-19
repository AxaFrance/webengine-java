package fr.axa.automation.webengine.argument;

import org.apache.commons.cli.*;

import java.util.List;

public class ArgumentParser {

    public static CommandLine getOption(String[] args,Options options) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }
        return cmd;
    }

    public static Options getOptionList(List<ArgumentOption> argumentOptionList) {
        Options options = new Options();
        for (ArgumentOption argumentOption :argumentOptionList) {
            options.addOption(getOption(argumentOption));
        }
        return options;
    }

    public static Option getOption(ArgumentOption argumentOption) {
        Option option = new Option(argumentOption.getOption(), argumentOption.getHasArg(), argumentOption.getDescription());
        option.setRequired(argumentOption.getRequired());
        return option;
    }
}
