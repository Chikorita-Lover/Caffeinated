package net.chikorita_lover.caffeinated.item;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.registry.CaffeinatedSoundEvents;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class CoffeeBottleItem extends Item {
    private static final int MAX_USE_TIME = 40;
    private static final String TRANSLATION_KEY = Util.createTranslationKey("item", Caffeinated.of("coffee_bottle"));

    public CoffeeBottleItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        super.finishUsing(stack, world, user);
        if (user instanceof ServerPlayerEntity serverPlayer) {
            Criteria.CONSUME_ITEM.trigger(serverPlayer, stack);
            serverPlayer.incrementStat(Stats.USED.getOrCreateStat(this));
        }
        if (!world.isClient) {
            user.removeStatusEffect(StatusEffects.SLOWNESS);
        }

        if (stack.isEmpty()) {
            return new ItemStack(Items.GLASS_BOTTLE);
        }
        if (user instanceof PlayerEntity player && !player.getAbilities().creativeMode) {
            ItemStack bottleStack = new ItemStack(Items.GLASS_BOTTLE);
            if (!player.getInventory().insertStack(bottleStack)) {
                player.dropItem(bottleStack, false);
            }
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
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
        return this.getDrinkSound();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        tooltip.add(Text.translatable(Util.createTranslationKey("item", Registries.ITEM.getId(this)).concat(".desc")).formatted(Formatting.GRAY));
    }

    public static float getEffectChance(StatusEffectInstance effect) {
        return getEffectChance(effect.getAmplifier());
    }

    public static float getEffectChance(int i) {
        return (i + 1) * 0.2F;
    }

    @Override
    public String getTranslationKey() {
        return TRANSLATION_KEY;
    }
}

