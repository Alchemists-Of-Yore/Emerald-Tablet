package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.Namespace;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BuiltInDefinitionSet<B> {
    private final ResourceKey<Registry<B>> registryKey;
    private final Registry<B> builtInRegistry;
    private final Namespace namespace;
    private final List<BuiltInDefinition<?, B>> entries = new ArrayList<>();

    public BuiltInDefinitionSet(ResourceKey<Registry<B>> registryKey, Registry<B> builtInRegistry, Namespace namespace) {
        this.registryKey = registryKey;
        this.builtInRegistry = builtInRegistry;
        this.namespace = namespace;
    }

    public <T extends B> BuiltInDefinition<T, B> add(String id, Supplier<T> supplier) {
        Entry<T, B> entry = new Entry<>(registryKey, builtInRegistry, id, supplier);
        entries.add(entry);
        namespace.add(entry);
        return entry;
    }

    public void forEach(Consumer<BuiltInDefinition<?, B>> consumer) {
        entries.forEach(consumer);
    }

    public List<BuiltInDefinition<?, B>> entries() {
        return entries;
    }

    private static class Entry<T extends B, B> extends BuiltInDefinition<T, B> {
        private final Registry<B> builtInRegistry;

        Entry(ResourceKey<Registry<B>> registryKey, Registry<B> builtInRegistry, String id, Supplier<T> supplier) {
            super(registryKey, id, supplier);
            this.builtInRegistry = builtInRegistry;
        }

        @Override
        public Registry<B> builtInRegistry() {
            return builtInRegistry;
        }
    }
}
