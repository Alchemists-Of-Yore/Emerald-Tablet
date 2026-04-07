package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class CreativeTabDefinition extends BuiltInDefinition<CreativeModeTab, CreativeModeTab> implements HasName {
    private Translations translations;
    private final List<Supplier<? extends ItemLike>> explicitOrder = new ArrayList<>();
    private boolean catchAll = false;

    public CreativeTabDefinition(String id, Supplier<CreativeModeTab> creativeTab) {
        super(Registries.CREATIVE_MODE_TAB, id, creativeTab);
        this.translations = new Translations(Translations.createName(id));
    }

    public CreativeTabDefinition withItems(List<Supplier<? extends ItemLike>> items) {
        requireMutable();
        this.explicitOrder.addAll(items);
        return this;
    }

    @SafeVarargs
    public final CreativeTabDefinition withItems(Supplier<? extends ItemLike>... items) {
        requireMutable();
        this.explicitOrder.addAll(List.of(items));
        return this;
    }

    public CreativeTabDefinition catchAll() {
        requireMutable();
        this.catchAll = true;
        return this;
    }

    public boolean isCatchAll() {
        return catchAll;
    }

    public List<Supplier<? extends ItemLike>> explicitOrder() {
        return explicitOrder;
    }

    public List<ItemStack> resolveItems(Namespace namespace) {
        Set<ItemStack> result = new LinkedHashSet<>();

        for (Supplier<? extends ItemLike> item : explicitOrder) {
            result.add(new ItemStack(item.get()));
        }

        for (ItemDefinition<?> item : namespace.getDefinitions(ItemDefinition.class)) {
            if (item.creativeTab() == this) {
                result.add(item.stack());
            }
        }

        if (catchAll) {
            for (ItemDefinition<?> item : namespace.getDefinitions(ItemDefinition.class)) {
                if (item.creativeTab() == null) {
                    result.add(item.stack());
                }
            }
        }

        return new ArrayList<>(result);
    }

    @Override
    protected Registry<CreativeModeTab> builtInRegistry() {
        return BuiltInRegistries.CREATIVE_MODE_TAB;
    }

    @Override
    public String translationPrefix() {
        return "itemGroup";
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
