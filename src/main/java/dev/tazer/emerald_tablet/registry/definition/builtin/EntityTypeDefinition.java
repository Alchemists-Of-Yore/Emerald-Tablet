package dev.tazer.emerald_tablet.registry.definition.builtin;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.HasName;
import dev.tazer.emerald_tablet.registry.definition.Translations;
import dev.tazer.emerald_tablet.registry.definition.template.ItemModelTemplate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class EntityTypeDefinition<T extends Entity> extends BuiltInDefinition<EntityType<T>, EntityType<?>> implements HasName {
    private Translations translations;
    private int spawnEggPrimaryColor = -1;
    private int spawnEggSecondaryColor = -1;
    @Nullable
    private Supplier<AttributeSupplier.Builder> attributesSupplier;
    @Nullable
    private SpawnPlacementConfig<?> spawnPlacementConfig;

    public EntityTypeDefinition(String id, Supplier<EntityType<T>> entityType) {
        super(Registries.ENTITY_TYPE, id, entityType);
        this.translations = new Translations(Translations.createName(id));
    }

    public static <E extends Entity> EntityTypeDefinition<E> create(String id, EntityType.Builder<E> builder) {
        return new EntityTypeDefinition<>(id, () -> builder.build(id));
    }

    public EntityTypeDefinition<T> spawnEgg(int primaryColor, int secondaryColor) {
        requireMutable();
        this.spawnEggPrimaryColor = primaryColor;
        this.spawnEggSecondaryColor = secondaryColor;
        return this;
    }

    public boolean hasSpawnEgg() {
        return spawnEggPrimaryColor >= 0;
    }

    public int spawnEggPrimaryColor() {
        return spawnEggPrimaryColor;
    }

    public int spawnEggSecondaryColor() {
        return spawnEggSecondaryColor;
    }

    public EntityTypeDefinition<T> attributes(Supplier<AttributeSupplier.Builder> attributes) {
        requireMutable();
        this.attributesSupplier = attributes;
        return this;
    }

    @Nullable
    public Supplier<AttributeSupplier.Builder> attributesSupplier() {
        return attributesSupplier;
    }

    public <M extends Mob> EntityTypeDefinition<T> spawnPlacement(SpawnPlacementType placementType, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<M> predicate) {
        requireMutable();
        this.spawnPlacementConfig = new SpawnPlacementConfig<>(placementType, heightmapType, predicate);
        return this;
    }

    @Nullable
    public SpawnPlacementConfig<?> spawnPlacementConfig() {
        return spawnPlacementConfig;
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
    protected Registry<EntityType<?>> builtInRegistry() {
        return BuiltInRegistries.ENTITY_TYPE;
    }

    @Override
    public String translationPrefix() {
        return "entity";
    }

    @Override
    public void onBuild(Namespace namespace) {
        super.onBuild(namespace);

        if (hasSpawnEgg()) {
            @SuppressWarnings("unchecked")
            EntityType<? extends Mob> mobType = (EntityType<? extends Mob>) get();
            namespace.add(
                    new ItemDefinition<>(
                            id() + "_spawn_egg",
                            () -> new SpawnEggItem(mobType, spawnEggPrimaryColor, spawnEggSecondaryColor, new Item.Properties())
                    ).model(ItemModelTemplate.SPAWN_EGG).withName(translations.name() + " Spawn Egg")
            );
        }
    }

    public record SpawnPlacementConfig<M extends Mob>(
            SpawnPlacementType placementType,
            Heightmap.Types heightmapType,
            SpawnPlacements.SpawnPredicate<M> predicate
    ) {}
}
