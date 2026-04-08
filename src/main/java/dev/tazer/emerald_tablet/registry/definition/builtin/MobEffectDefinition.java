package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

public class MobEffectDefinition<T extends MobEffect> extends BuiltInDefinition<T, MobEffect> implements HasName {
    private Translations translations;

    public MobEffectDefinition(String id, Supplier<T> mobEffect) {
        super(Registries.MOB_EFFECT, id, mobEffect);
        this.translations = new Translations(Translations.createName(id));
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
    public Registry<MobEffect> builtInRegistry() {
        return BuiltInRegistries.MOB_EFFECT;
    }

    @Override
    public String translationPrefix() {
        return "effect";
    }

    public MobEffectDefinition<T> withName(String name) {
        setName(name);
        return this;
    }
}
