package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.block.CoffeeCauldronBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

public class CaffeinatedCauldronBehavior {
    public static final CauldronBehavior.CauldronBehaviorMap COFFEE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap("caffeinated_coffee_cauldron");
    public static final CauldronBehavior.CauldronBehaviorMap GROUND_COFFEE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap("caffeinated_ground_coffee_cauldron");

    public static void register() {
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(CaffeinatedItems.COFFEE_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                world.setBlockState(pos, CaffeinatedBlocks.COFFEE_CAULDRON.getDefaultState());
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ItemActionResult.success(world.isClient());
        });
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().put(CaffeinatedItems.GROUND_COFFEE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                Item item = stack.getItem();
                if (!player.isCreative()) {
                    stack.decrement(1);
                }
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Blocks.AIR)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.setBlockState(pos, CaffeinatedBlocks.GROUND_COFFEE_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, state.get(LeveledCauldronBlock.LEVEL)));
                world.playSound(null, pos, CaffeinatedSoundEvents.ITEM_GROUND_COFFEE_SPLASH, SoundCategory.BLOCKS);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }

            return ItemActionResult.success(world.isClient());
        });
        COFFEE_CAULDRON_BEHAVIOR.map().put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(CaffeinatedItems.COFFEE_BOTTLE)));
                player.incrementStat(CaffeinatedStats.COFFEE_TAKEN);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
                if (state.get(CoffeeCauldronBlock.REWARD_EXPERIENCE)) {
                    ExperienceOrbEntity.spawn((ServerWorld) world, Vec3d.ofCenter(pos), world.getRandom().nextBetween(1, 3));
                    world.setBlockState(pos, world.getBlockState(pos).cycle(CoffeeCauldronBlock.REWARD_EXPERIENCE));
                }
            }
            return ItemActionResult.success(world.isClient());
        });
        COFFEE_CAULDRON_BEHAVIOR.map().put(CaffeinatedItems.COFFEE_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (state.get(LeveledCauldronBlock.LEVEL) == 3) {
                return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            if (!world.isClient()) {
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                world.setBlockState(pos, state.cycle(LeveledCauldronBlock.LEVEL));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ItemActionResult.success(world.isClient());
        });
        GROUND_COFFEE_CAULDRON_BEHAVIOR.map().put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionContentsComponent.createStack(Items.POTION, Potions.WATER)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ItemActionResult.success(world.isClient());
        });
        GROUND_COFFEE_CAULDRON_BEHAVIOR.map().put(Items.BUCKET, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.WATER_BUCKET)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.setBlockState(pos, Blocks.CAULDRON.getDefaultState());
                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ItemActionResult.success(world.isClient());
        });
        GROUND_COFFEE_CAULDRON_BEHAVIOR.map().put(Items.POTION, (state, world, pos, player, hand, stack) -> {
            if (state.get(LeveledCauldronBlock.LEVEL) == 3 || !stack.contains(DataComponentTypes.POTION_CONTENTS) || !stack.get(DataComponentTypes.POTION_CONTENTS).matches(Potions.WATER)) {
                return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            if (!world.isClient()) {
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                world.setBlockState(pos, state.cycle(LeveledCauldronBlock.LEVEL));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ItemActionResult.success(world.isClient());
        });
    }
}
