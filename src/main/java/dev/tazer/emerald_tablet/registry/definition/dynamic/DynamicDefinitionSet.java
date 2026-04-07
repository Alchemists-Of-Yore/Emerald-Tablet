package dev.tazer.emerald_tablet.registry.definition.dynamic;

import dev.tazer.emerald_tablet.registry.Namespace;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DynamicDefinitionSet<T> {
    private final ResourceKey<Registry<T>> registryKey;
    private final Namespace namespace;
    private final List<DynamicDefinition<T>> entries = new ArrayList<>();

    public DynamicDefinitionSet(ResourceKey<Registry<T>> registryKey, Namespace namespace) {
        this.registryKey = registryKey;
        this.namespace = namespace;
    }

    public DynamicDefinition<T> add(String id, T object) {
        Entry<T> entry = new Entry<>(registryKey, id, object);
        entries.add(entry);
        namespace.add(entry);
        return entry;
    }

    public DynamicDefinition<T> add(String id, Function<BootstrapContext<T>, T> factory) {
        DeferredEntry<T> entry = new DeferredEntry<>(registryKey, id, factory);
        entries.add(entry);
        namespace.add(entry);
        return entry;
    }

    public void forEach(Consumer<DynamicDefinition<T>> consumer) {
        entries.forEach(consumer);
    }

    public List<DynamicDefinition<T>> entries() {
        return entries;
    }

    private static class Entry<T> extends DynamicDefinition<T> {
        Entry(ResourceKey<Registry<T>> registry, String id, T object) {
            super(registry, id, object);
        }
    }

    private static class DeferredEntry<T> extends DynamicDefinition<T> {
        DeferredEntry(ResourceKey<Registry<T>> registry, String id, Function<BootstrapContext<T>, T> factory) {
            super(registry, id, factory);
        }
    }
}
