package net.chikorita_lover.caffeinated.block;

import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedItems;
import net.chikorita_lover.caffeinated.registry.CaffeinatedSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;

import java.util.Map;

public class GroundCoffeeCauldronBlock extends LeveledCauldronBlock implements HeatableCauldron {
    private static final CauldronBehavior.CauldronBehaviorMap GROUND_COFFEE_CAULDRON_BEHAVIOR = CauldronBehavior.createMap("ground_coffee");

    public GroundCoffeeCauldronBlock(Settings settings) {
        super(Biome.Precipitation.RAIN, GROUND_COFFEE_CAULDRON_BEHAVIOR, settings);
    }

    public static void registerBehavior() {
        Map<Item, CauldronBehavior> map = GROUND_COFFEE_CAULDRON_BEHAVIOR.map();
        CauldronBehavior.registerBucketBehavior(map);
        map.put(Items.BUCKET, CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().get(Items.BUCKET));
        map.put(Items.GLASS_BOTTLE, CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().get(Items.GLASS_BOTTLE));
        map.put(Items.POTION, CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().get(Items.POTION));
        CauldronBehavior.WATER_CAULDRON_BEHAVIOR.map().put(CaffeinatedItems.GROUND_COFFEE, (state, world, pos, player, hand, stack) -> {
            if (!world.isClient()) {
                Item item = stack.getItem();
                stack.decrementUnlessCreative(1, player);
                player.incrementStat(Stats.USED.getOrCreateStat(item));
                world.setBlockState(pos, CaffeinatedBlocks.GROUND_COFFEE_CAULDRON.getStateWithProperties(state));
                world.playSound(null, pos, CaffeinatedSoundEvents.ITEM_GROUND_COFFEE_SPLASH, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            }
            return ItemActionResult.success(world.isClient());
        });
    }

    private void scheduleFinishBrewing(World world, BlockPos pos) {
        if (HeatableCauldron.isLitFireInRange(world, pos)) {
            Random random = world.getRandom();
            world.scheduleBlockTick(pos, this, 300 + random.nextInt(300));
        }
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        this.scheduleFinishBrewing(world, pos);
    }

    @Override
    protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        this.scheduleFinishBrewing(world, pos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (HeatableCauldron.isLitFireInRange(world, pos)) {
            BlockState newState = CaffeinatedBlocks.COFFEE_CAULDRON.getStateWithProperties(state).with(CoffeeCauldronBlock.HAS_EXPERIENCE, true);
            world.setBlockState(pos, newState);
            world.playSound(null, pos, CaffeinatedSoundEvents.BLOCK_CAULDRON_BREW, SoundCategory.BLOCKS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(newState));
        }
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(Blocks.CAULDRON);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        this.randomCauldronEffects(state, world, pos, random);
    }
}
