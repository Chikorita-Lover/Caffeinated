package net.chikorita_lover.caffeinated.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ScatBlock extends Block {
    public static final MapCodec<ScatBlock> CODEC = createCodec(ScatBlock::new);

    public ScatBlock(Settings settings) {
        super(settings);
    }

    @Override
    public MapCodec<ScatBlock> getCodec() {
        return CODEC;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (random.nextInt(7) == 0) {
            Direction direction = Direction.random(random);
            BlockPos offsetPos = pos.offset(direction);
            BlockState offsetState = world.getBlockState(offsetPos);
            if (state.isOpaque() && offsetState.isSideSolidFullSquare(world, offsetPos, direction.getOpposite())) {
                return;
            }
            double dx = direction.getOffsetX() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetX() * 0.6;
            double dy = direction.getOffsetY() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetY() * 0.6;
            double dz = direction.getOffsetZ() == 0 ? random.nextDouble() : 0.5 + direction.getOffsetZ() * 0.6;
            world.addParticle(ParticleTypes.MYCELIUM, pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, 0.0, 0.0, 0.0);
        }
    }
}
