package fr.axa.automation.webengine.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class WindowsRegistry {

    public static String readRegistry(String location){
        try {
            // Run reg query, then read output with StreamReader (internal class)
            Process process = Runtime.getRuntime().exec("reg query \"" + location + "\"");

            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String output = reader.getResult();

            // Output has the following format:
            // \n<Version information>\n\n<key>\t<registry type>\t<value>
            if( ! output.contains("\t")){
                return null;
            }
            // Parse out the value
            String[] parsed = output.split("\t");
            return parsed[parsed.length-1];
        }
        catch (Exception e) {
            return null;
        }
    }

    static class StreamReader extends Thread {
        private final InputStream is;
        private final StringWriter sw= new StringWriter();

        public StreamReader(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            }
            catch (IOException e) {
            }
        }

        public String getResult() {
            return sw.toString();
        }
    }

    public static String getExecutablePath(String version) {
        String filePath = version.substring(version.indexOf("REG_SZ")+6, version.indexOf("Path "));
        filePath = filePath.substring(0,filePath.indexOf(".exe")+4);
        return filePath.trim();
    }

//    public static void main(String[] args) {
//
//        // Sample usage
//        String value = WindowsReqistry.readRegistry("HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\"
//                + "Explorer\\Shell Folders", "Personal");
//        System.out.println(value);
//    }
}
