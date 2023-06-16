package fr.axa.automation.webengine.global;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class GlobalApplicationContextNoCode extends AbstractGlobalApplicationContext{

    @Builder
    public GlobalApplicationContextNoCode(AbstractSettings settings) {
        super(settings);
    }
}
