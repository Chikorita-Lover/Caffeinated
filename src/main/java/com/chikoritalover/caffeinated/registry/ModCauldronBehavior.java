package com.chikoritalover.caffeinated.registry;

import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

public class ModCauldronBehavior {
    public static final Map<Item, CauldronBehavior> COFFEE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();
    public static final Map<Item, CauldronBehavior> GROUND_COFFEE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap();

    public static void register() {
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.put(ModItems.COFFEE_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (stack.getItem() != ModItems.COFFEE_BOTTLE) {
                return ActionResult.PASS;
            } else {
                if (!world.isClient) {
                    Item item = stack.getItem();
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.incrementStat(Stats.USED.getOrCreateStat(item));
                    world.setBlockState(pos, ModBlocks.COFFEE_CAULDRON.getDefaultState());
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                }

                return ActionResult.success(world.isClient);
            }
        });

        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(ModItems.GROUND_COFFEE, (state, world, pos, player, hand, stack) -> {
            if (stack.getItem() != ModItems.GROUND_COFFEE) {
                return ActionResult.PASS;
            } else {
                IntProperty level3 = Properties.LEVEL_3;

                if (!world.isClient) {
                    Item item = stack.getItem();
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Blocks.AIR)));
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.incrementStat(Stats.USED.getOrCreateStat(item));
                    world.setBlockState(pos, ModBlocks.GROUND_COFFEE_CAULDRON.getDefaultState().with(level3, state.get(level3)));
                    world.playSound(null, pos, ModSoundEvents.ITEM_GROUND_COFFEE_SPLASH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                }

                return ActionResult.success(world.isClient);
            }
        });

        COFFEE_CAULDRON_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(ModItems.COFFEE_BOTTLE)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }

            return ActionResult.success(world.isClient);
        });
        COFFEE_CAULDRON_BEHAVIOR.put(ModItems.COFFEE_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (state.get(LeveledCauldronBlock.LEVEL) != 3 && stack.getItem() == ModItems.COFFEE_BOTTLE) {
                if (!world.isClient) {
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                    world.setBlockState(pos, state.cycle(LeveledCauldronBlock.LEVEL));
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                }

                return ActionResult.success(world.isClient);
            } else {
                return ActionResult.PASS;
            }
        });

        GROUND_COFFEE_CAULDRON_BEHAVIOR.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }

            return ActionResult.success(world.isClient);
        });
        GROUND_COFFEE_CAULDRON_BEHAVIOR.put(Items.BUCKET, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.WATER_BUCKET)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }

            return ActionResult.success(world.isClient);
        });
        GROUND_COFFEE_CAULDRON_BEHAVIOR.put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (state.get(LeveledCauldronBlock.LEVEL) != 3 && PotionUtil.getPotion(stack) == Potions.WATER) {
                if (!world.isClient) {
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                    player.incrementStat(Stats.USE_CAULDRON);
                    player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                    world.setBlockState(pos, state.cycle(LeveledCauldronBlock.LEVEL));
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                }

                return ActionResult.success(world.isClient);
            } else {
                return ActionResult.PASS;
            }
        });
    }
}
