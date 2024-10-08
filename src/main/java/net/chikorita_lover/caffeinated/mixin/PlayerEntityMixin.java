package net.chikorita_lover.caffeinated.mixin;

import net.chikorita_lover.caffeinated.item.CoffeeBottleItem;
import net.chikorita_lover.caffeinated.registry.CaffeinatedStatusEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "addExhaustion", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private float modifyExhaustion(float exhaustion) {
        if (this.hasStatusEffect(CaffeinatedStatusEffects.CAFFEINE)) {
            StatusEffectInstance effect = this.getStatusEffect(CaffeinatedStatusEffects.CAFFEINE);
            exhaustion *= CoffeeBottleItem.getEffectMultiplier(effect);
        }
        return exhaustion;
    }
}
