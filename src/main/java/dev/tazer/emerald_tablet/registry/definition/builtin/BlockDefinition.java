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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
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

    private final Map<String, ResourceLocation> textures = new LinkedHashMap<>();

    public BlockDefinition(String id, Supplier<T> block) {
        super(Registries.BLOCK, id, block);
    }

    public BlockDefinition<T> withoutItem() {
        requireMutable();
        this.item = null;
        return this;
    }

    public BlockDefinition<T> withItem(ItemDefinition<?> itemDefinition) {
        requireMutable();
        this.item = itemDefinition;
        return this;
    }

    public boolean hasItem() {
        return item != null;
    }

    @Nullable
    public ItemDefinition<?> item() {
        return item;
    }

    public BlockDefinition<T> blockState(BlockStateTemplate template) {
        requireMutable();
        this.blockStateTemplate = template;
        return this;
    }

    @Nullable
    public BlockStateTemplate blockStateTemplate() {
        return blockStateTemplate;
    }

    public BlockDefinition<T> lootTable(LootTableTemplate template) {
        requireMutable();
        this.lootTableTemplate = template;
        return this;
    }

    @Nullable
    public LootTableTemplate lootTableTemplate() {
        return lootTableTemplate;
    }

    public BlockDefinition<T> alternativeDrop(Supplier<Item> drop) {
        requireMutable();
        this.alternativeDrop = drop;
        return this;
    }

    @Nullable
    public Supplier<Item> alternativeDrop() {
        return alternativeDrop;
    }

    public BlockDefinition<T> miningTool(MiningTool tool) {
        requireMutable();
        this.miningTool = tool;
        return this;
    }

    @Nullable
    public MiningTool miningTool() {
        return miningTool;
    }

    public BlockDefinition<T> miningLevel(MiningLevel level) {
        requireMutable();
        this.miningLevel = level;
        return this;
    }

    @Nullable
    public MiningLevel miningLevel() {
        return miningLevel;
    }

    public BlockDefinition<T> renderType(RenderType type) {
        requireMutable();
        this.renderType = type;
        return this;
    }

    @Nullable
    public RenderType renderType() {
        return renderType;
    }

    public BlockDefinition<T> flammable(int encouragement, int flammability) {
        requireMutable();
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
        requireMutable();
        this.strippedBlock = stripped;
        return this;
    }

    @Nullable
    public Supplier<Block> strippedBlock() {
        return strippedBlock;
    }

    public BlockDefinition<T> texture(String key, ResourceLocation texture) {
        requireMutable();
        this.textures.put(key, texture);
        return this;
    }

    public BlockDefinition<T> texture(ResourceLocation texture) {
        return texture("all", texture);
    }

    public Map<String, ResourceLocation> textures() {
        return Collections.unmodifiableMap(textures);
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
    public void onBuild(Namespace namespace) {
        super.onBuild(namespace);

        if (item != null) {
            if (blockStateTemplate == null && item.modelTemplate() == ItemModelTemplate.BLOCK_PARENT) {
                item.withoutModel();
            }
            item = namespace.add(item);
        }

        if (miningTool != null) {
            namespace.tag(miningTool.tag()).add(this);
        }

        if (miningLevel != null && miningLevel.tag() != null) {
            namespace.tag(miningLevel.tag()).add(this);
        }
    }

    public BlockDefinition<T> withName(String name) {
        setName(name);
        return this;
    }
}
