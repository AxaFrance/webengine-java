package fr.axa.automation.webengine.constante;

import fr.axa.automation.webengine.global.Browser;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum IncognitoBrowserOption {
    CHROME_PRIVATE(Browser.CHROME, Arrays.asList("--incognito")),
    EDGE_PRIVATE(Browser.CHROMIUM_EDGE, Arrays.asList("--inprivate")),
    FIREFOX_PRIVATE(Browser.FIREFOX, Arrays.asList("-private"));

    final Browser browser;
    final List<String> options ;

    public static IncognitoBrowserOption getIncognitoBrowserOption(Browser browser) {
        return Arrays.stream(IncognitoBrowserOption.values())
                .filter(incognitoBrowserOption -> incognitoBrowserOption.getBrowser().equals(browser))
                .findFirst()
                .orElse(null);
    }
}
