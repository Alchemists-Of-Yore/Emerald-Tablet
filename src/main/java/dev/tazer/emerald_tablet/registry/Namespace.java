package dev.tazer.emerald_tablet.registry;

import dev.tazer.emerald_tablet.integration.ModIntegration;
import dev.tazer.emerald_tablet.registry.definition.Definition;
import dev.tazer.emerald_tablet.registry.definition.builtin.BuiltInDefinitionSet;
import dev.tazer.emerald_tablet.registry.definition.builtin.BuiltInDefinition;
import dev.tazer.emerald_tablet.registry.definition.dynamic.DynamicDefinition;
import dev.tazer.emerald_tablet.registry.definition.dynamic.DynamicDefinitionSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Namespace {
    private final String id;
    private final List<Definition<?, ?>> definitions = new ArrayList<>();
    private final Map<TagKey<?>, Tag<?>> tags = new HashMap<>();
    private final List<CreativeTabModification> creativeTabModifications = new ArrayList<>();

    public Namespace(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }

    public <T extends Definition<?, ?>> T add(T definition) {
        definition.build(this);
        definitions.add(definition);
        definition.onBuild(this);
        return definition;
    }

    public <B> BuiltInDefinitionSet<B> builtinDefinitionSet(ResourceKey<Registry<B>> registryKey, Registry<B> builtInRegistry) {
        return new BuiltInDefinitionSet<>(registryKey, builtInRegistry, this);
    }

    public <T> DynamicDefinitionSet<T> dynamicDefinitionSet(ResourceKey<Registry<T>> registryKey) {
        return new DynamicDefinitionSet<>(registryKey, this);
    }

    public List<Definition<?, ?>> getDefinitions() {
        return definitions.stream().toList();
    }

    public <D extends Definition<?, ?>> List<D> getDefinitions(Class<D> clazz) {
        return definitions.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }

    public List<? extends BuiltInDefinition<?, ?>> getBuiltInDefinitions() {
        return definitions.stream()
                .filter(d -> d instanceof BuiltInDefinition<?, ?>)
                .map(d -> (BuiltInDefinition<?, ?>) d)
                .toList();
    }

    public List<? extends DynamicDefinition<?>> getDynamicDefinitions() {
        return definitions.stream()
                .filter(d -> d instanceof DynamicDefinition<?>)
                .map(d -> (DynamicDefinition<?>) d)
                .toList();
    }

    @SuppressWarnings("unchecked")
    public void addToRegistrySetBuilder(RegistrySetBuilder builder) {
        Map<ResourceKey<? extends Registry<?>>, List<DynamicDefinition<?>>> byRegistry =
                getDynamicDefinitions().stream()
                        .collect(Collectors.groupingBy(DynamicDefinition::registry));

        byRegistry.forEach((registryKey, defs) -> {
            ResourceKey<Registry<Object>> key = (ResourceKey<Registry<Object>>) registryKey;
            builder.add(key, context -> {
                for (DynamicDefinition<?> def : defs) {
                    ((DynamicDefinition<Object>) def).bootstrap(context);
                }
            });
        });
    }

    @SuppressWarnings("unchecked")
    public <T> Tag<T> tag(TagKey<T> key) {
        return (Tag<T>) tags.computeIfAbsent(key, k -> new Tag<>(this, k));
    }

    public Map<TagKey<?>, Tag<?>> getTags() {
        return tags;
    }

    @SuppressWarnings("unchecked")
    public <T> List<Tag<T>> getTags(ResourceKey<Registry<T>> registry) {
        return tags.values().stream()
                .filter(tag -> tag.key().registry().equals(registry))
                .map(tag -> (Tag<T>) tag)
                .toList();
    }

    public BlockItemTag blockAndItemTag(TagKey<Block> blockTagKey, TagKey<Item> itemTagKey) {
        return new BlockItemTag(tag(blockTagKey), tag(itemTagKey));
    }

    public void ifLoaded(String modId, Consumer<Namespace> action) {
        ModIntegration.ifLoaded(modId, () -> action.accept(this));
    }

    public CreativeTabModification modifyTab(ResourceKey<CreativeModeTab> tab) {
        CreativeTabModification modification = new CreativeTabModification(tab);
        creativeTabModifications.add(modification);
        return modification;
    }

    public List<CreativeTabModification> getTabModifications() {
        return creativeTabModifications;
    }

    public ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(id, path);
    }
}
