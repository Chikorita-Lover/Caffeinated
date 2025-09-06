package net.chikorita_lover.caffeinated.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.chikorita_lover.caffeinated.block.CauldronDispenserBehavior;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DispenserBlock.class)
public class DispenserBlockMixin {
    @ModifyExpressionValue(method = "dispense", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/DispenserBlock;getBehaviorForItem(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/block/dispenser/DispenserBehavior;"))
    private DispenserBehavior getCauldronBehavior(DispenserBehavior behavior, ServerWorld world, BlockState state, BlockPos pos) {
        if (world.getBlockState(pos.offset(state.get(DispenserBlock.FACING))).getBlock() instanceof AbstractCauldronBlock) {
            return CauldronDispenserBehavior.INSTANCE;
        }
        return behavior;
    }
}
