package net.chikorita_lover.caffeinated.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CafeMielBottleItem extends CoffeeBottleItem {
    public CafeMielBottleItem(Settings settings) {
        super(settings, false);
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
