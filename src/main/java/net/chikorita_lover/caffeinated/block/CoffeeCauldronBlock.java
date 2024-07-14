package net.chikorita_lover.caffeinated.block;

import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedBlockTags;
import net.chikorita_lover.caffeinated.registry.CaffeinatedParticleTypes;
import net.chikorita_lover.caffeinated.registry.CaffeinatedSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class CoffeeCauldronBlock extends LeveledCauldronBlock {
    public static final BooleanProperty REWARD_EXPERIENCE = BooleanProperty.of("reward_experience");

    public CoffeeCauldronBlock(CauldronBehavior.CauldronBehaviorMap behaviorMap, Settings settings) {
        super(null, behaviorMap, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(REWARD_EXPERIENCE, false));
    }

    public static boolean isLitFireInRange(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos.down());
        return state.isIn(CaffeinatedBlockTags.LIT_FIRES) && (!state.contains(Properties.LIT) || state.get(Properties.LIT));
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(Blocks.CAULDRON);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (isLitFireInRange(world, pos)) {
            int level = state.get(LEVEL);

            double d = pos.getX() + random.nextDouble() * 0.5 + 0.25;
            double e = pos.getY() + 0.375 + level * 0.1875;
            double f = pos.getZ() + random.nextDouble() * 0.5 + 0.25;
            if (random.nextDouble() < 0.15) {
                world.playSound(d, e, f, CaffeinatedSoundEvents.BLOCK_CAULDRON_CAMPFIRE_BUBBLE, SoundCategory.BLOCKS, 0.2F, 2.5F - level * 0.5F, true);
            }

            world.addParticle(getPopParticleEffect(), d, e, f, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(REWARD_EXPERIENCE);
    }

    public ParticleEffect getPopParticleEffect() {
        return CaffeinatedParticleTypes.COFFEE_POP;
    }
}
