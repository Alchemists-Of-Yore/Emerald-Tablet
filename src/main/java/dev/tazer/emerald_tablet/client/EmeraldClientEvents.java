package dev.tazer.emerald_tablet.client;

import dev.tazer.emerald_tablet.EmeraldTablet;
import dev.tazer.emerald_tablet.registry.definition.builtin.FluidTypeDefinition;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = EmeraldTablet.MODID)
public class EmeraldClientEvents {

    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        EmeraldTablet.namespaces().forEach(namespace -> {
            for (FluidTypeDefinition definition : namespace.getDefinitions(FluidTypeDefinition.class)) {
                ResourceLocation still = definition.stillTexture();
                ResourceLocation flowing = definition.flowingTexture();
                int tint = definition.tintColor();

                if (still != null || flowing != null) {
                    event.registerFluidType(new IClientFluidTypeExtensions() {
                        @Override
                        public ResourceLocation getStillTexture() {
                            return still != null ? still : flowing;
                        }

                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return flowing != null ? flowing : still;
                        }

                        @Override
                        public int getTintColor() {
                            return tint != -1 ? tint : 0xFFFFFFFF;
                        }
                    }, definition.get());
                }
            }
        });
    }
}
