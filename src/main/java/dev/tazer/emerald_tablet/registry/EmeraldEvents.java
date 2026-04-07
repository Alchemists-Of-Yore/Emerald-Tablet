package dev.tazer.emerald_tablet.registry;

import dev.tazer.emerald_tablet.EmeraldTablet;
import dev.tazer.emerald_tablet.registry.definition.builtin.BlockDefinition;
import dev.tazer.emerald_tablet.registry.definition.builtin.CreativeTabDefinition;
import dev.tazer.emerald_tablet.registry.definition.builtin.EntityTypeDefinition;
import dev.tazer.emerald_tablet.registry.definition.builtin.PotionDefinition;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = EmeraldTablet.MODID)
public class EmeraldEvents {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> EmeraldTablet.namespaces().forEach(namespace -> {
            for (BlockDefinition<?> block : namespace.getDefinitions(BlockDefinition.class)) {
                if (block.isFlammable()) {
                    ((FireBlock) Blocks.FIRE).setFlammable(block.get(), block.flameEncouragement(), block.flameFlammability());
                }
            }
        }));
    }

    @SubscribeEvent
    public static void onEntityAttributes(EntityAttributeCreationEvent event) {
        EmeraldTablet.namespaces().forEach(namespace -> {
            for (EntityTypeDefinition<?> definition : namespace.getDefinitions(EntityTypeDefinition.class)) {
                if (definition.attributesSupplier() != null) {
                    EntityType<?> entityType = definition.get();
                    event.put((EntityType<? extends LivingEntity>) entityType, definition.attributesSupplier().get().build());
                }
            }
        });
    }

    @SubscribeEvent
    public static void onSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        EmeraldTablet.namespaces().forEach(namespace -> {
            for (EntityTypeDefinition<?> definition : namespace.getDefinitions(EntityTypeDefinition.class)) {
                EntityTypeDefinition.SpawnPlacementConfig<?> config = definition.spawnPlacementConfig();
                if (config != null) {
                    registerSpawnPlacement(event, definition.get(), config);
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <M extends Mob> void registerSpawnPlacement(RegisterSpawnPlacementsEvent event, EntityType<?> entityType, EntityTypeDefinition.SpawnPlacementConfig<M> config) {
        event.register((EntityType<M>) entityType, config.placementType(), config.heightmapType(), config.predicate(),
                RegisterSpawnPlacementsEvent.Operation.AND);
    }

    @SubscribeEvent
    public static void onBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        EmeraldTablet.namespaces().forEach(namespace -> {
            for (PotionDefinition definition : namespace.getDefinitions(PotionDefinition.class)) {
                if (definition.brewingInput() != null && definition.brewingIngredient() != null) {
                    builder.addMix(definition.brewingInput().get(), definition.brewingIngredient().get(), definition.holder());
                }
            }
        });
    }

    @SubscribeEvent
    public static void onCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        EmeraldTablet.namespaces().forEach(namespace -> {
            for (CreativeTabDefinition tab : namespace.getDefinitions(CreativeTabDefinition.class)) {
                if (event.getTab() == tab.get()) {
                    for (ItemStack stack : tab.resolveItems(namespace)) {
                        event.accept(stack);
                    }
                }
            }

            for (CreativeTabModification modification : namespace.getTabModifications()) {
                if (event.getTabKey() == modification.tab()) {
                    for (CreativeTabModification.Entry entry : modification.entries()) {
                        switch (entry.type()) {
                            case ADD -> event.accept(new ItemStack(entry.item().get()));
                            case INSERT_BEFORE -> event.insertBefore(
                                    new ItemStack(entry.anchor().get()),
                                    new ItemStack(entry.item().get()),
                                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                            case INSERT_AFTER -> event.insertAfter(
                                    new ItemStack(entry.anchor().get()),
                                    new ItemStack(entry.item().get()),
                                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                        }
                    }
                    modification.freeze();
                }
            }
        });
    }

    @SubscribeEvent
    public static void onToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility() != ItemAbilities.AXE_STRIP) return;

        BlockState state = event.getState();
        EmeraldTablet.namespaces().forEach(namespace -> {
            for (BlockDefinition<?> definition : namespace.getDefinitions(BlockDefinition.class)) {
                if (definition.strippedBlock() != null && state.is(definition.get())) {
                    event.setFinalState(definition.strippedBlock().get().withPropertiesOf(state));
                }
            }
        });
    }
}
