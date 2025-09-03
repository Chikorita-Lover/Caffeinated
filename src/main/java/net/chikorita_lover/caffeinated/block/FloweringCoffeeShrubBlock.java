package net.chikorita_lover.caffeinated.block;

import com.mojang.serialization.MapCodec;
import net.chikorita_lover.caffeinated.registry.CaffeinatedItems;
import net.chikorita_lover.caffeinated.registry.CaffeinatedSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class FloweringCoffeeShrubBlock extends TallPlantBlock implements Fertilizable {
    public static final int MAX_AGE = 3;
    public static final IntProperty AGE = Properties.AGE_3;

    public FloweringCoffeeShrubBlock(Settings settings) {
        super(settings);
    }

    public static ActionResult pickBerries(@Nullable Entity picker, BlockState state, World world, BlockPos pos, boolean eaten) {
        if (!state.contains(AGE) || state.get(AGE) < MAX_AGE) {
            return ActionResult.PASS;
        }
        if (!eaten) {
            dropStack(world, pos, new ItemStack(CaffeinatedItems.COFFEE_BERRIES, 1 + world.getRandom().nextInt(3)));
        }
        float pitch = MathHelper.nextBetween(world.random, 0.8F, 1.2F);
        world.playSound(null, pos, CaffeinatedSoundEvents.BLOCK_COFFEE_SHRUB_PICK_BERRIES, SoundCategory.BLOCKS, 1.0F, pitch);
        BlockState pickedState = state.with(AGE, eaten ? world.getRandom().nextInt(3) : 0);
        world.setBlockState(pos, pickedState, Block.NOTIFY_LISTENERS);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(picker, pickedState));
        BlockState otherState = pickedState.cycle(HALF);
        BlockPos otherPos = pos.offset(pickedState.get(HALF).getOppositeDirection());
        world.setBlockState(otherPos, otherState, Block.NOTIFY_LISTENERS);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, otherPos, GameEvent.Emitter.of(picker, otherState));
        return ActionResult.success(world.isClient());
    }

    @Override
    public MapCodec<? extends TallPlantBlock> getCodec() {
        return createCodec(FloweringCoffeeShrubBlock::new);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(CaffeinatedItems.COFFEE_BEANS);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER && state.get(AGE) < MAX_AGE;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(AGE);
        if (age < MAX_AGE && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            this.grow(world, random, pos, state);
        }
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(AGE) < MAX_AGE && stack.getItem() instanceof BoneMealItem) {
            return ItemActionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (state.get(AGE) < MAX_AGE) {
            return super.onUse(state, world, pos, player, hit);
        }
        return pickBerries(player, state, world, pos, false);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
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
        int newAge = Math.min(MAX_AGE, state.get(AGE) + 1);
        world.setBlockState(pos, state.with(AGE, newAge), Block.NOTIFY_LISTENERS);
        world.setBlockState(pos.offset(state.get(HALF).getOppositeDirection()), state.with(AGE, newAge).cycle(HALF), Block.NOTIFY_ALL);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AGE);
    }
}
