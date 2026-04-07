package dev.tazer.emerald_tablet.datagen;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.builtin.BlockDefinition;
import dev.tazer.emerald_tablet.registry.definition.template.LootTableTemplate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;

import java.util.HashSet;
import java.util.Set;

public class EmeraldBlockLootProvider extends BlockLootSubProvider {
    private final Namespace namespace;

    public EmeraldBlockLootProvider(HolderLookup.Provider lookupProvider, Namespace namespace) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        this.namespace = namespace;
    }

    @Override
    protected void generate() {
        for (BlockDefinition<?> definition : namespace.getDefinitions(BlockDefinition.class)) {
            LootTableTemplate template = definition.lootTableTemplate();
            if (template == null) continue;

            Block block = definition.get();
            switch (template) {
                case SELF_DROP -> dropSelf(block);
                case SILK_TOUCH_ONLY -> dropWhenSilkTouch(block);
                case SILK_TOUCH_OR_EQUIVALENT -> {
                    if (definition.alternativeDrop() != null) {
                        add(block, createSingleItemTableWithSilkTouch(block, definition.alternativeDrop().get()));
                    }
                }
                case SLAB -> add(block, createSlabItemTable(block));
                case DOOR -> add(block, createDoorTable(block));
                case LEAVES -> add(block, createLeavesDrops(block, block, NORMAL_LEAVES_SAPLING_CHANCES));
                case NOTHING -> {}
                case SHEARS_ONLY -> add(block, createShearsOnlyDrop(block));
                case SHEARS_OR_SILK_TOUCH -> add(block, createShearsDispatchTable(block, applyExplosionCondition(block, LootItem.lootTableItem(block))));
            }
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        Set<Block> blocks = new HashSet<>();
        for (BlockDefinition<?> definition : namespace.getDefinitions(BlockDefinition.class)) {
            if (definition.lootTableTemplate() != null) {
                blocks.add(definition.get());
            }
        }
        return blocks;
    }
}
