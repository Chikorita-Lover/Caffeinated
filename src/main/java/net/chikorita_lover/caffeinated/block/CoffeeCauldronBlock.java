package net.chikorita_lover.caffeinated.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class CoffeeCauldronBlock extends LeveledCauldronBlock {
    public CoffeeCauldronBlock(Settings settings) {
        super(null, CauldronBehavior.WATER_CAULDRON_BEHAVIOR, settings);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(Blocks.CAULDRON);
    }
}
