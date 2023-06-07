package fr.axa.automation.webengine.report.constante;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public enum HtmlFileConstant {
    CSS_FILE_LIST(Arrays.asList("badge.css","banner-information.css","body-content.css","content-view.css","global.css","header.css","modal.css","tab.css","table.css","tree.css")),
    JS_FILE_LIST(Arrays.asList("global.js"));
    final List<String> value;
}
