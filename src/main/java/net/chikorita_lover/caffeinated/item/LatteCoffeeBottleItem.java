package net.chikorita_lover.caffeinated.item;

import net.chikorita_lover.caffeinated.registry.CaffeinatedStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

public class LatteCoffeeBottleItem extends CoffeeBottleItem {
    public LatteCoffeeBottleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        stack = super.finishUsing(stack, world, user);
        if (!world.isClient()) {
            ArrayList<StatusEffectInstance> statusEffects = new ArrayList<>(user.getStatusEffects());
            user.clearStatusEffects();
            for (StatusEffectInstance effect : statusEffects) {
                if (effect.getEffectType() == CaffeinatedStatusEffects.CAFFEINE) {
                    user.addStatusEffect(effect);
                } else if (effect.getAmplifier() > 0) {
                    user.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), effect.getDuration(), effect.getAmplifier() - 1, effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon()));
                }
            }
        }
        return stack;
    }
}
