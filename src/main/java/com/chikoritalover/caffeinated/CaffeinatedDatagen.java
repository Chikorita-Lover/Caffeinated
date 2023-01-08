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
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

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
            blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.COFFEE_BEAN_BLOCK);
            blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GROUND_COFFEE_BLOCK);

            blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.COFFEE_CAULDRON).coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1.upload(ModBlocks.COFFEE_CAULDRON, "_level1", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2.upload(ModBlocks.COFFEE_CAULDRON, "_level2", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL.upload(ModBlocks.COFFEE_CAULDRON, "_full", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector)))));

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
            offerReversibleCompactingRecipes(exporter, ModItems.COFFEE_BEANS, ModBlocks.COFFEE_BEAN_BLOCK, "coffee_bean_block", null, "coffee_beans_from_block", null);

            offerReversibleCompactingRecipes(exporter, ModItems.GROUND_COFFEE, ModBlocks.GROUND_COFFEE_BLOCK, "ground_coffee_block", null, "ground_coffee_from_block", "ground_coffee");

            offerShapelessRecipe(exporter, ModItems.COFFEE_BEANS, ModItems.COFFEE_BERRIES, "coffee_beans", 1);

            offerShapelessRecipe(exporter, ModItems.GROUND_COFFEE, ModItems.COFFEE_BEANS, "ground_coffee", 2);
        }

        public static void offerReversibleCompactingRecipes(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible compacted, String compactingRecipeName, @Nullable String compactingRecipeGroup, String reverseRecipeName, @Nullable String reverseRecipeGroup) {
            ShapelessRecipeJsonBuilder.create(input, 9).input(compacted).group(reverseRecipeGroup).criterion(hasItem(compacted), conditionsFromItem(compacted)).offerTo(exporter, new Identifier(Caffeinated.MODID, reverseRecipeName));
            ShapedRecipeJsonBuilder.create(compacted).input('#', input).pattern("###").pattern("###").pattern("###").group(compactingRecipeGroup).criterion(hasItem(input), conditionsFromItem(input)).offerTo(exporter, new Identifier(Caffeinated.MODID, compactingRecipeName));
        }
    }
}
