package dev.tazer.emerald_tablet.datagen;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.Tag;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EmeraldTagsProvider<T> extends TagsProvider<T> {
    private final Namespace namespace;

    public EmeraldTagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, Namespace namespace, ExistingFileHelper existingFileHelper) {
        super(output, registryKey, lookupProvider, namespace.id(), existingFileHelper);
        this.namespace = namespace;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        List<Tag<T>> tags = namespace.getTags((ResourceKey<Registry<T>>) registryKey);
        for (Tag<T> tag : tags) {
            TagAppender<T> appender = tag(tag.key());
            tag.definitions().forEach(definition -> appender.add(definition.key()));
            tag.optionalEntries().forEach(appender::addOptional);
            tag.includedTags().forEach(appender::addTag);
        }
    }
}
