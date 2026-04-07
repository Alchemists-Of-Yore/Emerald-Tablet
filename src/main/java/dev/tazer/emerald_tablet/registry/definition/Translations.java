package dev.tazer.emerald_tablet.registry.definition;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Translations {
    private final String name;
    private final Map<String, String> extra;

    public Translations(String name) {
        this.name = name;
        this.extra = Map.of();
    }

    public Translations(String name, Map<String, String> extra) {
        this.name = name;
        this.extra = Collections.unmodifiableMap(new LinkedHashMap<>(extra));
    }

    public String name() {
        return name;
    }

    public Map<String, String> extra() {
        return extra;
    }

    public Translations withName(String name) {
        return new Translations(name, extra);
    }

    public Translations withExtra(String suffix, String value) {
        Map<String, String> newExtra = new LinkedHashMap<>(extra);
        newExtra.put(suffix, value);
        return new Translations(name, newExtra);
    }

    public static String createName(String id) {
        String processed = id.replace("_", " ");

        List<String> nonCapital = List.of("of", "and", "with");

        String[] words = processed.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                if (!nonCapital.contains(word)) result.append(Character.toUpperCase(word.charAt(0)));
                else result.append(word.charAt(0));
                result.append(word.substring(1)).append(" ");
            }
        }

        return result.toString().trim();
    }
}
