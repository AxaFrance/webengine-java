package fr.axa.automation.webengine.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SerializationUtils {

    public static <T> T clone(T object, Class<T> clazzType) throws IOException {
        final ObjectMapper objMapper = new ObjectMapper();
        String jsonStr= objMapper.writeValueAsString(object);
        return objMapper.readValue(jsonStr, clazzType);
    }
}
