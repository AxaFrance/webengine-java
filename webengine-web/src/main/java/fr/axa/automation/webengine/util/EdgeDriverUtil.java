package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.File;

public class EdgeDriverUtil {

    public static final String EDGE_DIRECTORY = "/Edge/";
    public static final String HKEY_LOCAL_MACHINE_SOFTWARE_PATHS_EDGE_EXE_POWERSHELL = "(Get-AppxPackage -Name 'Microsoft.MicrosoftEdge.Stable').Version";
    public static final String URL_EDGE_DRIVER_REMOTE = "https://msedgedriver.azureedge.net/";
    public static final String NAME_EDGE_DRIVER_ZIP = "edgedriver_win64.zip";
    public static final String NAME_EDGE_DRIVER_EXECUTABLE = "msedgedriver.exe";

//    public static WebDriver getEdgeDriver() throws WebEngineException {
//        List<String> edgeInformationList = Command.runPowershellCommand(HKEY_LOCAL_MACHINE_SOFTWARE_PATHS_EDGE_EXE_POWERSHELL);
//        String edgeVersion = edgeInformationList.get(0);
//        File edgeDir = EdgeDriverUtil.createTempDirectory(edgeVersion);
//        ConfigurationSSL.configureSSL();
//        String urlDownloadEdgeDriver = URL_EDGE_DRIVER_REMOTE + edgeVersion + "/" + NAME_EDGE_DRIVER_ZIP;
//        File driverExe = DriverUtil.installDriver(urlDownloadEdgeDriver, edgeDir.getAbsolutePath(), NAME_EDGE_DRIVER_ZIP, NAME_EDGE_DRIVER_EXECUTABLE);
//        System.setProperty(EdgeDriverService.EDGE_DRIVER_EXE_PROPERTY, edgeDir.getAbsolutePath() +  File.separator + NAME_EDGE_DRIVER_EXECUTABLE);
//        WebDriver driver = new EdgeDriver();
//        return driver;
//    }

    public static WebDriver getEdgeDriver() throws WebEngineException {
        WebDriverManager.edgedriver().setup();
        return new EdgeDriver();
    }

    private static File createTempDirectory(String version){
        String installationDirectory = EDGE_DIRECTORY + version;
        return FileUtil.createDirectoryInUserDir(installationDirectory);
    }
}
