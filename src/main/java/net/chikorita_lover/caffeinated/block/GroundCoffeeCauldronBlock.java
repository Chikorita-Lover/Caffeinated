package net.chikorita_lover.caffeinated.block;

import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedCauldronBehavior;
import net.chikorita_lover.caffeinated.registry.CaffeinatedSoundEvents;
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
        super(CaffeinatedCauldronBehavior.GROUND_COFFEE_CAULDRON_BEHAVIOR, settings);
    }

    @Override
    public ParticleEffect getPopParticleEffect() {
        return ParticleTypes.BUBBLE_POP;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (isLitFireInRange(world, pos)) {
            world.scheduleBlockTick(pos, CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, 200);
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (isLitFireInRange(world, pos)) {
            world.scheduleBlockTick(pos, CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, 200);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (isLitFireInRange(world, pos)) {
            BlockState blockState = CaffeinatedBlocks.COFFEE_CAULDRON.getStateWithProperties(state).with(CoffeeCauldronBlock.REWARD_EXPERIENCE, true);
            world.setBlockState(pos, blockState);
            world.playSound(null, pos, CaffeinatedSoundEvents.BLOCK_CAULDRON_BREW, SoundCategory.BLOCKS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
        }
    }
}
