package dev.tazer.emerald_tablet.datagen;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.builtin.BlockDefinition;
import dev.tazer.emerald_tablet.registry.definition.template.BlockStateTemplate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
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
//            switch (template) {
//                case SIMPLE -> simpleBlockWithItem(block, cubeAll(block));
//                case SLAB -> slabBlock((SlabBlock) block, blockTexture(block), blockTexture(block));
//                case STAIRS -> stairsBlock((StairBlock) block, blockTexture(block));
//                case WALL -> wallBlock((WallBlock) block, blockTexture(block));
//                case FENCE -> fenceBlock((FenceBlock) block, blockTexture(block));
//                case FENCE_GATE -> fenceGateBlock((FenceGateBlock) block, blockTexture(block));
//                case DOOR -> doorBlock((DoorBlock) block, blockTexture(block).withSuffix("_bottom"), blockTexture(block).withSuffix("_top"));
//                case TRAPDOOR -> trapdoorBlock((TrapDoorBlock) block, blockTexture(block), true);
//                case BUTTON -> buttonBlock((ButtonBlock) block, blockTexture(block));
//                case PRESSURE_PLATE -> pressurePlateBlock((PressurePlateBlock) block, blockTexture(block));
//                case PILLAR -> logBlock((RotatedPillarBlock) block);
//                case CROSS -> simpleBlock(block, models().cross(name(block), blockTexture(block)).renderType("cutout"));
//                case CROP -> {} // TODO: crops need age property handling
//                case HORIZONTAL_FACING -> horizontalBlock(block, blockTexture(block), blockTexture(block).withSuffix("_front"), blockTexture(block));
//                case ALL_FACING -> directionalBlock(block, models().cubeAll(name(block), blockTexture(block)));
//                case SIGN -> signBlock(block, blockTexture(block));
//                case WALL_SIGN -> signBlock((WallSignBlock) block, blockTexture(block));
//                case HANGING_SIGN -> hangingSignBlock((CeilingHangingSignBlock) block, blockTexture(block));
//                case WALL_HANGING_SIGN -> hangingSignBlock((WallHangingSignBlock) block, blockTexture(block));
//                case FLOWER_POT -> simpleBlock(block, models().singleTexture(name(block), mcLoc("block/flower_pot_cross"), "plant", blockTexture(block)));
//            }
        }
    }

//    private void signBlock(StandingSignBlock block, net.minecraft.resources.ResourceLocation texture) {
//        ModelFile model = models().sign(name(block), texture);
//        simpleBlock(block, model);
//    }
//
//    private void signBlock(WallSignBlock block, net.minecraft.resources.ResourceLocation texture) {
//        ModelFile model = models().sign(name(block), texture);
//        simpleBlock(block, model);
//    }
//
//    private void hangingSignBlock(CeilingHangingSignBlock block, net.minecraft.resources.ResourceLocation texture) {
//        ModelFile model = models().sign(name(block), texture);
//        simpleBlock(block, model);
//    }
//
//    private void hangingSignBlock(WallHangingSignBlock block, net.minecraft.resources.ResourceLocation texture) {
//        ModelFile model = models().sign(name(block), texture);
//        simpleBlock(block, model);
//    }
//
//    private void campfireBlock(CampfireBlock block) {
//        String n = name(block);
//        ModelFile lit = models().withExistingParent(n, mcLoc("block/template_campfire"))
//                .texture("fire", blockTexture(block).withSuffix("_fire"))
//                .texture("lit_log", blockTexture(block).withSuffix("_log_lit"));
//        ModelFile unlit = models().getExistingFile(mcLoc("block/campfire_off"));
//        getVariantBuilder(block)
//                .forAllStatesExcept(state -> {
//                    boolean isLit = state.getValue(BlockStateProperties.LIT);
//                    int rotation = (int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot();
//                    return ConfiguredModel.builder()
//                            .modelFile(isLit ? lit : unlit)
//                            .rotationY(rotation)
//                            .build();
//                }, BlockStateProperties.SIGNAL_FIRE, BlockStateProperties.WATERLOGGED);
//    }
//
//    private void lanternBlock(LanternBlock block) {
//        String n = name(block);
//        ModelFile lantern = models().withExistingParent(n, mcLoc("block/template_lantern"))
//                .texture("lantern", blockTexture(block));
//        ModelFile hanging = models().withExistingParent(n + "_hanging", mcLoc("block/template_hanging_lantern"))
//                .texture("lantern", blockTexture(block));
//        getVariantBuilder(block).forAllStates(state -> {
//            boolean isHanging = state.getValue(BlockStateProperties.HANGING);
//            return ConfiguredModel.builder()
//                    .modelFile(isHanging ? hanging : lantern)
//                    .build();
//        });
//    }
//
//    private void candleBlock(CandleBlock block) {
//        String n = name(block);
//        getVariantBuilder(block).forAllStatesExcept(state -> {
//            int candles = state.getValue(BlockStateProperties.CANDLES);
//            boolean lit = state.getValue(BlockStateProperties.LIT);
//            String suffix = (candles > 1 ? "_" + candles + "_candles" : "_one_candle") + (lit ? "_lit" : "");
//            ModelFile model = models().withExistingParent(n + suffix, mcLoc("block/template" + suffix))
//                    .texture("all", blockTexture(block))
//                    .texture("particle", blockTexture(block));
//            return ConfiguredModel.builder().modelFile(model).build();
//        }, BlockStateProperties.WATERLOGGED);
//    }

    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
