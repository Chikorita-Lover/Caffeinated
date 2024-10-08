package net.chikorita_lover.caffeinated.mixin;

import net.chikorita_lover.caffeinated.registry.CaffeinatedStatusEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "addExhaustion", at = @At(value = "HEAD"), cancellable = true)
    private void tryCancelExhaustion(float exhaustion, CallbackInfo ci) {
        if (this.hasStatusEffect(CaffeinatedStatusEffects.CAFFEINE)) {
            int amplifier = this.getStatusEffect(CaffeinatedStatusEffects.CAFFEINE).getAmplifier();
            if (this.getRandom().nextBetween(0, amplifier + 2) > 1) {
                ci.cancel();
            }
        }
    }
}
