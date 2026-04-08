package dev.tazer.emerald_tablet.datagen;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.builtin.ItemDefinition;
import dev.tazer.emerald_tablet.registry.definition.template.ItemModelTemplate;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Map;

public class EmeraldItemModelProvider extends ItemModelProvider {
    private final Namespace namespace;

    public EmeraldItemModelProvider(PackOutput output, Namespace namespace, ExistingFileHelper existingFileHelper) {
        super(output, namespace.id(), existingFileHelper);
        this.namespace = namespace;
    }

    @Override
    protected void registerModels() {
        for (ItemDefinition<?> definition : namespace.getDefinitions(ItemDefinition.class)) {
            ItemModelTemplate template = definition.modelTemplate();
            if (template == null) continue;

            String itemId = definition.id();
            Map<String, ResourceLocation> textures = definition.textures();

            Map<String, ResourceLocation> resolved = textures.isEmpty()
                    ? Map.of("layer0", modLoc("item/" + itemId))
                    : textures;

            switch (template) {
                case BLOCK_PARENT -> withExistingParent(itemId, modLoc("block/" + itemId));
                case SPAWN_EGG -> withExistingParent(itemId, mcLoc("item/template_spawn_egg"));
                case GENERATED -> {
                    ItemModelBuilder builder = getBuilder(itemId)
                            .parent(new ModelFile.UncheckedModelFile("item/generated"));
                    resolved.forEach(builder::texture);
                }
                case HANDHELD -> {
                    ItemModelBuilder builder = getBuilder(itemId)
                            .parent(new ModelFile.UncheckedModelFile("item/handheld"));
                    resolved.forEach(builder::texture);
                }
                case HANDHELD_ROD -> {
                    ItemModelBuilder builder = getBuilder(itemId)
                            .parent(new ModelFile.UncheckedModelFile("item/handheld_rod"));
                    resolved.forEach(builder::texture);
                }
            }
        }
    }
}
