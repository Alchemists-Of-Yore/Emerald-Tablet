package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.Map;

public class SoundEventDefinition extends BuiltInDefinition<SoundEvent, SoundEvent> implements HasName {
    private final Translations translations;
    private boolean hasSubtitle = true;

    public SoundEventDefinition(String id) {
        super(Registries.SOUND_EVENT, id, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.withDefaultNamespace(id)));
        this.translations = new Translations(Translations.createName(id));
    }

    public SoundEventDefinition withoutSubtitle() {
        requireEditable();
        this.hasSubtitle = false;
        return this;
    }

    public boolean hasSubtitle() {
        return hasSubtitle;
    }

    @Override
    public Translations translations() {
        return translations;
    }

    @Override
    public Map<String, String> translationEntries() {
        if (!hasSubtitle) return Map.of();
        return HasName.super.translationEntries();
    }

    @Override
    protected Registry<SoundEvent> builtInRegistry() {
        return BuiltInRegistries.SOUND_EVENT;
    }

    @Override
    public String translationPrefix() {
        return "subtitles";
    }
}
