package com.chikoritalover.caffeinated.item;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.registry.CaffeinatedSoundEvents;
import com.chikoritalover.caffeinated.registry.CaffeinatedStatusEffects;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            buildTooltip(list, tooltip, 1.0F);
        }
    }

    private static void buildTooltip(List<StatusEffectInstance> statusEffects, List<Text> list, float durationMultiplier) {
        ArrayList<Pair<EntityAttribute, EntityAttributeModifier>> list2 = Lists.newArrayList();
        int i = 0;
        for (StatusEffectInstance statusEffectInstance : statusEffects) {
            MutableText mutableText = Text.translatable(statusEffectInstance.getTranslationKey());
            StatusEffect statusEffect = statusEffectInstance.getEffectType();
            Map<EntityAttribute, EntityAttributeModifier> map = statusEffect.getAttributeModifiers();
            if (!map.isEmpty()) {
                for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : map.entrySet()) {
                    EntityAttributeModifier entityAttributeModifier = entry.getValue();
                    EntityAttributeModifier entityAttributeModifier2 = new EntityAttributeModifier(entityAttributeModifier.getName(), statusEffect.adjustModifierAmount(statusEffectInstance.getAmplifier(), entityAttributeModifier), entityAttributeModifier.getOperation());
                    list2.add(new Pair<>(entry.getKey(), entityAttributeModifier2));
                }
            }
            if (statusEffectInstance.getAmplifier() > 0) {
                mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + statusEffectInstance.getAmplifier()));
                if (statusEffectInstance.getEffectType() == CaffeinatedStatusEffects.CAFFEINE) {
                    i = statusEffectInstance.getAmplifier();
                }
            }
            if (!statusEffectInstance.isDurationBelow(20)) {
                mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.getDurationText(statusEffectInstance, durationMultiplier));
            }
            list.add(mutableText.formatted(statusEffect.getCategory().getFormatting()));
        }
        list.add(ScreenTexts.EMPTY);
        list.add(Text.translatable("potion.whenDrank").formatted(Formatting.DARK_PURPLE));
        list.add(Text.translatable("attribute.modifier.take.2", ItemStack.MODIFIER_FORMAT.format(getEffectChance(i) * 100.0F), Text.translatable("item.modifiers.exhaustion")).formatted(Formatting.BLUE));
        for (Pair<EntityAttribute, EntityAttributeModifier> pair : list2) {
            EntityAttributeModifier entityAttributeModifier3 = pair.getSecond();
            double d = entityAttributeModifier3.getValue();
            double e = entityAttributeModifier3.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_BASE || entityAttributeModifier3.getOperation() == EntityAttributeModifier.Operation.MULTIPLY_TOTAL ? entityAttributeModifier3.getValue() * 100.0 : entityAttributeModifier3.getValue();
            if (d > 0.0) {
                list.add(Text.translatable("attribute.modifier.plus." + entityAttributeModifier3.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(e), Text.translatable(pair.getFirst().getTranslationKey())).formatted(Formatting.BLUE));
                continue;
            }
            if (!(d < 0.0)) continue;
            list.add(Text.translatable("attribute.modifier.take." + entityAttributeModifier3.getOperation().getId(), ItemStack.MODIFIER_FORMAT.format(-e), Text.translatable(pair.getFirst().getTranslationKey())).formatted(Formatting.RED));
        }
    }

    public static float getEffectChance(StatusEffectInstance statusEffectInstance) {
        return getEffectChance(statusEffectInstance.getAmplifier());
    }

    public static float getEffectChance(int i) {
        return (i + 1) * 0.2F;
    }

    @Override
    public String getTranslationKey() {
        return TRANSLATION_KEY;
    }
}

