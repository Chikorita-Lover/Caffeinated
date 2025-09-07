package net.chikorita_lover.caffeinated.entity.effect;

import net.chikorita_lover.caffeinated.registry.CaffeinatedStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;

public class CaffeineStatusEffect extends StatusEffect {
    public CaffeineStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    public static float getExhaustionMultiplier(LivingEntity entity) {
        StatusEffectInstance effect = entity.getStatusEffect(CaffeinatedStatusEffects.CAFFEINE);
        if (effect == null) {
            return 1.0F;
        }
        int level = effect.getAmplifier() + 1;
        return 1.0F - level * 0.2F;
    }
}
