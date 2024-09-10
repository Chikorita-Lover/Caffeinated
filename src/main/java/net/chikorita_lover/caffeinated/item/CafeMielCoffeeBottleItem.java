package net.chikorita_lover.caffeinated.item;

import net.chikorita_lover.caffeinated.registry.CaffeinatedStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;

public class CafeMielCoffeeBottleItem extends CoffeeBottleItem {
    public CafeMielCoffeeBottleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        stack = super.finishUsing(stack, world, user);
        if (!world.isClient()) {
            user.removeStatusEffect(StatusEffects.POISON);
        }
        return stack;
    }
}
