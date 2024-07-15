package net.chikorita_lover.caffeinated.mixin;

import net.chikorita_lover.caffeinated.block.CauldronCampfireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
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
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (context.getSide() == Direction.DOWN) {
            cir.setReturnValue(ActionResult.PASS);
        } else {
            BlockState state2 = null;
            if (state.getBlock() instanceof CauldronCampfireBlock && state.get(CauldronCampfireBlock.LIT)) {
                if (!world.isClient()) {
                    world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, pos, 0);
                }
                CauldronCampfireBlock.extinguish(context.getPlayer(), world, pos, state);
                state2 = state.with(CauldronCampfireBlock.LIT, false);
            }

            if (state2 != null) {
                if (!world.isClient()) {
                    PlayerEntity player = context.getPlayer();
                    world.setBlockState(pos, state2, Block.NOTIFY_ALL_AND_REDRAW);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, state2));
                    if (player != null) {
                        context.getStack().damage(1, player, LivingEntity.getSlotForHand(context.getHand()));
                    }
                }
                cir.setReturnValue(ActionResult.success(world.isClient()));
            }
        }
    }
}
