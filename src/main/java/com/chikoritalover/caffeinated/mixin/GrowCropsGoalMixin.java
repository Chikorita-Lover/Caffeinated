package com.chikoritalover.caffeinated.mixin;

import com.chikoritalover.caffeinated.block.CoffeeShrubBlock;
import com.chikoritalover.caffeinated.block.FloweringCoffeeShrubBlock;
import com.chikoritalover.caffeinated.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(targets = "net.minecraft.entity.passive.BeeEntity$GrowCropsGoal")
public class GrowCropsGoalMixin {
    @Final
    BeeEntity field_20373;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void tick(CallbackInfo info, int i, BlockPos blockPos, BlockState blockState, Block block) {
        boolean bl = false;

        if (block instanceof CoffeeShrubBlock) {
            bl = true;

            if (blockState.get(CoffeeShrubBlock.AGE) == 3) {
                field_20373.world.setBlockState(blockPos, ModBlocks.FLOWERING_COFFEE_SHRUB.getDefaultState());
                field_20373.world.setBlockState(blockPos.up(), ModBlocks.FLOWERING_COFFEE_SHRUB.getDefaultState().cycle(FloweringCoffeeShrubBlock.HALF));
            } else {
                field_20373.world.setBlockState(blockPos, blockState.cycle(CoffeeShrubBlock.AGE));
            }
        } else if (block instanceof FloweringCoffeeShrubBlock) {
            if (blockState.get(FloweringCoffeeShrubBlock.AGE) < 3) {
                bl = true;
                IntProperty intProperty = FloweringCoffeeShrubBlock.AGE;

                BlockState blockState2 = blockState.with(intProperty, blockState.get(intProperty) + 1);

                field_20373.world.setBlockState(blockPos, blockState2);
                field_20373.world.setBlockState(blockState2.get(FloweringCoffeeShrubBlock.HALF) == DoubleBlockHalf.LOWER ? blockPos.up() : blockPos.down(), blockState2.cycle(FloweringCoffeeShrubBlock.HALF));
            }
        }

        if (bl) {
            field_20373.world.syncWorldEvent(2005, blockPos, 0);
            field_20373.addCropCounter();
        }
    }
}
