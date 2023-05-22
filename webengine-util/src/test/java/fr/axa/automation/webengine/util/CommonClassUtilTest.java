package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

class CommonClassUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(CommonClassUtilTest .class);

    @Test
    void testCreateWith() throws WebEngineException {
        Map map = CommonClassUtil.create(HashMap.class);
        Assertions.assertNotNull(map);
    }

    @Test
    void testCreate() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        Map map = CommonClassUtil.create(HashMap.class,new Object[]{1},new Class[]{int.class});
        Assertions.assertNotNull(map);
    }

    @Test
    void testCreateAndCallMethod() throws WebEngineException {
        Properties properties = CommonClassUtil.createAndCallMethod(Properties.class,"setProperty","key","value");
    }
}