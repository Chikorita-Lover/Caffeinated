package net.chikorita_lover.caffeinated.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.chikorita_lover.caffeinated.entity.effect.CaffeineStatusEffect;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;addExhaustion(F)V"))
    private float modifyExhaustion(float exhaustion, @Local(argsOnly = true) PlayerEntity player) {
        return exhaustion * CaffeineStatusEffect.getExhaustionMultiplier(player);
    }
}
