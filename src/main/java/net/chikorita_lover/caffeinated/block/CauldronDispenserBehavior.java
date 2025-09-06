package net.chikorita_lover.caffeinated.block;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CauldronDispenserBehavior extends FallibleItemDispenserBehavior {
    public static final CauldronDispenserBehavior INSTANCE = new CauldronDispenserBehavior();

    @Override
    protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
        ServerWorld world = pointer.world();
        Direction facing = pointer.state().get(DispenserBlock.FACING);
        BlockPos pos = pointer.pos().offset(facing);
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof AbstractCauldronBlock) {
            FakePlayer player = FakePlayer.get(world);
            player.getInventory().clear();
            player.setStackInHand(Hand.MAIN_HAND, stack);
            ItemActionResult result = state.onUseWithItem(stack, world, player, Hand.MAIN_HAND, new BlockHitResult(pos.toCenterPos(), facing.getOpposite(), pos, false));
            this.setSuccess(result.isAccepted());
            if (this.isSuccess()) {
                stack = player.getStackInHand(Hand.MAIN_HAND);
                player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                player.getInventory().main.forEach(stackx -> this.addStackOrSpawn(pointer, stackx));
            }
        }
        return stack;
    }
}
