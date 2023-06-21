package fr.axa.automation.webengine.cmd;

import java.io.FileOutputStream;
import java.io.IOException;

public class CommandNameGenerator {

    public static void main(String[] args) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.dir")+"/webengine-drive-by-excel/src/main/resources/command.yaml");
        for (CommandName cmd: CommandName.values()) {
            fileOutputStream.write(cmd.toYamlString().getBytes());
        }
        fileOutputStream.close();
        System.out.println("******** YAML *******************************");
    }
}
