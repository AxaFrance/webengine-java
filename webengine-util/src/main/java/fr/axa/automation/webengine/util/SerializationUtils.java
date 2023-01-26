package fr.axa.automation.webengine.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class SerializationUtils {

    private SerializationUtils() {
    }

    public static <T> T clone(T object) throws IOException {
        final ObjectMapper objMapper = new ObjectMapper();
        String jsonStr= objMapper.writeValueAsString(object);
        return (T) objMapper.readValue(jsonStr, object.getClass());
    }
}
