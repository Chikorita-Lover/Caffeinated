package com.chikoritalover.caffeinated.block;

import com.chikoritalover.caffeinated.registry.CaffeinatedItems;
import com.chikoritalover.caffeinated.registry.CaffeinatedSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class FloweringCoffeeShrubBlock extends TallPlantBlock implements Fertilizable {
    public static final int MAX_AGE = 3;
    public static final IntProperty AGE = Properties.AGE_3;

    public FloweringCoffeeShrubBlock(Settings settings) {
        super(settings);
    }

    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(CaffeinatedItems.COFFEE_BEANS);
    }

    public boolean hasRandomTicks(BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER && state.get(AGE) < MAX_AGE;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int i = state.get(AGE);
        if (i < MAX_AGE && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            this.grow(world, random, pos, state);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int i = state.get(AGE);
        if (i < MAX_AGE && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
            return ActionResult.PASS;
        }
        if (i == MAX_AGE) {
            int j = world.getRandom().nextInt(2) + 2;
            dropStack(world, pos, new ItemStack(CaffeinatedItems.COFFEE_BERRIES, j));
            world.playSound(null, pos, CaffeinatedSoundEvents.BLOCK_COFFEE_SHRUB_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.4F);
            BlockState blockState = state.with(AGE, 0);
            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, blockState));
            if (state.get(HALF) == DoubleBlockHalf.LOWER) {
                world.setBlockState(pos.up(), blockState.cycle(HALF), Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos.up(), GameEvent.Emitter.of(player, blockState.cycle(HALF)));
            } else {
                world.setBlockState(pos.down(), blockState.cycle(HALF), Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos.down(), GameEvent.Emitter.of(player, blockState.cycle(HALF)));
            }
            return ActionResult.success(world.isClient);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        super.appendProperties(builder);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(AGE) < MAX_AGE;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        if (state.get(HALF) == DoubleBlockHalf.UPPER) {
            return this.canGrow(world, random, pos.down(), world.getBlockState(pos.down()));
        }
        return state.get(AGE) < MAX_AGE && this.canPlaceAt(state, world, pos);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (!this.canGrow(world, random, pos, state)) {
            return;
        }
        int i = Math.min(MAX_AGE, state.get(AGE) + 1);
        world.setBlockState(pos, state.with(AGE, i), Block.NOTIFY_LISTENERS);
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            world.setBlockState(pos.up(), state.with(AGE, i).cycle(HALF), Block.NOTIFY_ALL);
        } else {
            world.setBlockState(pos.down(), state.with(AGE, i).cycle(HALF), Block.NOTIFY_ALL);
        }
    }
}
