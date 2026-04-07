package dev.tazer.emerald_tablet;

import dev.tazer.emerald_tablet.registry.Namespace;
import dev.tazer.emerald_tablet.registry.TabletBlocks;
import dev.tazer.emerald_tablet.registry.definition.builtin.BuiltInDefinition;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mod(EmeraldTablet.MODID)
public class EmeraldTablet {
    public static final String MODID = "emerald_tablet";
    public static final Logger LOGGER = LogManager.getLogger("Emerald Tablet");
    private static final Map<String, Namespace> NAMESPACES = new LinkedHashMap<>();
    public static final Namespace EMERALD_TABLET = EmeraldTablet.namespace(EmeraldTablet.MODID);

    public static Namespace namespace(String id) {
        return NAMESPACES.computeIfAbsent(id, Namespace::new);
    }

    public static List<Namespace> namespaces() {
        return List.copyOf(NAMESPACES.values());
    }

    public EmeraldTablet(IEventBus bus, ModContainer container, Dist dist) {
        TabletBlocks.register();
        bus.addListener(EmeraldTablet::register);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private static void register(RegisterEvent event) {
        namespaces().forEach(namespace ->
                namespace.getBuiltInDefinitions().forEach(definition ->
                        registerDefinition(event, namespace, definition)
                )
        );
    }

    private static <T extends B, B> void registerDefinition(RegisterEvent event, Namespace namespace,
                                                              BuiltInDefinition<T, B> definition) {
        ResourceLocation location = namespace.id(definition.id());
        event.register(definition.registry(), location, definition::get);
        definition.aliases().forEach(alias -> event.getRegistry().addAlias(location, namespace.id(alias)));
        definition.bind();
    }
}
