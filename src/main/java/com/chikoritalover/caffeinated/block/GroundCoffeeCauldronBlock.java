package com.chikoritalover.caffeinated.block;

import com.chikoritalover.caffeinated.registry.ModBlocks;
import com.chikoritalover.caffeinated.registry.ModCauldronBehavior;
import com.chikoritalover.caffeinated.registry.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.event.GameEvent;

public class GroundCoffeeCauldronBlock extends CoffeeCauldronBlock {
    public GroundCoffeeCauldronBlock(Settings settings) {
        super(settings, ModCauldronBehavior.GROUND_COFFEE_CAULDRON_BEHAVIOR);
    }

    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (CampfireBlock.isLitCampfireInRange(world, pos)) {
            BlockState blockState = ModBlocks.COFFEE_CAULDRON.getDefaultState().with(LEVEL, state.get(LEVEL));

            world.setBlockState(pos, blockState);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));

            world.playSound(null, pos, ModSoundEvents.BLOCK_CAULDRON_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public ParticleEffect getPopParticleEffect() {
        return ParticleTypes.BUBBLE_POP;
    }
}
