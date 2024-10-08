package net.chikorita_lover.caffeinated.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.chikorita_lover.caffeinated.item.CoffeeBottleItem;
import net.chikorita_lover.caffeinated.registry.CaffeinatedStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;addExhaustion(F)V"))
    private float modifyExhaustion(float exhaustion, @Local(argsOnly = true) PlayerEntity player) {
        if (player.hasStatusEffect(CaffeinatedStatusEffects.CAFFEINE)) {
            StatusEffectInstance effect = player.getStatusEffect(CaffeinatedStatusEffects.CAFFEINE);
            exhaustion *= CoffeeBottleItem.getEffectMultiplier(effect);
        }
        return exhaustion;
    }
}
