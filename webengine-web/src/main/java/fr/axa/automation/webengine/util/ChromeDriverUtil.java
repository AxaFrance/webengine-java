package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Optional;

public class ChromeDriverUtil {

    public static Optional<WebDriver> getChromeDriver() throws WebEngineException {
        WebDriverManager.chromedriver().setup();
        return Optional.of(new ChromeDriver());
    }

//    public static final String CHROME_DIRECTORY = "/Chrome/";
//    public static final String HKEY_LOCAL_MACHINE_SOFTWARE_PATHS_CHROME_EXE_POWERSHELL = "(Get-Item (Get-ItemProperty 'HKLM:\\Software\\Microsoft\\Windows\\CurrentVersion\\App Paths\\chrome.exe').'(Default)').VersionInfo";
//    public static final String URL_CHROME_DRIVER_REMOTE = "https://chromedriver.storage.googleapis.com/";
//    public static final String URL_CHROME_DRIVER_REMOTE_VERSION = URL_CHROME_DRIVER_REMOTE+"LATEST_RELEASE_";
//    public static final String NAME_CHROME_DRIVER_ZIP = "chromedriver_win32.zip";
//    public static final String NAME_CHROME_DRIVER_EXECUTABLE = "chromedriver.exe";

//    public static WebDriver getChromeDriver() throws WebEngineException {
//        List<String> chromeInformationList = Command.runPowershellCommand(HKEY_LOCAL_MACHINE_SOFTWARE_PATHS_CHROME_EXE_POWERSHELL);
//        String chromeVersionLocal = ChromeDriverUtil.getFirstPartOfChromeVersion(chromeInformationList);
//        String chromeVersionRemote = ChromeDriverUtil.getChromeVersionInRemote(URL_CHROME_DRIVER_REMOTE_VERSION +chromeVersionLocal);
//        File chromeDir = ChromeDriverUtil.createTempDirectory(chromeVersionRemote);
//        ConfigurationSSL.configureSSL();
//        String chromeUrlDownloadDriver = URL_CHROME_DRIVER_REMOTE + chromeVersionRemote+ "/" + NAME_CHROME_DRIVER_ZIP;
//        DriverUtil.installDriver(chromeUrlDownloadDriver, chromeDir.getAbsolutePath(), NAME_CHROME_DRIVER_ZIP, NAME_CHROME_DRIVER_EXECUTABLE);
//        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, chromeDir.getAbsolutePath() + File.separator + NAME_CHROME_DRIVER_EXECUTABLE);
//        return new ChromeDriver();
//    }

//    private static String getFirstPartOfChromeVersion(List<String> chromeInformationList) {
//        String infoFullVersion = getFullChromeVersion(chromeInformationList);
//        String fisrtPartVersion = infoFullVersion.split("\\s+")[0];
//        return fisrtPartVersion.substring(0, fisrtPartVersion.lastIndexOf("."));
//    }
//
//    private static String getFullChromeVersion(List<String> chromeInformationList) {
//        String chromeVersion="";
//        for (String chromeInformation: chromeInformationList) {
//            if(chromeInformation.contains(".exe")){
//                List<String> versionList = Collections.singletonList(chromeInformation);
//                chromeVersion = versionList.get(0);
//            }
//        }
//        return chromeVersion;
//    }
//
//    private static String getChromeVersionInRemote(String urlVersionInRemote) throws WebEngineException{
//        //Example of version https://chromedriver.storage.googleapis.com/LATEST_RELEASE_101.0.4951
//        String chromeVersionInRemote = null;
//        try {
//            URL url = new URL(urlVersionInRemote);
//            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//            chromeVersionInRemote = in.readLine();
//        } catch (IOException e) {
//            throw new WebEngineException("Error during get chrome version in remote with url : "+urlVersionInRemote,e);
//        }
//
//        return chromeVersionInRemote; //you get the IP as a String
//    }
//
//    private static File createTempDirectory(String version){
//        String installationDirectory = CHROME_DIRECTORY + version;
//        return FileUtil.createDirectoryInUserDir(installationDirectory);
//    }




}
