package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;

import java.util.function.Supplier;

public abstract class ItemLikeDefinition<T extends B, B extends ItemLike> extends BuiltInDefinition<T, B> implements HasName {
    private Translations translations;

    public ItemLikeDefinition(ResourceKey<Registry<B>> registry, String id, Supplier<T> itemLike) {
        super(registry, id, itemLike);
        this.translations = new Translations(Translations.createName(id));
    }

    @Override
    public Translations translations() {
        return translations;
    }

    @Override
    public void setTranslations(Translations translations) {
        this.translations = translations;
    }
}
