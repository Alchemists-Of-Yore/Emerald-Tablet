package dev.tazer.emerald_tablet.registry.definition;

import dev.tazer.emerald_tablet.registry.Namespace;

import java.util.LinkedHashMap;
import java.util.Map;

public interface HasName {
    String id();
    Namespace namespace();
    void requireMutable();

    String translationPrefix();
    Translations translations();
    void setTranslations(Translations translations);

    default String translationKey() {
        return translationPrefix() + "." + namespace().id() + "." + id();
    }

    default Map<String, String> translationEntries() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(translationKey(), translations().name());
        translations().extra().forEach((suffix, value) ->
                map.put(translationKey() + "." + suffix, value));
        return map;
    }

    default void setName(String name) {
        requireMutable();
        setTranslations(translations().withName(name));
    }

    default void addTranslation(String suffix, String value) {
        requireMutable();
        setTranslations(translations().withExtra(suffix, value));
    }
}
