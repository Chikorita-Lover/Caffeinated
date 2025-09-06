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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class HeatableCauldronEffects {
    public static void tryCauldronEffects(BlockState state, World world, BlockPos pos, Random random) {
        tryCauldronEffects(state, world, pos, random, ParticleTypes.BUBBLE_POP);
    }

    public static void tryCauldronEffects(BlockState state, World world, BlockPos pos, Random random, ParticleEffect particle) {
        if (!isLitFireInRange(world, pos) || !(state.getBlock() instanceof AbstractCauldronBlock cauldron)) {
            return;
        }
        double height = cauldron.getFluidHeight(state);
        double x = pos.getX() + random.nextDouble() * 0.5 + 0.25;
        double y = pos.getY() + height + 0.0625;
        double z = pos.getZ() + random.nextDouble() * 0.5 + 0.25;
        if (random.nextDouble() < 0.1) {
            world.playSoundAtBlockCenter(pos, CaffeinatedSoundEvents.BLOCK_CAULDRON_BUBBLE, SoundCategory.BLOCKS, 0.15F, MathHelper.nextBetween(random, 2.8F, 3.2F) - (float) height * 2.0F, false);
        }
        world.addParticle(particle, x, y, z, 0.0, 0.0, 0.0);
    }

    public static boolean isLitFireInRange(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos.down());
        return state.isIn(CaffeinatedBlockTags.LIT_FIRES) && (!state.contains(Properties.LIT) || state.get(Properties.LIT));
    }
}
