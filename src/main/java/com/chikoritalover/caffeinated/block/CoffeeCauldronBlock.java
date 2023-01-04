package com.chikoritalover.caffeinated.block;

import com.chikoritalover.caffeinated.registry.ModParticleTypes;
import com.chikoritalover.caffeinated.registry.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Map;

public class CoffeeCauldronBlock extends LeveledCauldronBlock {
    public CoffeeCauldronBlock(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, null, behaviorMap);
    }

    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(Blocks.CAULDRON);
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (CampfireBlock.isLitCampfireInRange(world, pos)) {
            double d = pos.getX() + random.nextDouble() * 0.5 + 0.25;
            double e = pos.getY() + 0.375 + state.get(LEVEL) * 0.1875;
            double f = pos.getZ() + random.nextDouble() * 0.5 + 0.25;
            if (random.nextDouble() < 0.15) {
                world.playSound(d, e, f, ModSoundEvents.BLOCK_COFFEE_CAULDRON_BUBBLE, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
            }

            world.addParticle(ModParticleTypes.COFFEE_POP, d, e, f, 0.0, 0.0, 0.0);
        }
    }
}
