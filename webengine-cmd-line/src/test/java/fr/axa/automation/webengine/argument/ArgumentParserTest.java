package fr.axa.automation.webengine.argument;

import fr.axa.automation.webengine.constante.IConstant;
import fr.axa.automation.webengine.parser.ArgumentParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class ArgumentParserTest {

    private static final List<ArgumentOption> ARGUMENT_OPTION_PROJECT = Arrays.asList(ArgumentOption.TEST_DATA, ArgumentOption.ENVIRONMENT_VARIABLE, ArgumentOption.PROPERTIES_FILE_LIST);

    @Test
    void getOption() throws ParseException {
        String []  argumentsListSeparatedByOptionAndValue = new String[]{"-data","data.xml","-env","env.xml"};
        CommandLine commandLine = ArgumentParser.getOption(argumentsListSeparatedByOptionAndValue, ArgumentParser.getOptionList(ARGUMENT_OPTION_PROJECT));
        Assertions.assertEquals("data.xml",commandLine.getOptionValue(ArgumentOption.TEST_DATA.getOption()));
    }

    @Test
    void getOptionList() {
        Options options = ArgumentParser.getOptionList(ARGUMENT_OPTION_PROJECT);
        Collection<Option> optionCollection = options.getOptions();
        Assertions.assertEquals(3,optionCollection.size());
    }

    @Test
    void testGetOption() {
        Option option = ArgumentParser.getOption(ArgumentOption.PROJECT);
        Assertions.assertEquals("a",option.getOpt());
        Assertions.assertTrue(option.hasArg());
        Assertions.assertTrue(option.isRequired());
        Assertions.assertEquals("Project to run",option.getDescription());
    }

    @Test
    void splitArguments() {
        List<String>  argumentsList = Arrays.asList("-data:data.xml","-env:env.xml");
        String[] argumentsListSeparatedByOptionAndValue = ArgumentParser.splitArguments(argumentsList, IConstant.SEPARATOR_ARG, 2);
        Assertions.assertEquals("-data",argumentsListSeparatedByOptionAndValue[0]);
        Assertions.assertEquals("data.xml",argumentsListSeparatedByOptionAndValue[1]);
        Assertions.assertEquals("-env",argumentsListSeparatedByOptionAndValue[2]);
        Assertions.assertEquals("env.xml",argumentsListSeparatedByOptionAndValue[3]);
    }
}