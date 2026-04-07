package dev.tazer.emerald_tablet.mixin;

import dev.tazer.emerald_tablet.extension.LivingEntityExtension;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements LivingEntityExtension {

    @Unique
    private boolean emeraldTablet$willDropDeathLoot = true;

    @Override
    public void emeraldTablet$setWillDropDeathLoot(boolean willDrop) {
        this.emeraldTablet$willDropDeathLoot = willDrop;
    }

    @Override
    public boolean emeraldTablet$willDropDeathLoot() {
        return this.emeraldTablet$willDropDeathLoot;
    }

    @Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
    private void emeraldTablet$onDropAllDeathLoot(ServerLevel level, DamageSource damageSource, CallbackInfo ci) {
        if (!emeraldTablet$willDropDeathLoot) ci.cancel();
    }
}
