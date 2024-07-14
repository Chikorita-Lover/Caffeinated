package com.chikoritalover.caffeinated.mixin;

import com.chikoritalover.caffeinated.block.CauldronCampfireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public class ShovelItemMixin {
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (context.getSide() == Direction.DOWN) {
            cir.setReturnValue(ActionResult.PASS);
        } else {
            BlockState blockState2 = null;
            if (blockState.getBlock() instanceof CauldronCampfireBlock && blockState.get(CauldronCampfireBlock.LIT)) {
                if (!world.isClient()) {
                    world.syncWorldEvent(null, 1009, blockPos, 0);
                }

                CauldronCampfireBlock.extinguish(context.getPlayer(), world, blockPos, blockState);
                blockState2 = blockState.with(CauldronCampfireBlock.LIT, false);
            }

            if (blockState2 != null) {
                if (!world.isClient) {
                    PlayerEntity playerEntity = context.getPlayer();
                    world.setBlockState(blockPos, blockState2, 11);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, blockState2));
                    if (playerEntity != null) {
                        context.getStack().damage(1, playerEntity, (p) -> {
                            p.sendToolBreakStatus(context.getHand());
                        });
                    }
                }

                cir.setReturnValue(ActionResult.success(world.isClient()));
            }
        }
    }
}
