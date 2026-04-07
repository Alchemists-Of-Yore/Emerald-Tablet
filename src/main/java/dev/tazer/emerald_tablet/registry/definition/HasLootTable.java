package dev.tazer.emerald_tablet.registry.definition;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface HasLootTable {
    @Nullable
    Supplier<LootTable.Builder> lootTableBuilderSupplier();

    @Nullable
    ResourceKey<LootTable> lootTableKey();
}
