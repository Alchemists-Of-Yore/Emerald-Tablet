package dev.tazer.emerald_tablet.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CreativeTabModification {
    private final ResourceKey<CreativeModeTab> tab;
    private final List<Entry> entries = new ArrayList<>();
    private boolean frozen = false;

    public CreativeTabModification(ResourceKey<CreativeModeTab> tab) {
        this.tab = tab;
    }

    private void requireMutable() {
        if (frozen) throw new IllegalStateException("CreativeTabModification for " + tab + " is frozen and cannot be modified!");
    }

    public void freeze() {
        this.frozen = true;
    }

    public ResourceKey<CreativeModeTab> tab() {
        return tab;
    }

    public CreativeTabModification add(Supplier<? extends ItemLike> item) {
        requireMutable();
        entries.add(new Entry(Type.ADD, item, null));
        return this;
    }

    public CreativeTabModification insertBefore(Supplier<? extends ItemLike> existingItem, Supplier<? extends ItemLike> newItem) {
        requireMutable();
        entries.add(new Entry(Type.INSERT_BEFORE, newItem, existingItem));
        return this;
    }

    public CreativeTabModification insertAfter(Supplier<? extends ItemLike> existingItem, Supplier<? extends ItemLike> newItem) {
        requireMutable();
        entries.add(new Entry(Type.INSERT_AFTER, newItem, existingItem));
        return this;
    }

    @SafeVarargs
    public final CreativeTabModification insertAfter(Supplier<? extends ItemLike> anchor, Supplier<? extends ItemLike>... items) {
        requireMutable();
        for (int i = items.length - 1; i >= 0; i--) {
            entries.add(new Entry(Type.INSERT_AFTER, items[i], anchor));
        }
        return this;
    }

    @SafeVarargs
    public final CreativeTabModification insertBefore(Supplier<? extends ItemLike> anchor, Supplier<? extends ItemLike>... items) {
        requireMutable();
        for (int i = items.length - 1; i >= 0; i--) {
            entries.add(new Entry(Type.INSERT_BEFORE, items[i], anchor));
        }
        return this;
    }

    public List<Entry> entries() {
        return entries;
    }

    public enum Type {
        ADD,
        INSERT_BEFORE,
        INSERT_AFTER
    }

    public record Entry(Type type, Supplier<? extends ItemLike> item, Supplier<? extends ItemLike> anchor) {}
}
