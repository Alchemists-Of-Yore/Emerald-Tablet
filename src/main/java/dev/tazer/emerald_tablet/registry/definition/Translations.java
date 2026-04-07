package dev.tazer.emerald_tablet.registry.definition;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Translations {
    private String name;
    private final Map<String, String> extra = new LinkedHashMap<>();

    public Translations(String defaultName) {
        this.name = defaultName;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> extra() {
        return Collections.unmodifiableMap(extra);
    }

    public void add(String suffix, String value) {
        extra.put(suffix, value);
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
