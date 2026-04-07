package dev.tazer.emerald_tablet.datagen;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.builtin.BlockDefinition;
import dev.tazer.emerald_tablet.registry.definition.builtin.ItemDefinition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class EmeraldDataMapProvider extends DataMapProvider {
    private final Namespace namespace;

    public EmeraldDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                  Namespace namespace) {
        super(output, lookupProvider);
        this.namespace = namespace;
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        var fuelBuilder = builder(NeoForgeDataMaps.FURNACE_FUELS);
        var compostableBuilder = builder(NeoForgeDataMaps.COMPOSTABLES);

        for (ItemDefinition<?> definition : namespace.getDefinitions(ItemDefinition.class)) {
            if (definition.isFuel()) {
                fuelBuilder.add(definition.key(), new FurnaceFuel(definition.fuelBurnTime()), false);
            }
            if (definition.isCompostable()) {
                compostableBuilder.add(definition.key(), new Compostable(definition.compostableChance()), false);
            }
        }
    }
}
