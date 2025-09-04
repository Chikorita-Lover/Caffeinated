package net.chikorita_lover.caffeinated.data;

import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class CaffeinatedBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public CaffeinatedBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries) {
        this.getOrCreateTagBuilder(CaffeinatedBlockTags.CAULDRON_CAMPFIRES).add(CaffeinatedBlocks.CAULDRON_CAMPFIRE, CaffeinatedBlocks.SOUL_CAULDRON_CAMPFIRE);
        this.getOrCreateTagBuilder(CaffeinatedBlockTags.CIVETS_SPAWNABLE_ON).forceAddTag(BlockTags.ANIMALS_SPAWNABLE_ON).forceAddTag(BlockTags.LEAVES).forceAddTag(BlockTags.LOGS);
        this.getOrCreateTagBuilder(CaffeinatedBlockTags.LIT_FIRES).forceAddTag(BlockTags.CAMPFIRES).forceAddTag(BlockTags.FIRE);
        this.getOrCreateTagBuilder(BlockTags.BEE_GROWABLES).add(CaffeinatedBlocks.COFFEE_SHRUB, CaffeinatedBlocks.FLOWERING_COFFEE_SHRUB);
        this.getOrCreateTagBuilder(BlockTags.CAMEL_SAND_STEP_SOUND_BLOCKS).add(CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
        this.getOrCreateTagBuilder(BlockTags.CAULDRONS).add(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, CaffeinatedBlocks.COFFEE_CAULDRON);
        this.getOrCreateTagBuilder(BlockTags.FLOWER_POTS).add(CaffeinatedBlocks.POTTED_COFFEE_SHRUB);
        this.getOrCreateTagBuilder(BlockTags.MUSHROOM_GROW_BLOCK).add(CaffeinatedBlocks.CIVET_SCAT);
        this.getOrCreateTagBuilder(BlockTags.TALL_FLOWERS).add(CaffeinatedBlocks.FLOWERING_COFFEE_SHRUB);
        this.getOrCreateTagBuilder(BlockTags.WOOL).add(CaffeinatedBlocks.COFFEE_STAINED_WOOL);
        this.getOrCreateTagBuilder(BlockTags.WOOL_CARPETS).add(CaffeinatedBlocks.COFFEE_STAINED_CARPET);
        this.getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).addTag(CaffeinatedBlockTags.CAULDRON_CAMPFIRES).add(CaffeinatedBlocks.COFFEE_BERRY_CRATE, CaffeinatedBlocks.COFFEE_BEAN_BLOCK);
        this.getOrCreateTagBuilder(BlockTags.HOE_MINEABLE).add(CaffeinatedBlocks.CIVET_SCAT);
        this.getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE).add(CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
    }
}
