package com.chikoritalover.caffeinated.block;

import com.chikoritalover.caffeinated.registry.ModBlocks;
import com.chikoritalover.caffeinated.registry.ModCauldronBehavior;
import com.chikoritalover.caffeinated.registry.ModSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class GroundCoffeeCauldronBlock extends CoffeeCauldronBlock {
    public GroundCoffeeCauldronBlock(Settings settings) {
        super(settings, ModCauldronBehavior.GROUND_COFFEE_CAULDRON_BEHAVIOR);
    }

    @Override
    public ParticleEffect getPopParticleEffect() {
        return ParticleTypes.BUBBLE_POP;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (isLitFireInRange(world, pos)) {
            world.scheduleBlockTick(pos, ModBlocks.GROUND_COFFEE_CAULDRON, 200);
        }
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (isLitFireInRange(world, pos)) {
            world.scheduleBlockTick(pos, ModBlocks.GROUND_COFFEE_CAULDRON, 200);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (isLitFireInRange(world, pos)) {
            BlockState blockState = ModBlocks.COFFEE_CAULDRON.getDefaultState().with(LEVEL, state.get(LEVEL)).with(CoffeeCauldronBlock.REWARD_EXPERIENCE, true);

            world.setBlockState(pos, blockState);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));

            world.playSound(null, pos, ModSoundEvents.BLOCK_CAULDRON_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }
}
