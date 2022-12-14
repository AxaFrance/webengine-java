package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.*;

class CommonClassUtilTest {

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
}