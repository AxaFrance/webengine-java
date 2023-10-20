package fr.axa.automation.webengine.cmd;

import com.google.common.collect.ImmutableMap;
import fr.axa.automation.webengine.util.StringUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum CommandName {

    OPEN(ImmutableMap.of( Locale.ENGLISH, "open" , Locale.FRENCH, "ouvrir" ),
            ImmutableMap.of( Locale.ENGLISH, "Open browser with the given url" ,
                    Locale.FRENCH, "Ouverture du navigateur avec l'url indiqué" )),
    OPEN_PRIVATE(ImmutableMap.of( Locale.ENGLISH, "open private" , Locale.FRENCH, "ouvrir en mode privé" ),
            ImmutableMap.of( Locale.ENGLISH, "Open browser in private mode with the given url" ,
                    Locale.FRENCH, "Ouverture du navigateur en mode privé avec l'url indiqué" )),
    CLEAR(ImmutableMap.of( Locale.ENGLISH, "Clear" ,Locale.FRENCH, "Effacer" ),
            ImmutableMap.of( Locale.ENGLISH, "Clear the given text field" ,
                    Locale.FRENCH, "Effacer le champ texte défini dans la colonne identification" )),

    SEND_KEYS(ImmutableMap.of( Locale.ENGLISH, "send keys" ,Locale.FRENCH, "écrire" ),
            ImmutableMap.of( Locale.ENGLISH, "Send keys to the given text field" ,
                    Locale.FRENCH, "Ecriture dans le champ texte défini dans la colonne identification" )),

    SEND_KEYS_WITH_CLEAR(ImmutableMap.of( Locale.ENGLISH, "send keys with clear" ,Locale.FRENCH, "écrire et effacer avant le champ" ),
            ImmutableMap.of( Locale.ENGLISH, "Send keys to the given text field" ,
                    Locale.FRENCH, "Ecriture dans le champ texte défini dans la colonne identification" )),

    CLICK(ImmutableMap.of( Locale.ENGLISH, "click" , Locale.FRENCH, "cliquer" ),
            ImmutableMap.of( Locale.ENGLISH, "Click on the given element" ,
                    Locale.FRENCH, "Clic sur l'élément indiqué" )),
    SELECT(ImmutableMap.of( Locale.ENGLISH, "select" , Locale.FRENCH, "séléctionner" ),
            ImmutableMap.of( Locale.ENGLISH, "Select the given value in the given drop down list" , Locale.FRENCH,
                    "Sélectionne la valeur indiquée dans la liste déroulante définie dans la colonne identification" )),
    CALL(ImmutableMap.of( Locale.ENGLISH, "call" , Locale.FRENCH, "appeler" ),
            ImmutableMap.of( Locale.FRENCH, "Execute le sous-scénario définie dans la colonne Identification" ,
                    Locale.ENGLISH, "Execute the sub-scenario defined in the Identification column" )),
    IF(ImmutableMap.of( Locale.ENGLISH, "if" , Locale.FRENCH, "si" ),
            ImmutableMap.of( Locale.FRENCH, "Si la condition définie dans la colonne des valeurs <<<exists>>>,<<<empty>>>,etc; les lignes en dessous sont exécutées" ,
                    Locale.ENGLISH, "If the condition defined in the values column <<<exists>>>,<<<empty>>>,etc; the lines below are executed" )),
    ELSE_IF(ImmutableMap.of( Locale.ENGLISH, "else if" , Locale.FRENCH, "sinon si" ),
            ImmutableMap.of( Locale.FRENCH, "Si la condition définie dans la colonne des valeurs <<<exists>>>,<<<empty>>>,etc; les lignes en dessous sont exécutées" ,
                    Locale.ENGLISH, "If the condition defined in the values column <<<exists>>>,<<<empty>>>,etc; the lines below are executed" )),
    ELSE(ImmutableMap.of( Locale.ENGLISH, "else" , Locale.FRENCH, "sinon" ),
            ImmutableMap.of( Locale.FRENCH, "Si la condition définie dans le IF au dessus n'est pas respecté, les lignes en dessous sont exécutées" ,
                    Locale.ENGLISH, "If the condition defined in the IF above is not respected, the lines below are executed" )),
    END_IF(ImmutableMap.of( Locale.ENGLISH, "end if" , Locale.FRENCH, "fin si" )
            ,ImmutableMap.of( Locale.FRENCH, "Fin d'un bloc IF" ,
            Locale.ENGLISH, "End of an IF block" )),
    SAVE_DATA(ImmutableMap.of( Locale.ENGLISH, "save data" ,Locale.FRENCH, "enregistrer" ),
            ImmutableMap.of( Locale.FRENCH, "Enregistre la valeur définie ou le contenu de l'élément indiqu\" si la colonne valeur est vide" ,
                    Locale.ENGLISH, "Save the defined value or the content of the element indicated if the value column is empty" )),
    ASSERT_EXIST(ImmutableMap.of( Locale.ENGLISH, "assert exist" ,Locale.FRENCH, "existe ?" ),
            ImmutableMap.of( Locale.FRENCH, "Vérifie que l'élement défini existe dans la page web" ,
                    Locale.ENGLISH, "Check that the defined element exists in the web page" )),
    ASSERT_NOT_EXIST(ImmutableMap.of( Locale.ENGLISH, "assert not exist" ,Locale.FRENCH, "n'existe pas ?" ),
            ImmutableMap.of( Locale.FRENCH, "Vérifie la valeur indiqué est contenu dans l'élement défini" ,
                    Locale.ENGLISH, "Check that the defined element does not exist in the web page" )),
    ASSERT_CONTENT(ImmutableMap.of( Locale.ENGLISH, "assert content" ,Locale.FRENCH, "contient ?" ),
            ImmutableMap.of( Locale.FRENCH, "Vérifie la valeur indiqué est contenu dans l'élement défini" ,
                    Locale.ENGLISH, "Check that the defined element contains the value indicated" )),
    ASSERT_NOT_CONTENT(ImmutableMap.of( Locale.ENGLISH, "assert not content" ,Locale.FRENCH, "ne contient pas ?" ),
            ImmutableMap.of( Locale.FRENCH, "Vérifie la valeur indiqué est contenu dans l'élement défini" ,
                    Locale.ENGLISH, "Check that the defined element does not contain the value indicated" )),
    ASSERT_SELECTED(ImmutableMap.of( Locale.ENGLISH, "assert selected" , Locale.FRENCH, "séléctionné ?"),
            ImmutableMap.of( Locale.FRENCH, "Vérifie la valeur indiqué est sélectionnée dans l'élement défini" ,
                    Locale.ENGLISH, "Check that the defined element is selected" )),
    ASSERT_NOT_SELECTED(ImmutableMap.of( Locale.ENGLISH, "assert not selected" , Locale.FRENCH, "non séléctionné ?"),
            ImmutableMap.of( Locale.FRENCH, "Vérifie que la valeur indiqué n'est pas sélectionnée dans l'élement" ,
                    Locale.ENGLISH, "Check that the defined element is not selected")),
    ASSERT_CHECKED(ImmutableMap.of( Locale.ENGLISH, "assert checked",Locale.FRENCH, "coché ?" ),
            ImmutableMap.of( Locale.FRENCH, "Vérifie que l'élement défini est bien coché" ,
                    Locale.ENGLISH, "Check that the defined element is checked" )),
    ASSERT_NOT_CHECKED(ImmutableMap.of( Locale.ENGLISH, "assert not checked",Locale.FRENCH, "non coché ?" ),
            ImmutableMap.of( Locale.FRENCH, "Vérifie que l'élement défini n'est pas coché" ,
                    Locale.ENGLISH, "Check that the defined element is not checked" )),
    SCREENSHOT(ImmutableMap.of( Locale.ENGLISH, "screenshot" , Locale.FRENCH, "capture"),
            ImmutableMap.of( Locale.FRENCH, "Effectue une capture d'ecran" ,
                    Locale.ENGLISH, "Take a screenshot" )),
    UPLOADFILE(ImmutableMap.of( Locale.ENGLISH, "upload file" , Locale.FRENCH, "choisir fichier"),
            ImmutableMap.of( Locale.FRENCH, "Permet d'uploader un fichier présent dans le répertoire nommé Upload lui-même présent au même niveau que le fichier excel" ,
                    Locale.ENGLISH, "Upload a file present in the Upload directory present at the same level as the excel file" )),
    POPUP(ImmutableMap.of( Locale.ENGLISH, "accept alert ?" , Locale.FRENCH, "accepter alerte ?"),
            ImmutableMap.of( Locale.FRENCH, "permet de gerer les alertes javascript" ,
                    Locale.ENGLISH, "Manage javascript alerts" )),
    WAIT(ImmutableMap.of( Locale.ENGLISH, "wait" , Locale.FRENCH, "pause"),
            ImmutableMap.of( Locale.FRENCH, "Effectue une pause du temps indiqué en seconde dans la colonne des valeurs" ,
                    Locale.ENGLISH, "Pause for the time indicated in seconds in the value column" )),
    SWITCH_FRAME(ImmutableMap.of( Locale.ENGLISH, "switch frame" , Locale.FRENCH, "changer frame"),
            ImmutableMap.of( Locale.FRENCH, "Permet de basculer vers une iframe à l'intérieur de la page. Cette iframe doit être définie dans la colonne Target en ID ou XPATH" ,
                    Locale.ENGLISH, "Switch to an iframe inside the page. This iframe must be defined in the Target column in ID or XPATH" )),
    EXIT_FRAME(ImmutableMap.of( Locale.ENGLISH, "exit frame" , Locale.FRENCH, "sortir frame"),
            ImmutableMap.of( Locale.FRENCH, "Permet de sortir de l'iframe courant" ,
                    Locale.ENGLISH, "Exit the current iframe" )),
    END_SCENARIO(ImmutableMap.of(Locale.ENGLISH, "end scenario" , Locale.FRENCH, "fin scenario"),
            ImmutableMap.of(Locale.FRENCH, "Indique la fin du scénario courant" ,
                    Locale.ENGLISH, "Indicates the end of the current scenario")),
    REFRESH(ImmutableMap.of(Locale.ENGLISH, "refresh" , Locale.FRENCH, "rafraichir"),
            ImmutableMap.of(Locale.FRENCH, "Permet de rafraichir la page courante" ,
                    Locale.ENGLISH, "Refresh the current page"));

    final Map<Locale, String> commandLibelleMap;
    final Map<Locale, String> commandDescriptionMap;

    @Override
    public String toString() {
        return "CommandName : { " +
                "Name : " + this.name()  + "; " +
                "Description : { " +
                "English : " + this.commandDescriptionMap.get(Locale.ENGLISH) + "; " +
                "French : " + this.commandDescriptionMap.get(Locale.FRENCH) + " }; " +
                "Value : { " + " " +
                "English : " + this.commandLibelleMap.get(Locale.ENGLISH) + " " +
                "French : " + this.commandLibelleMap.get(Locale.FRENCH) + " }; " +
                "}";
    }

    public String toYamlString() {
        return  "-"+this.name()+":\n" +
                "  DESCRIPTION:\n" +
                "    ENGLISH: " + this.commandDescriptionMap.get(Locale.ENGLISH) + "\n"+
                "    FRENCH: " + this.commandDescriptionMap.get(Locale.FRENCH) + "\n"+
                "  VALUE:\n" +
                "    ENGLISH: " + this.commandLibelleMap.get(Locale.ENGLISH) + "\n"+
                "    FRENCH: " + this.commandLibelleMap.get(Locale.FRENCH) + "\n" ;
    }


    public static CommandName fromValue(String value) {
        if(StringUtils.isEmpty(value)){
            throw new IllegalArgumentException("La valeur de la commande ne peut pas être vide");
        }
        for (CommandName commandName: CommandName.values()) {
            Optional<String> findValue = commandName.getCommandLibelleMap().values().stream().filter(s -> StringUtil.equalsIgnoreCase(value,s)).findFirst();
            if(findValue.isPresent()){
                return commandName;
            }
        }
        throw new IllegalArgumentException("Cette command n'existe pas : "+value+"");
    }
}
