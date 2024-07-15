package net.chikorita_lover.caffeinated;

import net.chikorita_lover.caffeinated.advancement.BrewCoffeeCriterion;
import net.chikorita_lover.caffeinated.entity.CivetEntity;
import net.chikorita_lover.caffeinated.recipe.CoffeeBrewingRecipe;
import net.chikorita_lover.caffeinated.registry.*;
import net.chikorita_lover.caffeinated.util.LootModificationUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caffeinated implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Caffeinated");
    public static final String NAMESPACE = "caffeinated";
    public static final BrewCoffeeCriterion BREW_COFFEE_CRITERION = Registry.register(Registries.CRITERION, of("brew_coffee"), new BrewCoffeeCriterion());
    public static final RecipeSerializer<CoffeeBrewingRecipe> COFFEE_BREWING_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, of("coffee_brewing"), new CoffeeBrewingRecipe.Serializer<CoffeeBrewingRecipe>(CoffeeBrewingRecipe::new, 600) {
    });

    public static Identifier of(String id) {
        return Identifier.of(NAMESPACE, id);
    }

    @Override
    public void onInitialize() {
        CaffeinatedBlockEntityTypes.register();
        CaffeinatedBlocks.register();
        CaffeinatedEntities.register();
        CaffeinatedItemGroups.register();
        CaffeinatedItems.register();
        CaffeinatedParticleTypes.register();
        CaffeinatedPlacedFeatures.register();
        CaffeinatedRecipeTypes.register();
        CaffeinatedSoundEvents.register();
        CaffeinatedStats.register();
        CaffeinatedStatusEffects.register();
        CaffeinatedTradeOffers.register();

        SpawnRestriction.register(CaffeinatedEntities.CIVET, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, CivetEntity::canSpawn);

        BiomeModifications.addSpawn(BiomeSelectors.tag(CaffeinatedBiomeTags.SPAWNS_CIVETS), SpawnGroup.CREATURE, CaffeinatedEntities.CIVET, 4, 1, 2);

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key.equals(LootTables.JUNGLE_TEMPLE_CHEST)) {
                tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(CaffeinatedItems.COFFEE_BERRIES)).with(EmptyEntry.builder()));
            }
            if (key.equals(LootTables.SHIPWRECK_SUPPLY_CHEST)) {
                LootModificationUtils.modifyPool(tableBuilder, 0, builder -> builder.with(ItemEntry.builder(CaffeinatedItems.COFFEE_BERRIES).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F)))));
            }
            if (key.equals(LootTables.VILLAGE_SAVANNA_HOUSE_CHEST)) {
                LootModificationUtils.modifyPool(tableBuilder, 0, builder -> builder.with(ItemEntry.builder(CaffeinatedItems.COFFEE_BERRIES).weight(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 7.0F)))));
            }
        });
    }
}
