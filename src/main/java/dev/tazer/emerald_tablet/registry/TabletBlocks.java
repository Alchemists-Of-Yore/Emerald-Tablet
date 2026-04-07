package dev.tazer.emerald_tablet.registry;

import dev.tazer.emerald_tablet.EmeraldTablet;
import dev.tazer.emerald_tablet.registry.definition.builtin.BlockDefinition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static dev.tazer.emerald_tablet.EmeraldTablet.EMERALD_TABLET;

public class TabletBlocks {
    public static final BlockDefinition<Block> BLOCK = EMERALD_TABLET.add(new BlockDefinition<>("testing", () -> new Block(BlockBehaviour.Properties.of())));

    public static void register() {}
}
