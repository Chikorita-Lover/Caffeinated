package com.chikoritalover.caffeinated.block.entity;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.block.CauldronCampfireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class CauldronCampfireBlockEntity extends BlockEntity {
    public CauldronCampfireBlockEntity(BlockPos pos, BlockState state) {
        super(Caffeinated.CAULDRON_CAMPFIRE, pos, state);
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, CauldronCampfireBlockEntity cauldronCampfire) {
        Random random = world.getRandom();
        int i;
        if (random.nextFloat() < 0.06F) {
            for(i = 0; i < random.nextInt(2) + 2; ++i) {
                CauldronCampfireBlock.spawnSmokeParticle(world, pos, false);
            }
        }
    }
}
