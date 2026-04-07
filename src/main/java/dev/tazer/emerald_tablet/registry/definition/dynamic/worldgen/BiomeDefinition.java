package dev.tazer.emerald_tablet.registry.definition.dynamic.worldgen;

import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import dev.tazer.emerald_tablet.registry.definition.dynamic.DynamicDefinition;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Function;

public class BiomeDefinition extends DynamicDefinition<Biome> implements HasName {
    private final Translations translations;

    public BiomeDefinition(String id, Biome biome) {
        super(Registries.BIOME, id, biome);
        this.translations = new Translations(Translations.createName(id));
    }

    public BiomeDefinition(String id, Function<BootstrapContext<Biome>, Biome> factory) {
        super(Registries.BIOME, id, factory);
        this.translations = new Translations(Translations.createName(id));
    }

    @Override
    public Translations translations() {
        return translations;
    }

    @Override
    public String translationPrefix() {
        return "biome";
    }
}
