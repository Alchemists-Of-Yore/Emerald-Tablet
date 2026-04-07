package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PotionDefinition extends BuiltInDefinition<Potion, Potion> implements HasName {
    private Translations translations;
    @Nullable
    private Supplier<Holder<Potion>> brewingInput;
    @Nullable
    private Supplier<Item> brewingIngredient;

    public PotionDefinition(String id, Supplier<Potion> potion) {
        super(Registries.POTION, id, potion);
        this.translations = new Translations(Translations.createName(id));
    }

    public PotionDefinition brewingRecipe(Supplier<Holder<Potion>> input, Supplier<Item> ingredient) {
        requireMutable();
        this.brewingInput = input;
        this.brewingIngredient = ingredient;
        return this;
    }

    @Nullable
    public Supplier<Holder<Potion>> brewingInput() {
        return brewingInput;
    }

    @Nullable
    public Supplier<Item> brewingIngredient() {
        return brewingIngredient;
    }

    @Override
    public Translations translations() {
        return translations;
    }

    @Override
    public void setTranslations(Translations translations) {
        this.translations = translations;
    }

    @Override
    public Map<String, String> translationEntries() {
        Map<String, String> map = new LinkedHashMap<>();
        String name = translations.name();
        map.put("item.minecraft.potion.effect." + id(), name);
        map.put("item.minecraft.splash_potion.effect." + id(), "Splash " + name);
        map.put("item.minecraft.lingering_potion.effect." + id(), "Lingering " + name);
        map.put("item.minecraft.tipped_arrow.effect." + id(), name + " Arrow");
        return map;
    }

    @Override
    protected Registry<Potion> builtInRegistry() {
        return BuiltInRegistries.POTION;
    }

    @Override
    public String translationPrefix() {
        return "potion";
    }
}
