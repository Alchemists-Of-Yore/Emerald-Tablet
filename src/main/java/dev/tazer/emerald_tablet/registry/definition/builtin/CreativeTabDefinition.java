package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;

import java.util.function.Supplier;

public class CreativeTabDefinition extends BuiltInDefinition<CreativeModeTab, CreativeModeTab> implements HasName {
    private final Translations translations;

    public CreativeTabDefinition(String id, Supplier<CreativeModeTab> supplier) {
        super(Registries.CREATIVE_MODE_TAB, id, supplier);
        this.translations = new Translations(Translations.createName(id));
    }

    @Override
    protected Registry<CreativeModeTab> builtInRegistry() {
        return BuiltInRegistries.CREATIVE_MODE_TAB;
    }

    @Override
    public String translationPrefix() {
        return "itemGroup";
    }

    @Override
    public Translations translations() {
        return translations;
    }
}
