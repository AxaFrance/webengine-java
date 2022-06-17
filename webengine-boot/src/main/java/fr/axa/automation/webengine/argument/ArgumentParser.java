package fr.axa.automation.webengine.argument;

import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.Arrays;
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
        Option option = Option.builder()
                        .option(argumentOption.getOption())
                        .hasArg(argumentOption.getHasArg())
                        .valueSeparator(argumentOption.getSeparator())
                        .required(argumentOption.getRequired())
                        .desc(argumentOption.getDescription()).build();

        return option;
    }

    public static String[] splitArguments(String[] args,String regex,int limit) {
        List<String> newArgsList = new ArrayList<>();
        List<String> argsList = Arrays.asList(args);
        for (String argument:argsList){
            newArgsList.addAll(Arrays.asList(argument.split(regex, limit)));
        }
        String[] array = new String[newArgsList.size()];
        return newArgsList.toArray(array);
    }
}
