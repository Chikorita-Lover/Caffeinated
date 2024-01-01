package com.chikoritalover.caffeinated.item;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.registry.CaffeinatedSoundEvents;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CoffeeBottleItem extends Item {
    private static final int MAX_USE_TIME = 40;
    private static final String TRANSLATION_KEY = Util.createTranslationKey("item", new Identifier(Caffeinated.MODID, "coffee_bottle"));

    public CoffeeBottleItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (!world.isClient) {
            user.removeStatusEffect(StatusEffects.SLOWNESS);
        }
        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        if (user instanceof PlayerEntity playerEntity && !((PlayerEntity)user).getAbilities().creativeMode) {
            ItemStack itemStack = new ItemStack(Items.GLASS_BOTTLE);
            if (!playerEntity.getInventory().insertStack(itemStack)) {
                playerEntity.dropItem(itemStack, false);
            }
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return MAX_USE_TIME;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getDrinkSound() {
        return CaffeinatedSoundEvents.ITEM_COFFEE_BOTTLE_DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return CaffeinatedSoundEvents.ITEM_COFFEE_BOTTLE_DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable(Util.createTranslationKey("item", Registries.ITEM.getId(this)).concat(".desc")).formatted(Formatting.GRAY));
        List<StatusEffectInstance> list = Lists.newArrayList();
        for (Pair<StatusEffectInstance, Float> statusEffect : this.getFoodComponent().getStatusEffects()) {
            list.add(statusEffect.getFirst());
        }
        if (!list.isEmpty()) {
            PotionUtil.buildTooltip(list, tooltip, 1.0F);
        }
    }

    @Override
    public String getTranslationKey() {
        return TRANSLATION_KEY;
    }
}

