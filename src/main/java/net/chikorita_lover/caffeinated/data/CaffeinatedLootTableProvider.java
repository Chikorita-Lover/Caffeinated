package net.chikorita_lover.caffeinated.data;

import net.chikorita_lover.caffeinated.block.FloweringCoffeeShrubBlock;
import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class CaffeinatedLootTableProvider extends FabricBlockLootTableProvider {
    public CaffeinatedLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        this.addDrop(CaffeinatedBlocks.FLOWERING_COFFEE_SHRUB, this::floweringCoffeeShrubDrops);
        this.addPottedPlantDrops(CaffeinatedBlocks.POTTED_COFFEE_SHRUB);
        this.addDrop(CaffeinatedBlocks.COFFEE_BEAN_BLOCK);
        this.addDrop(CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
        this.addDrop(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, Blocks.CAULDRON);
        this.addDrop(CaffeinatedBlocks.COFFEE_CAULDRON, Blocks.CAULDRON);
        this.addDrop(CaffeinatedBlocks.COFFEE_BERRY_CRATE);
    }

    private LootTable.Builder floweringCoffeeShrubDrops(Block block) {
        RegistryWrapper.Impl<Enchantment> enchantmentRegistry = this.registryLookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        return this.applyExplosionDecay(block, LootTable.builder().pool(LootPool.builder().conditionally(BlockStatePropertyLootCondition.builder(CaffeinatedBlocks.FLOWERING_COFFEE_SHRUB).properties(StatePredicate.Builder.create().exactMatch(FloweringCoffeeShrubBlock.AGE, 3).exactMatch(FloweringCoffeeShrubBlock.HALF, DoubleBlockHalf.LOWER))).with(ItemEntry.builder(CaffeinatedItems.COFFEE_BERRIES)).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))).apply(ApplyBonusLootFunction.uniformBonusCount(enchantmentRegistry.getOrThrow(Enchantments.FORTUNE)))));
    }
}
