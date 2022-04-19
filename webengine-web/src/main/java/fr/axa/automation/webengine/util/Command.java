package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class Command {

    public static List<String> runPowershellCommand (String command) throws WebEngineException {
        //Example powershell.exe  (Get-Item (Get-ItemProperty 'HKLM:\\Software\\Microsoft\\Windows\\CurrentVersion\\App Paths\\chrome.exe').'(Default)').VersionInfo
        List<String> list;
        BufferedReader bufferedReader = null;
        try{
            Process powerShellProcess = Runtime.getRuntime().exec("powershell.exe "+command);
            powerShellProcess.getOutputStream().close();// Getting the results
            bufferedReader = new BufferedReader(new InputStreamReader(powerShellProcess.getInputStream()));
            list = bufferedReader.lines().collect(Collectors.toList());
        }catch(Exception e){
            throw new WebEngineException("Error during powershell command"+command);
        }finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new WebEngineException("Error during powershell command"+command);
                }
            }
        }
        return list;
    }

}
