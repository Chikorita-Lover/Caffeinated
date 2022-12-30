package com.chikoritalover.caffeinated;

import com.chikoritalover.caffeinated.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public class CaffeinatedDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(CaffeinatedModelGenerator::new);
        fabricDataGenerator.addProvider(CaffeinatedRecipeGenerator::new);
    }

    private static class CaffeinatedModelGenerator extends FabricModelProvider {
        private CaffeinatedModelGenerator(FabricDataGenerator generator) {
            super(generator);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {}

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            itemModelGenerator.register(ModItems.COFFEE_BEANS, Models.GENERATED);
            itemModelGenerator.register(ModItems.GROUND_COFFEE, Models.GENERATED);

            itemModelGenerator.register(ModItems.COFFEE_BERRIES, Models.GENERATED);
            itemModelGenerator.register(ModItems.COFFEE_BOTTLE, Models.GENERATED);
        }
    }

    private static class CaffeinatedRecipeGenerator extends FabricRecipeProvider {
        private CaffeinatedRecipeGenerator(FabricDataGenerator generator) {
            super(generator);
        }

        @Override
        protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
            offerShapelessRecipe(exporter, ModItems.COFFEE_BEANS, ModItems.COFFEE_BERRIES, "coffee_beans", 1);

            offerShapelessRecipe(exporter, ModItems.GROUND_COFFEE, ModItems.COFFEE_BEANS, "ground_coffee", 2);
        }
    }
}
