package com.chikoritalover.caffeinated.block;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.block.entity.CauldronCampfireBlockEntity;
import com.chikoritalover.caffeinated.registry.CaffeinatedBlockTags;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CauldronCampfireBlock extends BlockWithEntity implements Waterloggable {
    protected static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0.5, 0.0, 0.5, 15.5, 16.0, 15.5);
    protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0, 16.0), Block.createCuboidShape(2.0, 5.0, 2.0, 14.0, 16.0, 14.0), BooleanBiFunction.OR);
    public static final BooleanProperty LIT = Properties.LIT;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final Map<Block, Block> CAMPFIRE_TO_CAULDRON_CAMPFIRE = Maps.newHashMap();
    private final Block baseBlock;
    private final ParticleEffect particleEffect;

    public CauldronCampfireBlock(Block baseBlock, ParticleEffect particleEffect, Settings settings) {
        super(settings);
        this.baseBlock = baseBlock;
        this.particleEffect = particleEffect;
        CAMPFIRE_TO_CAULDRON_CAMPFIRE.put(baseBlock, this);
        this.setDefaultState(this.getStateManager().getDefaultState().with(LIT, true).with(WATERLOGGED, false).with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE;
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        List<ItemStack> stacks = super.getDroppedStacks(state, builder);
        for (ItemStack stack : Blocks.CAULDRON.getDroppedStacks(Blocks.CAULDRON.getDefaultState(), builder)) {
            stacks.add(stack);
        }
        for (ItemStack stack : baseBlock.getDroppedStacks(getBaseBlockState(state), builder)) {
            stacks.add(stack);
        }
        return stacks;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            if (random.nextInt(10) == 0) {
                world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
            }
            float f = random.nextFloat() * MathHelper.PI * 2.0F;
            world.addParticle(this.particleEffect, pos.getX() + 0.5 + MathHelper.sin(f) * 0.45, pos.getY() + random.nextBetween(6, 8) / 16.0, pos.getZ() + 0.5 + MathHelper.cos(f) * 0.45, 0.0, 0.0, 0.0);
        }
    }

    public static void extinguish(@Nullable Entity entity, WorldAccess world, BlockPos pos, BlockState state) {
        if (world.isClient()) {
            for (int i = 0; i < 20; i++) {
                CampfireBlock.spawnSmokeParticle((World) world, pos, false, true);
            }
        }

        world.emitGameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.get(Properties.WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
            boolean bl = state.get(LIT);
            if (bl) {
                extinguish(null, world, pos, state);
            }

            world.setBlockState(pos, state.with(WATERLOGGED, true).with(LIT, false), 3);
            world.scheduleFluidTick(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        BlockPos blockPos = hit.getBlockPos();
        if (!world.isClient() && projectile.isOnFire() && projectile.canModifyAt(world, blockPos) && !state.get(LIT) && !state.get(WATERLOGGED)) {
            world.setBlockState(blockPos, state.with(Properties.LIT, true), 11);
        }
    }

    public static void spawnSmokeParticle(World world, BlockPos pos, boolean lotsOfSmoke) {
        Random random = world.getRandom();
        float f = random.nextFloat() * MathHelper.PI * 2.0F;
        world.addImportantParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, pos.getX() + 0.5 + MathHelper.sin(f) * 0.5, pos.getY() + random.nextDouble() + random.nextDouble(), pos.getZ() + 0.5 + MathHelper.cos(f) * 0.5, 0.0, 0.07, 0.0);
        if (lotsOfSmoke) {
            world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1.0 : -1.0), pos.getY() + 0.4, pos.getZ() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1.0 : -1.0), 0.0, 0.005, 0.0);
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED, FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CauldronCampfireBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) {
            return state.get(LIT) ? checkType(type, Caffeinated.CAULDRON_CAMPFIRE, CauldronCampfireBlockEntity::clientTick) : null;
        }
        return null;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    public static boolean canBeLit(BlockState state) {
        return state.isIn(CaffeinatedBlockTags.CAULDRON_CAMPFIRES, statex -> statex.contains(WATERLOGGED) && statex.contains(LIT)) && !state.get(WATERLOGGED) && !state.get(LIT);
    }

    private BlockState getBaseBlockState(BlockState state) {
        if (this.baseBlock instanceof CampfireBlock campfireBlock) {
            return campfireBlock.getDefaultState().with(FACING, state.get(FACING)).with(LIT, state.get(LIT)).with(WATERLOGGED, state.get(WATERLOGGED));
        } else {
            return this.baseBlock.getDefaultState();
        }
    }
}
