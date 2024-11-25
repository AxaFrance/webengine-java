package fr.axa.automation.webengine.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilTest {

    public static final String SPECIAL_CHARACTERS = "Je suis allé chercher mon chien à l'étrangers-";
    public static final String SPECIAL_CHARACTERS_RESULT = "je_suis_alle_chercher_mon_chien_a_l_etrangers-";

    @Test
    void testRemoveSpecialCharacters() {
        String stringWithoutSpecialCharacter = StringUtil.removeSpecialCharacters(SPECIAL_CHARACTERS);
        Assertions.assertEquals(SPECIAL_CHARACTERS_RESULT,stringWithoutSpecialCharacter);
    }
}