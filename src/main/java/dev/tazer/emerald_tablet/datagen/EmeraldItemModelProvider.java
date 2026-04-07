package dev.tazer.emerald_tablet.datagen;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.builtin.ItemDefinition;
import dev.tazer.emerald_tablet.registry.definition.template.ItemModelTemplate;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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
            String itemId = definition.id();

            switch (template) {
                case BLOCK_PARENT -> withExistingParent(itemId, modLoc("block/" + itemId));
                case GENERATED -> basicItem(definition.get());
                case HANDHELD -> handheldItem(itemId);
                case HANDHELD_ROD -> handheldRodItem(itemId);
                case SPAWN_EGG -> withExistingParent(itemId, mcLoc("item/template_spawn_egg"));
            }
        }
    }

    private ItemModelBuilder handheldItem(String itemId) {
        return getBuilder(itemId)
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", modLoc("item/" + itemId));
    }

    private ItemModelBuilder handheldRodItem(String itemId) {
        return getBuilder(itemId)
                .parent(new ModelFile.UncheckedModelFile("item/handheld_rod"))
                .texture("layer0", modLoc("item/" + itemId));
    }
}
