package com.chikoritalover.caffeinated.block;

import com.chikoritalover.caffeinated.registry.ModBlockTags;
import com.chikoritalover.caffeinated.registry.ModParticleTypes;
import com.chikoritalover.caffeinated.registry.ModSoundEvents;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShapes;
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
        if (isLitFireInRange(world, pos)) {
            int level = state.get(LEVEL);

            double d = pos.getX() + random.nextDouble() * 0.5 + 0.25;
            double e = pos.getY() + 0.375 + level * 0.1875;
            double f = pos.getZ() + random.nextDouble() * 0.5 + 0.25;
            if (random.nextDouble() < 0.15) {
                world.playSound(d, e, f, ModSoundEvents.BLOCK_CAULDRON_BUBBLE, SoundCategory.BLOCKS, 0.2F, 2.5F - level * 0.5F, true);
            }

            world.addParticle(getPopParticleEffect(), d, e, f, 0.0, 0.0, 0.0);
        }
    }

    public static boolean isLitFireInRange(World world, BlockPos pos) {
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);

        boolean bl = blockState.isIn(ModBlockTags.LIT_FIRES);

        if (blockState.contains(Properties.LIT)) {
            return bl && blockState.get(Properties.LIT);
        } else {
            return bl;
        }
    }

    public ParticleEffect getPopParticleEffect() {
        return ModParticleTypes.COFFEE_POP;
    }
}
