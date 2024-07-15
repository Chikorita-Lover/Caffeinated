package net.chikorita_lover.caffeinated.mixin;

import net.chikorita_lover.caffeinated.block.CauldronCampfireBlock;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin extends AbstractCauldronBlock {
    public CauldronBlockMixin(Settings settings, CauldronBehavior.CauldronBehaviorMap behaviorMap) {
        super(settings, behaviorMap);
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());
        Block block = state.getBlock();
        if (ctx.getSide() != Direction.DOWN && CauldronCampfireBlock.CAMPFIRE_TO_CAULDRON_CAMPFIRE.containsKey(block)) {
            BlockState state2 = CauldronCampfireBlock.CAMPFIRE_TO_CAULDRON_CAMPFIRE.get(block).getDefaultState();
            if (block instanceof CampfireBlock) {
                CauldronCampfireBlock.spawnSmokeParticle(ctx.getWorld(), ctx.getBlockPos(), false);
                return state2.with(CauldronCampfireBlock.FACING, state.get(CauldronCampfireBlock.FACING)).with(CauldronCampfireBlock.LIT, state.get(CauldronCampfireBlock.LIT)).with(CauldronCampfireBlock.WATERLOGGED, state.get(CauldronCampfireBlock.WATERLOGGED));
            } else {
                return state2;
            }
        } else {
            return super.getPlacementState(ctx);
        }
    }
}
