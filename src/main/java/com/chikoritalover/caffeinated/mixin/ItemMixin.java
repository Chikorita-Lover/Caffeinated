package com.chikoritalover.caffeinated.mixin;

import com.chikoritalover.caffeinated.registry.CaffeinatedEntityTypeTags;
import com.chikoritalover.caffeinated.registry.CaffeinatedItemTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow
    public abstract Item asItem();

    @Inject(method = "getRecipeRemainder", at = @At("RETURN"), cancellable = true)
    public void getRecipeRemainder(CallbackInfoReturnable<Item> cir) {
        if (this.asItem() == Items.POTION) {
            cir.setReturnValue(Items.GLASS_BOTTLE);
        }
    }

    @Inject(method = "hasRecipeRemainder", at = @At("RETURN"), cancellable = true)
    public void hasRecipeRemainder(CallbackInfoReturnable<Boolean> cir) {
        if (this.asItem() == Items.POTION) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "finishUsing", at = @At(value = "HEAD"))
    public void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info) {
        if (stack.isIn(CaffeinatedItemTags.COFFEE_FOOD) && user.getType().isIn(CaffeinatedEntityTypeTags.COFFEE_INFLICTS_POISON)) {
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 900));
        }
    }
}
