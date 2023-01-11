package com.chikoritalover.caffeinated.block;

import com.chikoritalover.caffeinated.registry.ModBlocks;
import com.chikoritalover.caffeinated.registry.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

public class GroundCoffeeCauldronBlock extends LeveledCauldronBlock {
    public GroundCoffeeCauldronBlock(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, null, behaviorMap);
    }

    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(Blocks.CAULDRON);
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

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (CampfireBlock.isLitCampfireInRange(world, pos)) {
            int level = state.get(LEVEL);

            double d = pos.getX() + random.nextDouble() * 0.5 + 0.25;
            double e = pos.getY() + 0.375 + level * 0.1875;
            double f = pos.getZ() + random.nextDouble() * 0.5 + 0.25;
            if (random.nextDouble() < 0.15) {
                world.playSound(d, e, f, ModSoundEvents.BLOCK_CAULDRON_BUBBLE, SoundCategory.BLOCKS, 0.2F, 2.5F - level * 0.5F, true);
            }

            world.addParticle(ParticleTypes.BUBBLE_POP, d, e, f, 0.0, 0.0, 0.0);
        }
    }
}
