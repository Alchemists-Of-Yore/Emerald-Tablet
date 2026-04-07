package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.definition.template.ItemModelTemplate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ItemDefinition<T extends Item> extends ItemLikeDefinition<T, Item> {
    private ItemModelTemplate modelTemplate = ItemModelTemplate.GENERATED;
    private int fuelBurnTime = -1;
    private float compostableChance = -1f;
    @Nullable
    private CreativeTabDefinition creativeTab;

    public ItemDefinition(String id, Supplier<T> item) {
        super(Registries.ITEM, id, item);
    }

    public ItemStack stack() {
        return new ItemStack(get());
    }

    public ItemDefinition<T> model(ItemModelTemplate template) {
        requireMutable();
        this.modelTemplate = template;
        return this;
    }

    public ItemModelTemplate modelTemplate() {
        return modelTemplate;
    }

    public ItemDefinition<T> fuelBurnTime(int ticks) {
        requireMutable();
        this.fuelBurnTime = ticks;
        return this;
    }

    public boolean isFuel() {
        return fuelBurnTime > 0;
    }

    public int fuelBurnTime() {
        return fuelBurnTime;
    }

    public ItemDefinition<T> compostable(float chance) {
        requireMutable();
        this.compostableChance = chance;
        return this;
    }

    public boolean isCompostable() {
        return compostableChance >= 0;
    }

    public float compostableChance() {
        return compostableChance;
    }

    public ItemDefinition<T> creativeTab(CreativeTabDefinition tab) {
        requireMutable();
        this.creativeTab = tab;
        return this;
    }

    @Nullable
    public CreativeTabDefinition creativeTab() {
        return creativeTab;
    }

    @Override
    protected Registry<Item> builtInRegistry() {
        return BuiltInRegistries.ITEM;
    }

    @Override
    public String translationPrefix() {
        return "item";
    }
}
