package dev.tazer.emerald_tablet.datagen;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.builtin.BlockDefinition;
import dev.tazer.emerald_tablet.registry.definition.template.BlockStateTemplate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
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
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Map;

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
            Map<String, ResourceLocation> textures = definition.textures().isEmpty()
                    ? Map.of("all", ResourceLocation.fromNamespaceAndPath(namespace.id(), "block/" + definition.id()))
                    : definition.textures();

            switch (template) {
                case SIMPLE -> {
                    ResourceLocation tex = req(textures, "all", definition);
                    simpleBlockWithItem(block, models().cubeAll(name(block), tex));
                }
                case SLAB -> {
                    ResourceLocation tex = req(textures, "all", definition);
                    slabBlock((SlabBlock) block, tex, tex);
                }
                case STAIRS -> {
                    ResourceLocation tex = req(textures, "all", definition);
                    stairsBlock((StairBlock) block, tex);
                }
                case WALL -> {
                    ResourceLocation tex = req(textures, "all", definition);
                    wallBlock((WallBlock) block, tex);
                }
                case FENCE -> {
                    ResourceLocation tex = req(textures, "all", definition);
                    fenceBlock((FenceBlock) block, tex);
                }
                case FENCE_GATE -> {
                    ResourceLocation tex = req(textures, "all", definition);
                    fenceGateBlock((FenceGateBlock) block, tex);
                }
                case DOOR -> {
                    ResourceLocation top = req(textures, "top", definition);
                    ResourceLocation bottom = req(textures, "bottom", definition);
                    doorBlock((DoorBlock) block, bottom, top);
                }
                case TRAPDOOR -> {
                    ResourceLocation tex = req(textures, "all", definition);
                    trapdoorBlock((TrapDoorBlock) block, tex, true);
                }
                case BUTTON -> {
                    ResourceLocation tex = req(textures, "all", definition);
                    buttonBlock((ButtonBlock) block, tex);
                }
                case PRESSURE_PLATE -> {
                    ResourceLocation tex = req(textures, "all", definition);
                    pressurePlateBlock((PressurePlateBlock) block, tex);
                }
                case PILLAR -> logBlock((RotatedPillarBlock) block);
                case CROSS -> {
                    ResourceLocation tex = req(textures, "cross", definition);
                    simpleBlock(block, models().cross(name(block), tex).renderType("cutout"));
                }
                case CROP -> {}
                case HORIZONTAL_FACING -> {
                    ResourceLocation side = req(textures, "side", definition);
                    ResourceLocation front = req(textures, "front", definition);
                    ResourceLocation top = textures.getOrDefault("top", side);
                    horizontalBlock(block, side, front, top);
                }
                case ALL_FACING -> {
                    ResourceLocation tex = req(textures, "all", definition);
                    directionalBlock(block, models().cubeAll(name(block), tex));
                }
                case SIGN, WALL_SIGN, HANGING_SIGN, WALL_HANGING_SIGN -> {
                    ResourceLocation tex = req(textures, "particle", definition);
                    simpleBlock(block, models().sign(name(block), tex));
                }
                case CAMPFIRE -> {}
                case LANTERN -> {}
                case CANDLE -> {}
                case FLOWER_POT -> {
                    ResourceLocation plant = req(textures, "plant", definition);
                    simpleBlock(block, models().singleTexture(name(block), mcLoc("block/flower_pot_cross"), "plant", plant));
                }
            }
        }
    }

    private ResourceLocation req(Map<String, ResourceLocation> textures, String key, BlockDefinition<?> def) {
        ResourceLocation tex = textures.get(key);
        if (tex == null) {
            throw new IllegalStateException("BlockDefinition '" + def.id() + "' with template " +
                    def.blockStateTemplate() + " is missing required texture key '" + key + "'");
        }
        return tex;
    }

    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
