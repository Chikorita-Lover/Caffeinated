package net.chikorita_lover.caffeinated.block;

import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedItems;
import net.chikorita_lover.caffeinated.registry.CaffeinatedParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

public class CoffeeCauldronBlock extends LeveledCauldronBlock implements HeatableCauldron {
    private static final CauldronBehavior.CauldronBehaviorMap COFFEE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap("coffee");

    public CoffeeCauldronBlock(Settings settings) {
        super(null, COFFEE_CAULDRON_BEHAVIOR, settings);
    }

    public static void registerBehavior() {
        Map<Item, CauldronBehavior> map = COFFEE_CAULDRON_BEHAVIOR.map();
        CauldronBehavior.registerBucketBehavior(map);
        map.put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(CaffeinatedItems.COFFEE_BOTTLE)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                decrementFluidLevel(state, world, pos);
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
            }
            return ItemActionResult.success(world.isClient());
        });
        map.put(CaffeinatedItems.COFFEE_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (state.get(LeveledCauldronBlock.LEVEL) == 3) {
                return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            if (!world.isClient()) {
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                world.setBlockState(pos, state.cycle(LeveledCauldronBlock.LEVEL));
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ItemActionResult.success(world.isClient);
        });
        CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(CaffeinatedItems.COFFEE_BOTTLE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                Item item = stack.getItem();
                player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
                player.incrementStat(Stats.USE_CAULDRON);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.setBlockState(pos, CaffeinatedBlocks.COFFEE_CAULDRON.getDefaultState());
                world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
            return ItemActionResult.success(world.isClient());
        });
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(Blocks.CAULDRON);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        this.randomCauldronEffects(state, world, pos, random);
    }

    @Override
    public ParticleEffect getBubbleEffect() {
        return CaffeinatedParticleTypes.COFFEE_POP;
    }
}
