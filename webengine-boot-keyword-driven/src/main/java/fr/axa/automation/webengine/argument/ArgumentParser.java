package fr.axa.automation.webengine.argument;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentParser {

    public static CommandLine getOption(String[] args, Options options) {
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
        argumentOptionList.stream().forEach(argumentOption -> options.addOption(getOption(argumentOption)));
        return options;
    }

    public static Option getOption(ArgumentOption argumentOption) {

        return Option.builder()
                        .option(argumentOption.getOption())
                        .hasArg(argumentOption.getHasArg())
                        .required(argumentOption.getRequired())
                        .desc(argumentOption.getDescription()).build();
    }

    public static String[] splitArguments(List<String> args,String regex,int limit) {
        List<String> newArgsList = new ArrayList<>();
        args.stream().forEach(argument -> newArgsList.addAll(Arrays.asList(argument.split(regex, limit))));
        return (String[]) newArgsList.toArray(new String[newArgsList.size()]);
    }
}
