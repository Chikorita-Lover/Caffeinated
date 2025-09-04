package net.chikorita_lover.caffeinated.block;

import net.chikorita_lover.caffeinated.registry.CaffeinatedSoundEvents;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedBlockTags;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public interface HeatableCauldron {
    static boolean isLitFireInRange(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos.down());
        return state.isIn(CaffeinatedBlockTags.LIT_FIRES) && (!state.contains(Properties.LIT) || state.get(Properties.LIT));
    }

    default ParticleEffect getBubbleEffect() {
        return ParticleTypes.BUBBLE_POP;
    }

    default void randomCauldronEffects(BlockState state, World world, BlockPos pos, Random random) {
        if (!isLitFireInRange(world, pos) || !(state.getBlock() instanceof AbstractCauldronBlock cauldron)) {
            return;
        }
        double height = cauldron.getFluidHeight(state);
        double x = pos.getX() + random.nextDouble() * 0.5 + 0.25;
        double y = pos.getY() + height;
        double z = pos.getZ() + random.nextDouble() * 0.5 + 0.25;
        if (random.nextDouble() < 0.15) {
            world.playSound(x, y, z, CaffeinatedSoundEvents.BLOCK_CAULDRON_BUBBLE, SoundCategory.BLOCKS, 0.2F, 3.0F - (float) height * 2.0F, true);
        }
        world.addParticle(this.getBubbleEffect(), x, y, z, 0.0, 0.0, 0.0);
    }
}
