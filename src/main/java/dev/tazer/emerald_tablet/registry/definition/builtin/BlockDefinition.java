package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.HasLootTable;
import dev.tazer.emerald_tablet.registry.definition.template.BlockStateTemplate;
import dev.tazer.emerald_tablet.registry.definition.template.ItemModelTemplate;
import dev.tazer.emerald_tablet.registry.definition.template.LootTableTemplate;
import dev.tazer.emerald_tablet.registry.definition.template.MiningLevel;
import dev.tazer.emerald_tablet.registry.definition.template.MiningTool;
import dev.tazer.emerald_tablet.registry.definition.template.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockDefinition<T extends Block> extends ItemLikeDefinition<T, Block> implements HasLootTable {
    private ItemDefinition<?> item = new ItemDefinition<>(id(), () -> new BlockItem(get(), new Item.Properties()))
            .model(ItemModelTemplate.BLOCK_PARENT);

    @Nullable
    private BlockStateTemplate blockStateTemplate;

    @Nullable
    private LootTableTemplate lootTableTemplate;

    @Nullable
    private MiningTool miningTool;
    @Nullable
    private MiningLevel miningLevel;

    @Nullable
    private RenderType renderType;

    @Nullable
    private Supplier<Item> alternativeDrop;

    private int flammability = -1;
    private int flameEncouragement = -1;

    @Nullable
    private Supplier<Block> strippedBlock;

    public BlockDefinition(String id, Supplier<T> block) {
        super(Registries.BLOCK, id, block);
    }

    public BlockDefinition<T> withoutItem() {
        requireEditable();
        this.item = null;
        return this;
    }

    public BlockDefinition<T> withItem(ItemDefinition<?> itemDefinition) {
        requireEditable();
        this.item = itemDefinition;
        return this;
    }

    public boolean hasItem() {
        return item != null;
    }

    @Nullable
    public ItemDefinition<?> itemDefinition() {
        return item;
    }

    public BlockDefinition<T> blockState(BlockStateTemplate template) {
        requireEditable();
        this.blockStateTemplate = template;
        return this;
    }

    @Nullable
    public BlockStateTemplate blockStateTemplate() {
        return blockStateTemplate;
    }

    public BlockDefinition<T> lootTable(LootTableTemplate template) {
        requireEditable();
        this.lootTableTemplate = template;
        return this;
    }

    @Nullable
    public LootTableTemplate lootTableTemplate() {
        return lootTableTemplate;
    }

    public BlockDefinition<T> alternativeDrop(Supplier<Item> drop) {
        requireEditable();
        this.alternativeDrop = drop;
        return this;
    }

    @Nullable
    public Supplier<Item> alternativeDrop() {
        return alternativeDrop;
    }

    public BlockDefinition<T> miningTool(MiningTool tool) {
        requireEditable();
        this.miningTool = tool;
        return this;
    }

    @Nullable
    public MiningTool miningTool() {
        return miningTool;
    }

    public BlockDefinition<T> miningLevel(MiningLevel level) {
        requireEditable();
        this.miningLevel = level;
        return this;
    }

    @Nullable
    public MiningLevel miningLevel() {
        return miningLevel;
    }

    public BlockDefinition<T> renderType(RenderType type) {
        requireEditable();
        this.renderType = type;
        return this;
    }

    @Nullable
    public RenderType renderType() {
        return renderType;
    }

    public BlockDefinition<T> flammable(int encouragement, int flammability) {
        requireEditable();
        this.flameEncouragement = encouragement;
        this.flammability = flammability;
        return this;
    }

    public boolean isFlammable() {
        return flameEncouragement >= 0;
    }

    public int flameEncouragement() {
        return flameEncouragement;
    }

    public int flameFlammability() {
        return flammability;
    }

    public BlockDefinition<T> strippable(Supplier<Block> stripped) {
        requireEditable();
        this.strippedBlock = stripped;
        return this;
    }

    @Nullable
    public Supplier<Block> strippedBlock() {
        return strippedBlock;
    }

    @Override
    @Nullable
    public Supplier<LootTable.Builder> lootTableBuilderSupplier() {
        return null;
    }

    @Override
    @Nullable
    public ResourceKey<LootTable> lootTableKey() {
        if (lootTableTemplate == null) return null;
        return ResourceKey.create(Registries.LOOT_TABLE, namespace().id("blocks/" + id()));
    }

    @Override
    protected Registry<Block> builtInRegistry() {
        return BuiltInRegistries.BLOCK;
    }

    @Override
    public String translationPrefix() {
        return "block";
    }

    @Override
    public void onRegister(Namespace namespace) {
        super.onRegister(namespace);

        if (item != null) {
            namespace.add(item);
        }

        if (miningTool != null) {
            namespace.tag(miningTool.tag()).add(this);
        }

        if (miningLevel != null && miningLevel.tag() != null) {
            namespace.tag(miningLevel.tag()).add(this);
        }
    }
}
