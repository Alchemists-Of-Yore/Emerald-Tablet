package dev.tazer.emerald_tablet.integration;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fml.ModList;

public final class ModIntegration {

    private ModIntegration() {}

    public static boolean isLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static void ifLoaded(String modId, Runnable action) {
        if (isLoaded(modId)) action.run();
    }

    public static ResourceLocation location(String modId, String path) {
        return ResourceLocation.fromNamespaceAndPath(modId, path);
    }

    public static Block getBlock(String modId, String id) {
        return BuiltInRegistries.BLOCK.get(location(modId, id));
    }

    public static Item getItem(String modId, String id) {
        return BuiltInRegistries.ITEM.get(location(modId, id));
    }

    public static Fluid getFluid(String modId, String id) {
        return BuiltInRegistries.FLUID.get(location(modId, id));
    }
}
