package dev.tazer.emerald_tablet.registry;

import dev.tazer.emerald_tablet.registry.definition.builtin.BlockDefinition;
import dev.tazer.emerald_tablet.registry.definition.builtin.ItemDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BlockItemTag {
    private final Tag<Block> blockTag;
    private final Tag<Item> itemTag;

    BlockItemTag(Tag<Block> blockTag, Tag<Item> itemTag) {
        this.blockTag = blockTag;
        this.itemTag = itemTag;
    }

    public final BlockItemTag add(BlockDefinition<?>... blockDefinitions) {
        for (BlockDefinition<?> blockDefinition : blockDefinitions) {
            blockTag.add(blockDefinition);
            ItemDefinition<?> itemDefinition = blockDefinition.item();
            if (itemDefinition != null) {
                itemTag.add(itemDefinition);
            }
        }
        return this;
    }

    public Tag<Block> blockTag() {
        return blockTag;
    }

    public Tag<Item> itemTag() {
        return itemTag;
    }
}
