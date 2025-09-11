package net.chikorita_lover.caffeinated.block;

import com.mojang.serialization.MapCodec;
import net.chikorita_lover.caffeinated.registry.CaffeinatedFoodComponents;
import net.chikorita_lover.caffeinated.registry.CaffeinatedStats;
import net.minecraft.block.*;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
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
    public static final MapCodec<TiramisuBlock> CODEC = createCodec(TiramisuBlock::new);
    private static final VoxelShape ONE_SLICE_SHAPE = Block.createCuboidShape(2, 0, 8, 8, 10, 14);
    private static final VoxelShape TWO_SLICES_SHAPE = VoxelShapes.union(ONE_SLICE_SHAPE, Block.createCuboidShape(8, 0, 8, 14, 10, 14));
    private static final VoxelShape THREE_SLICES_SHAPE = VoxelShapes.union(TWO_SLICES_SHAPE, Block.createCuboidShape(8, 0, 2, 14, 10, 8));
    private static final VoxelShape FOUR_SLICES_SHAPE = VoxelShapes.union(THREE_SLICES_SHAPE, Block.createCuboidShape(2, 0, 2, 8, 10, 8));
    private static final VoxelShape[] SLICES_TO_SHAPE = new VoxelShape[]{ONE_SLICE_SHAPE, TWO_SLICES_SHAPE, THREE_SLICES_SHAPE, FOUR_SLICES_SHAPE};

    public TiramisuBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(SLICES, 4));
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SLICES_TO_SHAPE[state.get(SLICES) - 1];
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, final PlayerEntity player, BlockHitResult hit) {
        player.incrementStat(CaffeinatedStats.EAT_TIRAMISU_SLICE);
        FoodComponent food = CaffeinatedFoodComponents.TIRAMISU_SLICE;
        player.getHungerManager().add(food.nutrition(), food.nutrition() * food.saturation() * 2);
        food.effects().forEach(effect -> player.addStatusEffect(effect.effect()));
        world.emitGameEvent(player, GameEvent.EAT, pos);
        int slices = state.get(SLICES);
        if (slices > 1) {
            world.setBlockState(pos, state.with(SLICES, slices - 1), Block.NOTIFY_ALL);
        } else {
            world.removeBlock(pos, false);
            world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
        }
        return ActionResult.SUCCESS;
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
        return state.get(SLICES) * 4 - 1;
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
