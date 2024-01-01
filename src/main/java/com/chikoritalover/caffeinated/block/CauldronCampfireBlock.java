package com.chikoritalover.caffeinated.block;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.block.entity.CauldronCampfireBlockEntity;
import com.chikoritalover.caffeinated.registry.CaffeinatedBlockTags;
import com.chikoritalover.caffeinated.registry.CaffeinatedSoundEvents;
import com.chikoritalover.caffeinated.registry.CaffeinatedStats;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CauldronCampfireBlock extends BlockWithEntity implements Waterloggable {
    public static final BooleanProperty FILLED = BooleanProperty.of("filled");
    public static final BooleanProperty LIT = Properties.LIT;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final Map<Block, Block> CAMPFIRE_TO_CAULDRON_CAMPFIRE = Maps.newHashMap();
    protected static final VoxelShape COLLISION_SHAPE = Block.createCuboidShape(0.5, 0.0, 0.5, 15.5, 16.0, 15.5);
    protected static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0, 16.0), Block.createCuboidShape(2.0, 5.0, 2.0, 14.0, 16.0, 14.0), BooleanBiFunction.OR);
    private final Block baseBlock;
    private final ParticleEffect particleEffect;

    public CauldronCampfireBlock(Block baseBlock, ParticleEffect particleEffect, Settings settings) {
        super(settings);
        this.baseBlock = baseBlock;
        this.particleEffect = particleEffect;
        CAMPFIRE_TO_CAULDRON_CAMPFIRE.put(baseBlock, this);
        this.setDefaultState(this.getStateManager().getDefaultState().with(FILLED, false).with(LIT, true).with(WATERLOGGED, false).with(FACING, Direction.NORTH));
    }

    private static SoundEvent getEmptySoundEvent(ItemStack stack) {
        if (stack.getRecipeRemainder().isOf(Items.GLASS_BOTTLE)) {
            return SoundEvents.ITEM_BOTTLE_EMPTY;
        }
        if (stack.getRecipeRemainder().isIn(ConventionalItemTags.EMPTY_BUCKETS)) {
            return SoundEvents.ITEM_BUCKET_EMPTY;
        }
        return CaffeinatedSoundEvents.ITEM_GROUND_COFFEE_SPLASH;
    }

    public static void extinguish(@Nullable Entity entity, WorldAccess world, BlockPos pos, BlockState state) {
        if (world.isClient()) {
            for (int i = 0; i < 20; i++) {
                CampfireBlock.spawnSmokeParticle((World) world, pos, false, true);
            }
        }

        world.emitGameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
    }

    public static void spawnSmokeParticle(World world, BlockPos pos, boolean lotsOfSmoke) {
        Random random = world.getRandom();
        float f = random.nextFloat() * MathHelper.PI * 2.0F;
        world.addImportantParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, pos.getX() + 0.5 + MathHelper.sin(f) * 0.5, pos.getY() + random.nextDouble() + random.nextDouble(), pos.getZ() + 0.5 + MathHelper.cos(f) * 0.5, 0.0, 0.07, 0.0);
        if (lotsOfSmoke) {
            world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1.0 : -1.0), pos.getY() + 0.4, pos.getZ() + 0.5 + random.nextDouble() / 4.0 * (random.nextBoolean() ? 1.0 : -1.0), 0.0, 0.005, 0.0);
        }
    }

    public static boolean canBeLit(BlockState state) {
        return state.isIn(CaffeinatedBlockTags.CAULDRON_CAMPFIRES, statex -> statex.contains(WATERLOGGED) && statex.contains(LIT)) && !state.get(WATERLOGGED) && !state.get(LIT);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CauldronCampfireBlockEntity cauldronCampfire) {
            ItemStack stack = player.getStackInHand(hand);
            Item item = stack.getItem();
            Item item2 = cauldronCampfire.getStack(0).getItem();
            boolean bl = !state.get(FILLED) && cauldronCampfire.isBaseIngredient(stack);
            boolean bl2 = !cauldronCampfire.hasReagent() && cauldronCampfire.canBrewTogether(cauldronCampfire.getStack(0), stack);
            boolean bl3 = state.get(FILLED) && (!item2.hasRecipeRemainder() || stack.isOf(item2.getRecipeRemainder()));
            if (!bl && !bl2 && !bl3) {
                return ActionResult.PASS;
            }
            if (!world.isClient()) {
                if (bl) {
                    cauldronCampfire.addBaseIngredient(player, stack);
                    world.playSound(null, pos, getEmptySoundEvent(stack), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, item.getRecipeRemainder(stack)));
                    world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
                } else if (bl2) {
                    cauldronCampfire.addReagent(player, stack);
                    world.playSound(null, pos, getEmptySoundEvent(stack), SoundCategory.BLOCKS, 1.0F, 1.0F);
                    player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, item.getRecipeRemainder(stack)));
                    world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
                } else {
                    ItemStack itemStack = item2.getDefaultStack();
                    if (stack.isOf(item2.getRecipeRemainder())) {
                        player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, itemStack));
                    } else {
                        player.giveItemStack(itemStack);
                    }
                    cauldronCampfire.clear();
                    cauldronCampfire.dropExperienceForRecipesUsed((ServerPlayerEntity) player);
                    world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
                }
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                player.incrementStat(CaffeinatedStats.INTERACT_WITH_CAULDRON_CAMPFIRE);
            }
            return ActionResult.success(world.isClient());
        } else {
            return ActionResult.PASS;
        }
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
        if (!state.get(LIT)) {
            return;
        }
        if (random.nextInt(10) == 0) {
            world.playSoundAtBlockCenter(pos, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
        }
        float f = random.nextFloat() * MathHelper.PI * 2.0F;
        world.addParticle(this.particleEffect, pos.getX() + 0.5 + MathHelper.sin(f) * 0.45, pos.getY() + random.nextBetween(6, 8) / 16.0, pos.getZ() + 0.5 + MathHelper.cos(f) * 0.45, 0.0, 0.0, 0.0);
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
    public void precipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation) {
        if ((precipitation == Biome.Precipitation.RAIN || precipitation == Biome.Precipitation.SNOW) && world.getRandom().nextFloat() < 0.05F) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CauldronCampfireBlockEntity cauldronCampfire && !state.get(FILLED)) {
                cauldronCampfire.addBaseIngredient(null, PotionUtil.setPotion(Items.POTION.getDefaultStack(), Potions.WATER));
            }
            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        }
    }

    @Override
    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        BlockPos blockPos = hit.getBlockPos();
        if (!world.isClient() && projectile.isOnFire() && projectile.canModifyAt(world, blockPos) && !state.get(LIT) && !state.get(WATERLOGGED)) {
            world.setBlockState(blockPos, state.with(Properties.LIT, true), 11);
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
        builder.add(FILLED, LIT, WATERLOGGED, FACING);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CauldronCampfireBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (state.get(LIT)) {
            return checkType(type, Caffeinated.CAULDRON_CAMPFIRE, world.isClient() ? CauldronCampfireBlockEntity::clientTick : CauldronCampfireBlockEntity::litServerTick);
        }
        return null;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    private BlockState getBaseBlockState(BlockState state) {
        if (this.baseBlock instanceof CampfireBlock campfireBlock) {
            return campfireBlock.getDefaultState().with(FACING, state.get(FACING)).with(LIT, state.get(LIT)).with(WATERLOGGED, state.get(WATERLOGGED));
        } else {
            return this.baseBlock.getDefaultState();
        }
    }
}
