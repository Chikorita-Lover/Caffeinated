package net.chikorita_lover.caffeinated.mixin;

import net.chikorita_lover.caffeinated.block.CauldronCampfireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends BlockWithEntity {
    protected CampfireBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "canBeLit", at = @At("HEAD"), cancellable = true)
    private static void canBeLit(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() instanceof CauldronCampfireBlock) {
            cir.setReturnValue(CauldronCampfireBlock.canBeLit(state));
        }
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        ItemStack stack = context.getStack();
        return context.getSide() != Direction.DOWN && stack.isOf(Items.CAULDRON) && CauldronCampfireBlock.CAMPFIRE_TO_CAULDRON_CAMPFIRE.containsKey(this) || super.canReplace(state, context);
    }
}
