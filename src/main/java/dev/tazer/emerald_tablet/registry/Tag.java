package dev.tazer.emerald_tablet.registry;

import dev.tazer.emerald_tablet.registry.definition.Definition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.ArrayList;
import java.util.List;

public class Tag<T> {
    private final Namespace namespace;
    private final TagKey<T> key;
    private final List<Definition<?, T>> definitions = new ArrayList<>();
    private final List<ResourceLocation> optionalEntries = new ArrayList<>();
    private final List<TagKey<T>> includedTags = new ArrayList<>();

    Tag(Namespace namespace, TagKey<T> key) {
        this.namespace = namespace;
        this.key = key;
    }

    public TagKey<T> key() {
        return key;
    }

    @SafeVarargs
    public final Tag<T> add(Definition<?, T>... definitions) {
        this.definitions.addAll(List.of(definitions));
        return this;
    }

    public Tag<T> addOptional(ResourceLocation... locations) {
        optionalEntries.addAll(List.of(locations));
        return this;
    }

    @SafeVarargs
    public final Tag<T> addTag(TagKey<T>... tags) {
        includedTags.addAll(List.of(tags));
        return this;
    }

    public List<Definition<?, T>> definitions() {
        return definitions;
    }

    public List<ResourceLocation> optionalEntries() {
        return optionalEntries;
    }

    public List<TagKey<T>> includedTags() {
        return includedTags;
    }
}
