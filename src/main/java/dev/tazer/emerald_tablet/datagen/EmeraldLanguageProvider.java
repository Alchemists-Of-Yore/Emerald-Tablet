package dev.tazer.emerald_tablet.datagen;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.Definition;
import dev.tazer.emerald_tablet.registry.definition.HasName;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class EmeraldLanguageProvider extends LanguageProvider {
    private final Namespace namespace;
    public EmeraldLanguageProvider(PackOutput output, Namespace namespace) {
        super(output, namespace.id(), "en_us");
        this.namespace = namespace;
    }

    @Override
    protected void addTranslations() {
        for (Definition<?, ?> definition : namespace.getDefinitions()) {
            if (definition instanceof HasName hasName) {
                hasName.translationEntries().forEach(this::add);
            }
        }
    }
}
