package com.chikoritalover.caffeinated.block;

import com.chikoritalover.caffeinated.registry.ModBlocks;
import com.chikoritalover.caffeinated.registry.ModItems;
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
import net.minecraft.world.event.GameEvent;

public class CoffeeShrubBlock extends PlantBlock implements Fertilizable {
    public static final IntProperty AGE;
    private static final VoxelShape SMALL_SHAPE;
    private static final VoxelShape LARGE_SHAPE;

    public CoffeeShrubBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
    }

    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(ModItems.COFFEE_BEANS);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(AGE) < 2 ? SMALL_SHAPE : LARGE_SHAPE;
    }

    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int i = state.get(AGE);
        BlockState aboveState = world.getBlockState(pos.up());
        if ((i < 3 || aboveState.getMaterial().isReplaceable()) && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            grow(world, random, pos, state);
            BlockState state2 = world.getBlockState(pos);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state2));
        }

    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int i = state.get(AGE);
        BlockState aboveState = world.getBlockState(pos.up());
        boolean bl = i == 3 && !aboveState.getMaterial().isReplaceable();
        if (!bl && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
            return ActionResult.PASS;
        } else {
            return super.onUse(state, world, pos, player, hand, hit);
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        int i = state.get(AGE);
        BlockState aboveState = world.getBlockState(pos.up());
        return i < 3 || aboveState.getMaterial().isReplaceable();
    }

    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        BlockState state2 = state.get(AGE) < 3 ? state.cycle(AGE) : ModBlocks.FLOWERING_COFFEE_SHRUB.getDefaultState();
        world.setBlockState(pos, state2, 2);
        if (state.get(AGE) == 3) {
            world.setBlockState(pos.up(), state2.cycle(Properties.DOUBLE_BLOCK_HALF), 2);
        }
    }

    static {
        AGE = Properties.AGE_3;
        SMALL_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 11.0, 13.0);
        LARGE_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    }
}
