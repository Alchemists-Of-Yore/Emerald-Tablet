package dev.tazer.emerald_tablet.registry;

import dev.tazer.emerald_tablet.loot.AddItemLootModifier;
import dev.tazer.emerald_tablet.registry.definition.builtin.BuiltInDefinition;
import dev.tazer.emerald_tablet.registry.definition.builtin.BuiltInDefinitionSet;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import com.mojang.serialization.MapCodec;

import static dev.tazer.emerald_tablet.EmeraldTablet.EMERALD_TABLET;

public class TheTablet {
    private static final BuiltInDefinitionSet<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS =
            EMERALD_TABLET.builtinDefinitionSet(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS);

    public static final BuiltInDefinition<MapCodec<AddItemLootModifier>, MapCodec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIERS.add("add_item", () -> AddItemLootModifier.CODEC);

    public static void register() {}
}
