package net.chikorita_lover.caffeinated.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.chikorita_lover.caffeinated.block.CoffeeShrubBlock;
import net.chikorita_lover.caffeinated.block.FloweringCoffeeShrubBlock;
import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.entity.passive.BeeEntity$GrowCropsGoal")
public class GrowCropsGoalMixin {
    @Final
    BeeEntity field_20373;

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    public boolean tryGrowCoffeeShrub(boolean isBeeGrowable, @Local int i, @Local BlockPos blockPos, @Local(ordinal = 0) BlockState blockState, @Local Block block, @Local(ordinal = 1) LocalRef<BlockState> blockState2) {
        if (!isBeeGrowable) {
            return false;
        }
        World world = field_20373.getWorld();
        if (block instanceof CoffeeShrubBlock) {
            if (blockState.get(CoffeeShrubBlock.AGE) == 3) {
                blockState2.set(CaffeinatedBlocks.FLOWERING_COFFEE_SHRUB.getDefaultState());
                world.setBlockState(blockPos, blockState2.get());
                world.setBlockState(blockPos.up(), blockState2.get().cycle(FloweringCoffeeShrubBlock.HALF));
            } else {
                blockState2.set(blockState.cycle(CoffeeShrubBlock.AGE));
                world.setBlockState(blockPos, blockState2.get());
            }
        } else if (block instanceof FloweringCoffeeShrubBlock) {
            if (blockState.get(FloweringCoffeeShrubBlock.AGE) < 3) {
                blockState2.set(blockState.cycle(FloweringCoffeeShrubBlock.AGE));
                field_20373.getWorld().setBlockState(blockPos, blockState2.get());
                field_20373.getWorld().setBlockState(blockPos.add(blockState2.get().get(FloweringCoffeeShrubBlock.HALF).getOppositeDirection().getVector()), blockState2.get().cycle(FloweringCoffeeShrubBlock.HALF));
            }
        }
        return true;
    }
}
