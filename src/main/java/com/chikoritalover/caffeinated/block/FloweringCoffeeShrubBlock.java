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
    public static final IntProperty AGE = Properties.AGE_3;

    public FloweringCoffeeShrubBlock(Settings settings) {
        super(settings);
    }

    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(CaffeinatedItems.COFFEE_BEANS);
    }

    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int i = state.get(AGE);
        if (i < 3 && random.nextInt(11) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            grow(world, random, pos, state);
            BlockState blockState = world.getBlockState(pos);

            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
            if (blockState.get(HALF) == DoubleBlockHalf.LOWER) {
                world.setBlockState(pos.up(), blockState.cycle(HALF), 2);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos.up(), GameEvent.Emitter.of(blockState.cycle(HALF)));
            } else {
                world.setBlockState(pos.down(), blockState.cycle(HALF), 2);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos.down(), GameEvent.Emitter.of(blockState.cycle(HALF)));
            }
        }

    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int i = state.get(AGE);
        boolean bl = i == 3;
        if (!bl && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
            return ActionResult.PASS;
        } else if (i == 3) {
            int j = 1 + world.random.nextInt(2);
            dropStack(world, pos, new ItemStack(CaffeinatedItems.COFFEE_BERRIES, j + (bl ? 1 : 0)));
            world.playSound(null, pos, CaffeinatedSoundEvents.BLOCK_COFFEE_SHRUB_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
            BlockState blockState = state.with(AGE, 0);

            world.setBlockState(pos, blockState, 2);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, blockState));
            if (state.get(HALF) == DoubleBlockHalf.LOWER) {
                world.setBlockState(pos.up(), blockState.cycle(HALF), 2);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos.up(), GameEvent.Emitter.of(player, blockState.cycle(HALF)));
            } else {
                world.setBlockState(pos.down(), blockState.cycle(HALF), 2);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos.down(), GameEvent.Emitter.of(player, blockState.cycle(HALF)));
            }

            return ActionResult.success(world.isClient);
        } else {
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        super.appendProperties(builder);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return state.get(AGE) < 3;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int i = Math.min(3, state.get(AGE) + 1);
        world.setBlockState(pos, state.with(AGE, i), 2);
        if (state.get(HALF) == DoubleBlockHalf.LOWER) {
            world.setBlockState(pos.up(), state.with(AGE, i).cycle(HALF), 2);
        } else {
            world.setBlockState(pos.down(), state.with(AGE, i).cycle(HALF), 2);
        }
    }
}
