package dev.tazer.emerald_tablet.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.definition.builtin.SoundEventDefinition;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class EmeraldSoundProvider implements DataProvider {
    private final PackOutput output;
    private final Namespace namespace;

    public EmeraldSoundProvider(PackOutput output, Namespace namespace) {
        this.output = output;
        this.namespace = namespace;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        JsonObject root = new JsonObject();

        for (SoundEventDefinition definition : namespace.getDefinitions(SoundEventDefinition.class)) {
            if (definition.soundFiles().isEmpty()) continue;

            JsonObject entry = new JsonObject();

            JsonArray sounds = new JsonArray();
            for (ResourceLocation soundFile : definition.soundFiles()) {
                sounds.add(soundFile.toString());
            }
            entry.add("sounds", sounds);

            if (definition.hasSubtitle()) {
                entry.addProperty("subtitle", definition.translationKey());
            }

            root.add(definition.id(), entry);
        }

        if (root.size() == 0) return CompletableFuture.completedFuture(null);

        Path path = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(namespace.id())
                .resolve("sounds.json");

        return DataProvider.saveStable(cache, root, path);
    }

    @Override
    public String getName() {
        return "Sounds: " + namespace.id();
    }
}
