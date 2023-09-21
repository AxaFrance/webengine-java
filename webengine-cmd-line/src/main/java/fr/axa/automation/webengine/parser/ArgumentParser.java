package fr.axa.automation.webengine.parser;

import fr.axa.automation.webengine.argument.ArgumentOption;
import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class ArgumentParser {

    public static CommandLine getOption(String[] args, Options options) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("Webengine No code syntax", options);
            log.error("Syntax error in the command",e);
            throw e;
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

    public static String[] splitArguments(List<String> args, String regex, int limit) {
        List<String> newArgsList = new ArrayList<>();
        args.stream().forEach(argument -> newArgsList.addAll(Arrays.asList(argument.split(regex, limit))));
        return (String[]) newArgsList.toArray(new String[newArgsList.size()]);
    }

    public static boolean isOptionForProject(List<ArgumentOption> argumentOptionList, String option) {
        return argumentOptionList.stream().anyMatch(argumentOption -> option.startsWith("-" + argumentOption.getOption()));
    }

    public static List<String> removePassWordAfterDecompositionArgs(String[] args) {
        //remove arg after AguementOption.KEEPASS_PASSWORD.getOption()
        AtomicInteger index = new AtomicInteger();
        List<String> argslist = Arrays.asList(args).stream().filter(arg -> index.getAndIncrement() > 0 && !arg.contains(ArgumentOption.KEEPASS_PASSWORD.getOption())).collect(Collectors.toList());
        argslist.remove(index);
        return argslist;
    }

    public static List<String> removeOptionFromArguments(String[] args, ArgumentOption argumentOption) {
        //remove arg after AguementOption.KEEPASS_PASSWORD.getOption()
        return Arrays.asList(args).stream().filter(arg -> !arg.contains(argumentOption.getOption())).collect(Collectors.toList());
    }
}
