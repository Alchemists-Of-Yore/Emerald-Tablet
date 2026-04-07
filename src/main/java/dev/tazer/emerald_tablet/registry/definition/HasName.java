package dev.tazer.emerald_tablet.registry.definition;

import dev.tazer.emerald_tablet.registry.Namespace;

import java.util.LinkedHashMap;
import java.util.Map;

public interface HasName {
    String id();
    Namespace namespace();
    void requireEditable();

    String translationPrefix();
    Translations translations();

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

    @SuppressWarnings("unchecked")
    default <S extends Definition<?, ?>> S withName(String name) {
        requireEditable();
        translations().setName(name);
        return (S) this;
    }

    @SuppressWarnings("unchecked")
    default <S extends Definition<?, ?>> S withTranslation(String suffix, String value) {
        requireEditable();
        translations().add(suffix, value);
        return (S) this;
    }
}
