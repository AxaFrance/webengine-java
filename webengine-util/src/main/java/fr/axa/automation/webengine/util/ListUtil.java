package fr.axa.automation.webengine.util;

import fr.axa.automation.webengine.exception.WebEngineException;

import java.util.List;
import java.util.Optional;

public class ListUtil {

    public static Optional<String> findFirst(List<String> list, String fileName)  {
        Optional<String> findFirstOptional = list.stream().filter(s->s.contains(fileName)).findFirst();
        if(findFirstOptional.isPresent()){
            return findFirstOptional;
        }
        return Optional.empty();
    }
}
