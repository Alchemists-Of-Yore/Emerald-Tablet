package dev.tazer.emerald_tablet.registry.definition;

import dev.tazer.emerald_tablet.registry.Namespace;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.function.Supplier;

public abstract class Definition<T extends B, B> {
    private Namespace namespace;
    private final ResourceKey<Registry<B>> registry;
    private final String id;
    private final Supplier<T> supplier;
    private Holder<B> holder;

    protected Definition(ResourceKey<Registry<B>> registry, String id, Supplier<T> supplier) {
        this.registry = registry;
        this.id = id;
        this.supplier = supplier;
    }

    public void build(Namespace namespace) {
        requireEditable();
        this.namespace = namespace;
    }

    public void bind() {
        requireBuilt();
    }

    public boolean isBuilt() {
        return namespace != null;
    }

    public boolean isBound() {
        return holder != null;
    }

    public void requireEditable() {
        if (namespace != null) throw new IllegalStateException("Definition '" + id + "' is already built!");
    }

    public void requireBuilt() {
        if (namespace == null) throw new IllegalStateException("Definition '" + id + "' has not been built yet!");
    }

    public void requireBound() {
        if (holder == null) throw new IllegalStateException("Definition '" + id + "' has not been bound yet!");
    }

    public void onRegister(Namespace namespace) {
    }

    public final Namespace namespace() {
        requireBuilt();
        return namespace;
    }

    public final ResourceKey<Registry<B>> registry() {
        return registry;
    }

    public final ResourceKey<B> key() {
        requireBuilt();
        return ResourceKey.create(registry, namespace.id(id()));
    }

    public final String id() {
        return id;
    }

    public final T get() {
        return supplier.get();
    }

    protected void setHolder(Holder<B> holder) {
        this.holder = holder;
    }

    public final Holder<B> holder() {
        requireBound();
        return holder;
    }

    public final boolean is(TagKey<B> tag) {
        return holder().is(tag);
    }
}
