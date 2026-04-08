package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SoundEventDefinition extends BuiltInDefinition<SoundEvent, SoundEvent> implements HasName {
    private Translations translations;
    private boolean hasSubtitle = true;
    private final List<ResourceLocation> soundFiles = new ArrayList<>();

    public SoundEventDefinition(String id, Supplier<SoundEvent> soundEvent) {
        super(Registries.SOUND_EVENT, id, soundEvent);
        this.translations = new Translations(Translations.createName(id));
    }

    public SoundEventDefinition withoutSubtitle() {
        requireMutable();
        this.hasSubtitle = false;
        return this;
    }

    public SoundEventDefinition sound(ResourceLocation soundFile) {
        requireMutable();
        this.soundFiles.add(soundFile);
        return this;
    }

    public boolean hasSubtitle() {
        return hasSubtitle;
    }

    public List<ResourceLocation> soundFiles() {
        return Collections.unmodifiableList(soundFiles);
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
    public Map<String, String> translationEntries() {
        if (!hasSubtitle) return Map.of();
        return HasName.super.translationEntries();
    }

    @Override
    public Registry<SoundEvent> builtInRegistry() {
        return BuiltInRegistries.SOUND_EVENT;
    }

    @Override
    public String translationPrefix() {
        return "subtitles";
    }

    public SoundEventDefinition withName(String name) {
        setName(name);
        return this;
    }
}
