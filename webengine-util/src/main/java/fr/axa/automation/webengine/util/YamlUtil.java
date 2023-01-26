package fr.axa.automation.webengine.util;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;

public final class YamlUtil {

    private YamlUtil() {
    }

    public static <T> Yaml getYaml(Class<T> clazz) {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);
        return new Yaml(new Constructor(clazz),representer);
    }

    public static  <T> T loadYaml(Class<T> clazz, InputStream inputStream) {
        Yaml yaml = getYaml(clazz);
        return yaml.load(inputStream);
    }
}
