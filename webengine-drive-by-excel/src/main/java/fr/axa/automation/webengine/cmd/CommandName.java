package fr.axa.automation.webengine.cmd;

import com.google.common.collect.ImmutableMap;
import fr.axa.automation.webengine.util.StringUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum CommandName {

    OPEN(ImmutableMap.of( Locale.ENGLISH, "open" , Locale.FRENCH, "ouvrir" )),
    SEND_KEYS(ImmutableMap.of( Locale.ENGLISH, "send keys" ,Locale.FRENCH, "écrire" )),
    CLICK(ImmutableMap.of( Locale.ENGLISH, "click" , Locale.FRENCH, "cliquer" )),
    SELECT(ImmutableMap.of( Locale.ENGLISH, "select" , Locale.FRENCH, "séléctionner" )),
    CALL(ImmutableMap.of( Locale.ENGLISH, "call" , Locale.FRENCH, "appeler" )),
    IF(ImmutableMap.of( Locale.ENGLISH, "if" , Locale.FRENCH, "si" )),
    ELSE_IF(ImmutableMap.of( Locale.ENGLISH, "else if" , Locale.FRENCH, "sinon si" )),
    ELSE(ImmutableMap.of( Locale.ENGLISH, "else" , Locale.FRENCH, "sinon" )),
    END_IF(ImmutableMap.of( Locale.ENGLISH, "end if" , Locale.FRENCH, "fin si" )),
    SAVE_DATA(ImmutableMap.of( Locale.ENGLISH, "save data" ,Locale.FRENCH, "enregistrer" )),
    ASSERT_EXIST(ImmutableMap.of( Locale.ENGLISH, "assert exist" ,Locale.FRENCH, "existe ?" )),
    ASSERT_NOT_EXIST(ImmutableMap.of( Locale.ENGLISH, "assert not exist" ,Locale.FRENCH, "n'existe pas ?" )),
    ASSERT_CONTENT(ImmutableMap.of( Locale.ENGLISH, "assert content" ,Locale.FRENCH, "contient ?" )),
    ASSERT_NOT_CONTENT(ImmutableMap.of( Locale.ENGLISH, "assert not content" ,Locale.FRENCH, "ne contient pas ?" )),
    ASSERT_SELECTED(ImmutableMap.of( Locale.ENGLISH, "assert selected" , Locale.FRENCH, "séléctionné ?")),
    ASSERT_NOT_SELECTED(ImmutableMap.of( Locale.ENGLISH, "assert not selected" , Locale.FRENCH, "non séléctionné ?")),
    ASSERT_CHECKED(ImmutableMap.of( Locale.ENGLISH, "assert checked",Locale.FRENCH, "coché ?" )),
    ASSERT_NOT_CHECKED(ImmutableMap.of( Locale.ENGLISH, "assert not checked",Locale.FRENCH, "non coché ?" )),
    SCREENSHOT(ImmutableMap.of( Locale.ENGLISH, "screenshot" , Locale.FRENCH, "capture")),
    UPLOADFILE(ImmutableMap.of( Locale.ENGLISH, "upload file" , Locale.FRENCH, "choisir fichier")),
    POPUP(ImmutableMap.of( Locale.ENGLISH, "accept alert ?" , Locale.FRENCH, "accepter alerte ?")),
    WAIT(ImmutableMap.of( Locale.ENGLISH, "wait" , Locale.FRENCH, "pause")),
    END_SCENARIO(ImmutableMap.of(Locale.ENGLISH, "end scenario" , Locale.FRENCH, "fin scenario"));

    final Map<Locale, String> commandLibelleMap;

    public static CommandName fromValue(String value) {
        for (CommandName commandName: CommandName.values()) {
            Optional<String> findValue = commandName.getCommandLibelleMap().values().stream().filter(s -> StringUtil.equalsIgnoreCase(value,s)).findFirst();
            if(findValue.isPresent()){
                return commandName;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
