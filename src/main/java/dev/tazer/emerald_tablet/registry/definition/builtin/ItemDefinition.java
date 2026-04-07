package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.definition.template.ItemModelTemplate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ItemDefinition<T extends Item> extends ItemLikeDefinition<T, Item> {
    private ItemModelTemplate modelTemplate = ItemModelTemplate.GENERATED;
    private int fuelBurnTime = -1;
    private float compostableChance = -1f;

    public ItemDefinition(String id, Supplier<T> item) {
        super(Registries.ITEM, id, item);
    }

    public ItemDefinition<T> model(ItemModelTemplate template) {
        requireEditable();
        this.modelTemplate = template;
        return this;
    }

    public ItemModelTemplate modelTemplate() {
        return modelTemplate;
    }

    public ItemDefinition<T> fuelBurnTime(int ticks) {
        requireEditable();
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
        requireEditable();
        this.compostableChance = chance;
        return this;
    }

    public boolean isCompostable() {
        return compostableChance >= 0;
    }

    public float compostableChance() {
        return compostableChance;
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
