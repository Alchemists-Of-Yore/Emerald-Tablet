package dev.tazer.emerald_tablet.registry.definition.dynamic;

import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class AdvancementDefinition extends DynamicDefinition<Advancement> implements HasName {
    private Translations translations;
    private String title;
    private String description;

    public AdvancementDefinition(String id, Advancement advancement) {
        super(Registries.ADVANCEMENT, id, advancement);
        this.translations = new Translations(Translations.createName(id));
        this.title = translations.name();
        this.description = "";
        extractDisplay(advancement);
    }

    public AdvancementDefinition(String id, Function<BootstrapContext<Advancement>, Advancement> factory) {
        super(Registries.ADVANCEMENT, id, factory);
        this.translations = new Translations(Translations.createName(id));
        this.title = translations.name();
        this.description = "";
    }

    public static AdvancementDefinition create(String id, Advancement.Builder builder) {
        Advancement advancement = builder.build(ResourceLocation.parse(id)).value();
        return new AdvancementDefinition(id, advancement);
    }

    private void extractDisplay(Advancement advancement) {
        advancement.display().ifPresent(display -> {
            this.title = extractTranslationValue(display.getTitle(), title);
            this.description = extractTranslationValue(display.getDescription(), description);
        });
    }

    private static String extractTranslationValue(Component component, String fallback) {
        if (component.getContents() instanceof TranslatableContents translatable) {
            Object[] args = translatable.getArgs();
            if (args.length == 0) {
                return fallback;
            }
        }
        return component.getString();
    }

    public AdvancementDefinition title(String title) {
        requireMutable();
        this.title = title;
        return this;
    }

    public AdvancementDefinition description(String description) {
        requireMutable();
        this.description = description;
        return this;
    }

    @Override
    public Translations translations() {
        return translations;
    }

    @Override
    public void setTranslations(Translations translations) {
        this.translations = translations;
    }

    @Override
    public String translationPrefix() {
        return "advancements";
    }

    @Override
    public Map<String, String> translationEntries() {
        Map<String, String> map = new LinkedHashMap<>();
        advancement().display().ifPresent(display -> {
            Component titleComponent = display.getTitle();
            Component descComponent = display.getDescription();

            if (titleComponent.getContents() instanceof TranslatableContents titleKey) {
                map.put(titleKey.getKey(), title);
            }
            if (descComponent.getContents() instanceof TranslatableContents descKey) {
                map.put(descKey.getKey(), description);
            }
        });
        return map;
    }

    private Advancement advancement() {
        return get();
    }
}
