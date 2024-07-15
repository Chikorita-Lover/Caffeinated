package net.chikorita_lover.caffeinated.block;

import net.chikorita_lover.caffeinated.registry.CaffeinatedFoodComponents;
import net.chikorita_lover.caffeinated.registry.CaffeinatedStats;
import net.chikorita_lover.caffeinated.registry.CaffeinatedStatusEffects;
import net.minecraft.block.*;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class TiramisuBlock extends Block {
    public static final IntProperty SLICES = IntProperty.of("slices", 1, 4);
    protected static final VoxelShape[] SLICE_TO_SHAPE = new VoxelShape[]{Block.createCuboidShape(2, 0, 8, 8, 10, 14), Block.createCuboidShape(8, 0, 8, 14, 10, 14), Block.createCuboidShape(8, 0, 2, 14, 10, 8), Block.createCuboidShape(2, 0, 2, 8, 10, 8)};

    public TiramisuBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(SLICES, 4));
    }

    protected static ActionResult tryEat(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.canConsume(false)) {
            return ActionResult.PASS;
        } else {
            player.incrementStat(CaffeinatedStats.EAT_TIRAMISU_SLICE);
            player.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0F, 1.0F);
            FoodComponent food = CaffeinatedFoodComponents.TIRAMISU_SLICE;
            player.getHungerManager().add(food.nutrition(), food.nutrition() * food.saturation() * 2);
            player.addStatusEffect(new StatusEffectInstance(CaffeinatedStatusEffects.CAFFEINE, 600, 1));
            int i = state.get(SLICES);
            world.emitGameEvent(player, GameEvent.EAT, pos);
            if (i > 1) {
                world.setBlockState(pos, state.with(SLICES, i - 1), 3);
            } else {
                world.removeBlock(pos, false);
                world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }
            return ActionResult.SUCCESS;
        }
    }

    public static int getComparatorOutput(int bites) {
        return bites * 4 - 1;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape[] voxelShapes = new VoxelShape[state.get(SLICES) - 1];
        System.arraycopy(SLICE_TO_SHAPE, 1, voxelShapes, 0, state.get(SLICES) - 1);
        return VoxelShapes.union(SLICE_TO_SHAPE[0], voxelShapes);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient()) {
            if (tryEat(world, pos, state, player).isAccepted()) {
                return ActionResult.SUCCESS;
            }
        }
        return tryEat(world, pos, state, player);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSolid();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SLICES);
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return getComparatorOutput(state.get(SLICES));
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
