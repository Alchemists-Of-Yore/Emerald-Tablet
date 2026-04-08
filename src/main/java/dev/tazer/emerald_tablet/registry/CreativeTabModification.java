package dev.tazer.emerald_tablet.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

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

    public CreativeTabModification add(Supplier<ItemStack> stack) {
        requireMutable();
        entries.add(new Entry(Type.ADD, stack, null));
        return this;
    }

    public CreativeTabModification remove(Supplier<ItemStack> stack) {
        requireMutable();
        entries.add(new Entry(Type.REMOVE, stack, null));
        return this;
    }

    public CreativeTabModification insertBefore(Supplier<ItemStack> anchor, Supplier<ItemStack> item) {
        requireMutable();
        entries.add(new Entry(Type.INSERT_BEFORE, item, anchor));
        return this;
    }

    public CreativeTabModification insertAfter(Supplier<ItemStack> anchor, Supplier<ItemStack> item) {
        requireMutable();
        entries.add(new Entry(Type.INSERT_AFTER, item, anchor));
        return this;
    }

    @SafeVarargs
    public final CreativeTabModification insertAfter(Supplier<ItemStack> anchor, Supplier<ItemStack>... items) {
        requireMutable();
        for (int i = items.length - 1; i >= 0; i--) {
            entries.add(new Entry(Type.INSERT_AFTER, items[i], anchor));
        }
        return this;
    }

    @SafeVarargs
    public final CreativeTabModification insertBefore(Supplier<ItemStack> anchor, Supplier<ItemStack>... items) {
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
        REMOVE,
        INSERT_BEFORE,
        INSERT_AFTER
    }

    public record Entry(Type type, Supplier<ItemStack> stack, @Nullable Supplier<ItemStack> anchor) {}
}
