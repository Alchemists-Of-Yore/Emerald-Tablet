package dev.tazer.emerald_tablet.datagen;

import dev.tazer.emerald_tablet.EmeraldTablet;
import dev.tazer.emerald_tablet.registry.Namespace;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = EmeraldTablet.MODID)
public class DataGenEvents {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        boolean server = event.includeServer();
        boolean client = event.includeClient();

        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder();

        EmeraldTablet.namespaces().forEach(namespace -> {
            generator.addProvider(client, new EmeraldLanguageProvider(packOutput, namespace));
            generator.addProvider(client, new EmeraldBlockStateProvider(packOutput, namespace, existingFileHelper));
            generator.addProvider(client, new EmeraldItemModelProvider(packOutput, namespace, existingFileHelper));

            generator.addProvider(server, new EmeraldDataMapProvider(packOutput, lookupProvider, namespace));

            generator.addProvider(server, new LootTableProvider(packOutput, Set.of(), List.of(
                    new LootTableProvider.SubProviderEntry(
                            lookup -> new EmeraldBlockLootProvider(lookup, namespace),
                            LootContextParamSets.BLOCK
                    )
            ), lookupProvider));

            namespace.getTags().keySet().stream()
                    .map(tagKey -> tagKey.registry())
                    .distinct()
                    .forEach(registryKey -> registerTagProvider(
                            generator, packOutput, lookupProvider, namespace, existingFileHelper, registryKey));

            namespace.addToRegistrySetBuilder(registrySetBuilder);
        });

        generator.addProvider(server, new DatapackBuiltinEntriesProvider(
                packOutput, lookupProvider, registrySetBuilder, Set.of(EmeraldTablet.MODID)));
    }

    @SuppressWarnings("unchecked")
    private static <T> void registerTagProvider(DataGenerator generator, PackOutput packOutput,
                                                 CompletableFuture<HolderLookup.Provider> lookupProvider,
                                                 Namespace namespace, ExistingFileHelper existingFileHelper,
                                                 ResourceKey<?> registryKey) {
        generator.addProvider(true, new EmeraldTagsProvider<>(
                packOutput, (ResourceKey<? extends Registry<T>>) registryKey,
                lookupProvider, namespace, existingFileHelper));
    }
}
