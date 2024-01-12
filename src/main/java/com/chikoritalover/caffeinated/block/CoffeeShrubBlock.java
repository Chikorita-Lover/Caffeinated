package com.chikoritalover.caffeinated.block;

import com.chikoritalover.caffeinated.registry.CaffeinatedBlocks;
import com.chikoritalover.caffeinated.registry.CaffeinatedItems;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class CoffeeShrubBlock extends PlantBlock implements Fertilizable {
    public static final IntProperty AGE = Properties.AGE_3;
    private static final VoxelShape SMALL_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 11.0, 13.0);
    private static final VoxelShape LARGE_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public CoffeeShrubBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(AGE, 0));
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(CaffeinatedItems.COFFEE_BEANS);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(AGE) < 2 ? SMALL_SHAPE : LARGE_SHAPE;
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!this.canGrow(world, random, pos, state)) {
            return;
        }
        if (random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            this.grow(world, random, pos, state);
            BlockState blockState = world.getBlockState(pos);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        boolean bl = this.isFertilizable(world, pos, state, world.isClient());
        if (bl && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
            return ActionResult.PASS;
        } else {
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return state.get(AGE) < 3 || world.isAir(pos.up());
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return state.get(AGE) < 3 || world.isAir(pos.up());
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.get(AGE) < 3) {
            world.setBlockState(pos, state.with(AGE, state.get(AGE) + 1), Block.NOTIFY_LISTENERS);
            return;
        }
        BlockState blockState = CaffeinatedBlocks.FLOWERING_COFFEE_SHRUB.getDefaultState();
        if (blockState.canPlaceAt(world, pos) && world.isAir(pos.up())) {
            FloweringCoffeeShrubBlock.placeAt(world, blockState, pos, Block.NOTIFY_LISTENERS);
        }
    }
}
