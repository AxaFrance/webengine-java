package fr.axa.automation.webengine.helper;

import lombok.extern.slf4j.Slf4j;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.dom.DomDatabaseWrapper;
import org.linguafranca.pwdb.kdbx.dom.DomEntryWrapper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class KeepassUtils {
    //get password from keepass

    //explain: this method is used to get the password from keepass
    //it is used in the SendKeysCommand class
    public static String getPassword(String key, String keepassPwd, String keepassPaths) {
        String password = "";
        if (key == null || key.isEmpty()) {
            return null;
        }
        if (!key.startsWith("/")) {
            key = "/" + key;
        }
        try {
            //use KeePassJava2 dependency to get the password from keepass
            KdbxCreds creds = new KdbxCreds(keepassPwd.getBytes());
            for (String path : keepassPaths.split(",")) {
                InputStream inputStream = new FileInputStream(path);
                Database database = DomDatabaseWrapper.load(creds, inputStream);
                List entries = database.findEntries(key.substring(key.lastIndexOf("/") + 1));
                final String k = key;
                if (entries.size() > 0) {
                    password = ((DomEntryWrapper)entries.stream().filter(entry -> entry.toString().endsWith(k)).findFirst().get()).getPassword();
                    break;
                }
            }
        } catch (Exception e) {
            log.warn("Verifier le mot de passe et le chemin de votre fichier keepass");
            log.warn(e.getMessage());
        }
        return password;
    }
}
