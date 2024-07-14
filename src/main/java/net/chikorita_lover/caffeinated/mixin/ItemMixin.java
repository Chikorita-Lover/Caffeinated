package net.chikorita_lover.caffeinated.mixin;

import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedEntityTypeTags;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedItemTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "finishUsing", at = @At(value = "HEAD"))
    public void tryInflictCoffeePoison(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        if (stack.isIn(CaffeinatedItemTags.COFFEE_FOOD) && user.getType().isIn(CaffeinatedEntityTypeTags.COFFEE_INFLICTS_POISON)) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 900));
        }
    }
}
