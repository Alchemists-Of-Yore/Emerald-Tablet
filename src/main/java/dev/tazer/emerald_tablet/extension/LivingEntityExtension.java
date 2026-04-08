package dev.tazer.emerald_tablet.extension;

public interface LivingEntityExtension {

    default void emeraldTablet$setWillDropDeathLoot(boolean willDrop) {
        throw new AssertionError("Implemented in Mixins!");
    }

    default boolean emeraldTablet$willDropDeathLoot() {
        throw new AssertionError("Implemented in Mixins!");
    }
}
