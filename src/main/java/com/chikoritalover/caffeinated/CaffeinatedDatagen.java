package com.chikoritalover.caffeinated;

import com.chikoritalover.caffeinated.registry.ModBlocks;
import com.chikoritalover.caffeinated.registry.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

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

        public final void registerCoffeeShrub(BlockStateModelGenerator blockStateModelGenerator, Block crop, Property<Integer> ageProperty, int... ageTextureIndices) {
            if (ageProperty.getValues().size() != ageTextureIndices.length) {
                throw new IllegalArgumentException();
            } else {
                Int2ObjectMap<Identifier> int2ObjectMap = new Int2ObjectOpenHashMap<>();
                BlockStateVariantMap blockStateVariantMap = BlockStateVariantMap.create(ageProperty).register((integer) -> {
                    int i = ageTextureIndices[integer];
                    Identifier identifier = int2ObjectMap.computeIfAbsent(i, (j) -> blockStateModelGenerator.createSubModel(crop, "_stage" + i, Models.CROSS, TextureMap::cross));
                    return BlockStateVariant.create().put(VariantSettings.MODEL, identifier);
                });
                blockStateModelGenerator.registerItemModel(crop.asItem());
                blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(crop).coordinate(blockStateVariantMap));
            }
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
            registerCoffeeShrub(blockStateModelGenerator, ModBlocks.COFFEE_SHRUB, Properties.AGE_3, 0, 0, 1, 1);
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
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
