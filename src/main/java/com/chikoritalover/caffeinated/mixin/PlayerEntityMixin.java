package com.chikoritalover.caffeinated.mixin;

import com.chikoritalover.caffeinated.registry.ModStatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "addExhaustion", at = @At(value = "HEAD"), cancellable = true)
    private void addExhaustion(float exhaustion, CallbackInfo ci) {
        PlayerEntity player = PlayerEntity.class.cast(this);
        if (player.hasStatusEffect(ModStatusEffects.CAFFEINE) && !player.world.isClient() && !player.getAbilities().invulnerable) {
            player.getHungerManager().addExhaustion(exhaustion / 2);
            ci.cancel();
        }
    }
}
