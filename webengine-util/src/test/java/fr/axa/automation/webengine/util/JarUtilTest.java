package fr.axa.automation.webengine.util;

import org.apache.commons.collections4.properties.AbstractPropertiesFactory;
import org.apache.commons.collections4.properties.PropertiesFactory;
import org.apache.commons.collections4.properties.SortedPropertiesFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JarUtilTest {

    @Test
    void loadLibrary() {
        assertDoesNotThrow(() -> JarUtil.loadLibrary(FileUtil.getFileFromResource("jar/commons-collections4-4.4.jar")));
    }

    @Test
    void findAllClass(){
        loadLibrary();
        List<Class> propertiesList = Arrays.asList(PropertiesFactory.class, SortedPropertiesFactory.class);
        Set<Class<? extends AbstractPropertiesFactory>> propertiesFactoryList = CommonClassUtil.findAllClass(AbstractPropertiesFactory.class);
        List<Class<? extends AbstractPropertiesFactory>> propertiesFactoryFilterList = propertiesFactoryList.stream().filter(clazz -> propertiesList.contains(clazz)).collect(Collectors.toList());
        Assertions.assertEquals(2,propertiesFactoryFilterList.size());
    }
}