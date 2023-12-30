package com.chikoritalover.caffeinated;

import com.chikoritalover.caffeinated.advancement.BrewCoffeeCriterion;
import com.chikoritalover.caffeinated.block.entity.CauldronCampfireBlockEntity;
import com.chikoritalover.caffeinated.recipe.CoffeeBrewingRecipe;
import com.chikoritalover.caffeinated.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class Caffeinated implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MODID = "caffeinated";
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");
    public static final BrewCoffeeCriterion BREW_COFFEE_CRITERION = Criteria.register(new BrewCoffeeCriterion());
    public static final BlockEntityType<CauldronCampfireBlockEntity> CAULDRON_CAMPFIRE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(MODID, "cauldron_campfire"),
            FabricBlockEntityTypeBuilder.create(CauldronCampfireBlockEntity::new, CaffeinatedBlocks.CAULDRON_CAMPFIRE, CaffeinatedBlocks.SOUL_CAULDRON_CAMPFIRE).build(null)
    );
    public static final RecipeSerializer<CoffeeBrewingRecipe> COFFEE_BREWING_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MODID, "coffee_brewing"), new CoffeeBrewingRecipe.Serializer<CoffeeBrewingRecipe>(CoffeeBrewingRecipe::new, 600) {
    });
    public static final RecipeType<CoffeeBrewingRecipe> COFFEE_BREWING = Registry.register(Registries.RECIPE_TYPE, new Identifier(MODID, "coffee_brewing"), new RecipeType<CoffeeBrewingRecipe>(){

        public String toString() {
            return "coffee_brewing";
        }
    });

    /**
     * Modifies a single loot pool present in the provided builder.
     *
     * <p>This method can be used instead of simply adding a new pool
     * when you want the loot table to only drop items from one of the loot pool entries
     * instead of multiple.
     *
     * <p>Calling this method turns the loot pool at the specified index into a builder and rebuilds it back into a loot pool afterward.
     *
     * @param supplier the loot table builder
     * @param index    the list index of the target loot pool
     * @param modifier the modifying function
     */
    private static void modifyPool(LootTable.Builder supplier, int index, Consumer<? super LootPool.Builder> modifier) {
        LootPool lootPool = supplier.pools.get(index);
        LootPool.Builder poolBuilder = FabricLootPoolBuilder.copyOf(lootPool);
        modifier.accept(poolBuilder);
        supplier.pools.set(index, poolBuilder.build());
    }

    @Override
    public void onInitialize() {
        CaffeinatedBannerPatterns.initAndGetDefault(Registries.BANNER_PATTERN);
        CaffeinatedBlocks.register();
        CaffeinatedCauldronBehavior.register();
        CaffeinatedItemGroups.register();
        CaffeinatedItems.register();
        CaffeinatedParticleTypes.register();
        CaffeinatedPlacedFeatures.register();
        CaffeinatedSoundEvents.register();
        CaffeinatedStats.register();
        CaffeinatedStatusEffects.register();
        CaffeinatedTradeOffers.register();
        CaffeinatedBlockSoundGroup.register();
        CaffeinatedEntityTypeTags.register();
        CaffeinatedBlockTags.register();
        CaffeinatedItemTags.register();
        CaffeinatedBannerPatternTags.register();
        CaffeinatedBannerPatterns.register();

        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.equals(LootTables.JUNGLE_TEMPLE_CHEST)) {
                modifyPool(tableBuilder, 0, builder -> builder.with(ItemEntry.builder(CaffeinatedItems.COFFEE_BERRIES).weight(10).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 7.0F)))));
            }
            if (id.equals(LootTables.SHIPWRECK_SUPPLY_CHEST)) {
                modifyPool(tableBuilder, 0, builder -> builder.with(ItemEntry.builder(CaffeinatedItems.COFFEE_BERRIES).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F)))));
            }
            if (id.equals(LootTables.VILLAGE_SAVANNA_HOUSE_CHEST)) {
                modifyPool(tableBuilder, 0, builder -> builder.with(ItemEntry.builder(CaffeinatedItems.COFFEE_BERRIES).weight(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 7.0F)))));
            }
        });
    }
}
