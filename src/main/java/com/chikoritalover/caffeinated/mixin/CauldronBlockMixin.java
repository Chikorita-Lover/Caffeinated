package com.chikoritalover.caffeinated.mixin;

import com.chikoritalover.caffeinated.block.CauldronCampfireBlock;
import com.chikoritalover.caffeinated.block.entity.CauldronCampfireBlockEntity;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Map;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin extends AbstractCauldronBlock {
    public CauldronBlockMixin(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, behaviorMap);
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = ctx.getWorld().getBlockState(blockPos);
        if (ctx.getSide() != Direction.DOWN && CauldronCampfireBlock.CAMPFIRE_TO_CAULDRON_CAMPFIRE.containsKey(blockState.getBlock())) {
            BlockState blockState2 = CauldronCampfireBlock.CAMPFIRE_TO_CAULDRON_CAMPFIRE.get(blockState.getBlock()).getDefaultState();
            if (blockState.getBlock() instanceof CampfireBlock) {
                CauldronCampfireBlock.spawnSmokeParticle(ctx.getWorld(), ctx.getBlockPos(), false);
                return blockState2.with(CauldronCampfireBlock.FACING, blockState.get(CauldronCampfireBlock.FACING)).with(CauldronCampfireBlock.LIT, blockState.get(CauldronCampfireBlock.LIT)).with(CauldronCampfireBlock.WATERLOGGED, blockState.get(CauldronCampfireBlock.WATERLOGGED));
            } else {
                return blockState2;
            }
        } else {
            return super.getPlacementState(ctx);
        }
    }
}
