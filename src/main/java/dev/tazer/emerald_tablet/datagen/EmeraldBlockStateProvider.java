package dev.tazer.emerald_tablet.datagen;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.builtin.BlockDefinition;
import dev.tazer.emerald_tablet.registry.definition.template.BlockStateTemplate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class EmeraldBlockStateProvider extends BlockStateProvider {
    private final Namespace namespace;

    public EmeraldBlockStateProvider(PackOutput output, Namespace namespace, ExistingFileHelper existingFileHelper) {
        super(output, namespace.id(), existingFileHelper);
        this.namespace = namespace;
    }

    @Override
    protected void registerStatesAndModels() {
        for (BlockDefinition<?> definition : namespace.getDefinitions(BlockDefinition.class)) {
            BlockStateTemplate template = definition.blockStateTemplate();
            if (template == null) continue;

            Block block = definition.get();
            switch (template) {
                case SIMPLE -> simpleBlockWithItem(block, cubeAll(block));
                case SLAB -> slabBlock((SlabBlock) block, blockTexture(block), blockTexture(block));
                case STAIRS -> stairsBlock((StairBlock) block, blockTexture(block));
                case WALL -> wallBlock((WallBlock) block, blockTexture(block));
                case FENCE -> fenceBlock((FenceBlock) block, blockTexture(block));
                case FENCE_GATE -> fenceGateBlock((FenceGateBlock) block, blockTexture(block));
                case DOOR -> doorBlock((DoorBlock) block, blockTexture(block).withSuffix("_bottom"), blockTexture(block).withSuffix("_top"));
                case TRAPDOOR -> trapdoorBlock((TrapDoorBlock) block, blockTexture(block), true);
                case BUTTON -> buttonBlock((ButtonBlock) block, blockTexture(block));
                case PRESSURE_PLATE -> pressurePlateBlock((PressurePlateBlock) block, blockTexture(block));
                case PILLAR -> logBlock((RotatedPillarBlock) block);
                case CROSS -> simpleBlock(block, models().cross(name(block), blockTexture(block)).renderType("cutout"));
                case CROP -> {} // TODO: crops need age property handling
                case HORIZONTAL_FACING -> horizontalBlock(block, blockTexture(block), blockTexture(block).withSuffix("_front"), blockTexture(block));
                case ALL_FACING -> directionalBlock(block, models().cubeAll(name(block), blockTexture(block)));
            }
        }
    }

    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
