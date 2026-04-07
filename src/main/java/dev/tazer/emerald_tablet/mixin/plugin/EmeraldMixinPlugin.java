package dev.tazer.emerald_tablet.mixin.plugin;

import dev.tazer.emerald_tablet.mixin.annotation.IfAnyModPresent;
import dev.tazer.emerald_tablet.mixin.annotation.IfDevEnvironment;
import dev.tazer.emerald_tablet.mixin.annotation.IfModAbsent;
import dev.tazer.emerald_tablet.mixin.annotation.IfModPresent;
import dev.tazer.emerald_tablet.mixin.annotation.IfNotDevEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.moddiscovery.ModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class EmeraldMixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LogManager.getLogger("Emerald Tablet Mixin Plugin");
    private static final boolean IS_DEV = !FMLLoader.isProduction();

    private String mixinPackage;
    private Set<String> presentMods;
    private final Map<String, List<String>> mixinTargetTracker = new HashMap<>();

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage;
        this.presentMods = Set.copyOf(
                FMLLoader.getLoadingModList().getMods().stream()
                        .map(ModInfo::getModId)
                        .toList()
        );
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        try {
            Class<?> mixinClass = Class.forName(mixinClassName, false, getClass().getClassLoader());
            return evaluateConditions(mixinClass, mixinClassName);
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Could not load mixin class for condition evaluation: {}", mixinClassName);
            return true;
        }
    }

    private boolean evaluateConditions(Class<?> mixinClass, String mixinClassName) {
        boolean hasDevAnnotation = mixinClass.isAnnotationPresent(IfDevEnvironment.class);
        boolean hasNotDevAnnotation = mixinClass.isAnnotationPresent(IfNotDevEnvironment.class);

        if (hasDevAnnotation && hasNotDevAnnotation) {
            LOGGER.warn("Mixin {} has both @IfDevEnvironment and @IfNotDevEnvironment. It will never load!", mixinClassName);
            return false;
        }

        if (hasDevAnnotation && !IS_DEV) {
            LOGGER.debug("Skipping dev-only mixin: {}", mixinClassName);
            return false;
        }

        if (hasNotDevAnnotation && IS_DEV) {
            LOGGER.debug("Skipping production-only mixin: {}", mixinClassName);
            return false;
        }

        for (IfModPresent annotation : mixinClass.getAnnotationsByType(IfModPresent.class)) {
            if (!presentMods.contains(annotation.value())) {
                LOGGER.debug("Skipping mixin {}; mod '{}' is not present", mixinClassName, annotation.value());
                return false;
            }
        }

        for (IfModAbsent annotation : mixinClass.getAnnotationsByType(IfModAbsent.class)) {
            if (presentMods.contains(annotation.value())) {
                LOGGER.debug("Skipping mixin {}; mod '{}' is not absent", mixinClassName, annotation.value());
                return false;
            }
        }

        IfAnyModPresent anyModPresent = mixinClass.getAnnotation(IfAnyModPresent.class);
        if (anyModPresent != null) {
            boolean anyPresent = false;
            for (String modId : anyModPresent.value()) {
                if (presentMods.contains(modId)) {
                    anyPresent = true;
                    break;
                }
            }
            if (!anyPresent) {
                LOGGER.debug("Skipping mixin {}; none of {} are present", mixinClassName, List.of(anyModPresent.value()));
                return false;
            }
        }

        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        List<String> discovered = new ArrayList<>();

        String packagePath = mixinPackage.replace('.', '/');
        URL packageUrl = getClass().getClassLoader().getResource(packagePath);
        if (packageUrl == null) return discovered;

        try {
            Path root = Path.of(packageUrl.toURI());
            try (Stream<Path> walker = Files.walk(root)) {
                walker.filter(p -> p.toString().endsWith(".class"))
                        .forEach(p -> {
                            String relative = root.relativize(p).toString();
                            String className = relative.replace(File.separatorChar, '.').replace('/', '.');
                            if (className.endsWith(".class")) {
                                className = className.substring(0, className.length() - 6);
                            }
                            if (!className.startsWith("plugin.") && !className.startsWith("annotation.")) {
                                discovered.add(className);
                            }
                        });
            }
        } catch (URISyntaxException | IOException e) {
            LOGGER.warn("Failed to discover mixins in package {}", mixinPackage, e);
        }

        if (!discovered.isEmpty()) {
            LOGGER.debug("Auto-discovered {} mixins in {}", discovered.size(), mixinPackage);
        }

        return discovered;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        trackMixin(targetClassName, mixinClassName);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    private void trackMixin(String targetClassName, String mixinClassName) {
        List<String> mixins = mixinTargetTracker.computeIfAbsent(targetClassName, k -> new ArrayList<>());
        mixins.add(mixinClassName);

        if (mixins.size() > 1 && IS_DEV) {
            LOGGER.debug("Multiple mixins targeting {}: {}", targetClassName, mixins);
        }
    }
}
