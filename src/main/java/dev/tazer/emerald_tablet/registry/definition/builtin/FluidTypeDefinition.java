package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FluidTypeDefinition extends BuiltInDefinition<FluidType, FluidType> implements HasName {
    private Translations translations;
    private final Supplier<BaseFlowingFluid> sourceSupplier;
    private final Supplier<BaseFlowingFluid> flowingSupplier;

    @Nullable
    private BuiltInDefinition<? extends Fluid, Fluid> sourceDefinition;
    @Nullable
    private BuiltInDefinition<? extends Fluid, Fluid> flowingDefinition;
    @Nullable
    private BlockDefinition<? extends LiquidBlock> blockDefinition;
    @Nullable
    private ItemDefinition<? extends BucketItem> bucketDefinition;

    private boolean hasBucket = false;
    private boolean hasBlock = false;

    @Nullable
    private ResourceLocation stillTexture;
    @Nullable
    private ResourceLocation flowingTexture;
    private int tintColor = -1;

    public FluidTypeDefinition(String id, Supplier<FluidType> fluidType, Supplier<BaseFlowingFluid> source, Supplier<BaseFlowingFluid> flowing) {
        super(NeoForgeRegistries.Keys.FLUID_TYPES, id, fluidType);
        this.translations = new Translations(Translations.createName(id));
        this.sourceSupplier = source;
        this.flowingSupplier = flowing;
    }

    public FluidTypeDefinition withBucket() {
        requireMutable();
        this.hasBucket = true;
        return this;
    }

    public FluidTypeDefinition withBlock(BlockBehaviour.Properties properties) {
        requireMutable();
        this.hasBlock = true;
        return this;
    }

    public FluidTypeDefinition stillTexture(ResourceLocation texture) {
        requireMutable();
        this.stillTexture = texture;
        return this;
    }

    public FluidTypeDefinition flowingTexture(ResourceLocation texture) {
        requireMutable();
        this.flowingTexture = texture;
        return this;
    }

    public FluidTypeDefinition tintColor(int color) {
        requireMutable();
        this.tintColor = color;
        return this;
    }

    @Nullable
    public ResourceLocation stillTexture() {
        return stillTexture;
    }

    @Nullable
    public ResourceLocation flowingTexture() {
        return flowingTexture;
    }

    public int tintColor() {
        return tintColor;
    }

    public Supplier<BaseFlowingFluid> sourceSupplier() {
        return sourceSupplier;
    }

    public Supplier<BaseFlowingFluid> flowingSupplier() {
        return flowingSupplier;
    }

    @Nullable
    public BuiltInDefinition<? extends Fluid, Fluid> sourceDefinition() {
        return sourceDefinition;
    }

    @Nullable
    public BuiltInDefinition<? extends Fluid, Fluid> flowingDefinition() {
        return flowingDefinition;
    }

    @Nullable
    public BlockDefinition<? extends LiquidBlock> blockDefinition() {
        return blockDefinition;
    }

    @Nullable
    public ItemDefinition<? extends BucketItem> bucketDefinition() {
        return bucketDefinition;
    }

    @Override
    public void onBuild(Namespace namespace) {
        super.onBuild(namespace);

        var fluids = namespace.builtinDefinitionSet(Registries.FLUID, BuiltInRegistries.FLUID);
        sourceDefinition = fluids.add(id(), sourceSupplier);
        flowingDefinition = fluids.add("flowing_" + id(), flowingSupplier);

        if (hasBlock) {
            blockDefinition = namespace.add(
                    new BlockDefinition<>(id(), () -> new LiquidBlock(sourceSupplier.get(), BlockBehaviour.Properties.of().noCollission().strength(100.0F).noLootTable()))
                            .withoutItem());
        }

        if (hasBucket) {
            bucketDefinition = namespace.add(
                    new ItemDefinition<>(id() + "_bucket", () -> new BucketItem(sourceSupplier.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1))));
        }
    }

    @Override
    protected Registry<FluidType> builtInRegistry() {
        return NeoForgeRegistries.FLUID_TYPES;
    }

    @Override
    public String translationPrefix() {
        return "fluid_type";
    }

    @Override
    public Translations translations() {
        return translations;
    }

    @Override
    public void setTranslations(Translations translations) {
        this.translations = translations;
    }

    public FluidTypeDefinition withName(String name) {
        setName(name);
        return this;
    }
}
