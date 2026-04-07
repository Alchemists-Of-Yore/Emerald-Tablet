package dev.tazer.emerald_tablet.registry.definition.dynamic;

import dev.tazer.emerald_tablet.registry.definition.Definition;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class DynamicDefinition<T> extends Definition<T, T> {
    @Nullable
    private final Function<BootstrapContext<T>, T> factory;

    protected DynamicDefinition(ResourceKey<Registry<T>> registry, String id, T object) {
        super(registry, id, () -> object);
        this.factory = null;
    }

    protected DynamicDefinition(ResourceKey<Registry<T>> registry, String id, Function<BootstrapContext<T>, T> factory) {
        super(registry, id, () -> { throw new IllegalStateException("Deferred dynamic definition must be resolved via bootstrap()"); });
        this.factory = factory;
    }

    @Override
    public void bind() {
        throw new UnsupportedOperationException("Dynamic definitions are bound via bootstrap()");
    }

    public void bootstrap(BootstrapContext<T> context) {
        requireBuilt();
        T value = factory != null ? factory.apply(context) : get();
        setHolder(context.register(key(), value));
    }
}
