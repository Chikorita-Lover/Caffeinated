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
import net.minecraft.item.Items;
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

        public final void registerCoffeeShrub(BlockStateModelGenerator blockStateModelGenerator, Block crop, Block pottedCrop, Property<Integer> ageProperty, int... ageTextureIndices) {
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

            TextureMap textureMap = TextureMap.plant(TextureMap.getSubId(crop, "_stage1"));
            Identifier identifier = BlockStateModelGenerator.TintType.NOT_TINTED.getFlowerPotCrossModel().upload(pottedCrop, textureMap, blockStateModelGenerator.modelCollector);
            blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(pottedCrop, identifier));
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
            blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.COFFEE_BEAN_BLOCK);
            blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.GROUND_COFFEE_BLOCK);
            blockStateModelGenerator.registerSingleton(ModBlocks.COFFEE_BERRY_CRATE, new TextureMap().put(TextureKey.SIDE, new Identifier(Caffeinated.MODID, "block/coffee_berry_crate_side")).put(TextureKey.TOP, new Identifier(Caffeinated.MODID, "block/coffee_berry_crate_top")).put(TextureKey.BOTTOM, new Identifier("farmersdelight", "block/crate_bottom")), Models.CUBE_BOTTOM_TOP);

            blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.COFFEE_CAULDRON).coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1.upload(ModBlocks.COFFEE_CAULDRON, "_level1", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2.upload(ModBlocks.COFFEE_CAULDRON, "_level2", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL.upload(ModBlocks.COFFEE_CAULDRON, "_full", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector)))));

            blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.GROUND_COFFEE_CAULDRON).coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1.upload(ModBlocks.GROUND_COFFEE_CAULDRON, "_level1", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/ground_coffee_cauldron")), blockStateModelGenerator.modelCollector))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2.upload(ModBlocks.GROUND_COFFEE_CAULDRON, "_level2", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/ground_coffee_cauldron")), blockStateModelGenerator.modelCollector))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL.upload(ModBlocks.GROUND_COFFEE_CAULDRON, "_full", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/ground_coffee_cauldron")), blockStateModelGenerator.modelCollector)))));

            registerCoffeeShrub(blockStateModelGenerator, ModBlocks.COFFEE_SHRUB, ModBlocks.POTTED_COFFEE_SHRUB, Properties.AGE_3, 0, 0, 1, 1);
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            itemModelGenerator.register(ModItems.GROUND_COFFEE, Models.GENERATED);

            itemModelGenerator.register(ModItems.COFFEE_BERRIES, Models.GENERATED);
            itemModelGenerator.register(ModItems.COFFEE_BOTTLE, Models.GENERATED);
            itemModelGenerator.register(ModItems.TIRAMISU, Models.GENERATED);
        }
    }

    private static class CaffeinatedRecipeGenerator extends FabricRecipeProvider {
        private CaffeinatedRecipeGenerator(FabricDataGenerator generator) {
            super(generator);
        }

        @Override
        protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
            offerReversibleCompactingRecipes(exporter, ModItems.COFFEE_BEANS, ModBlocks.COFFEE_BEAN_BLOCK, "coffee_bean_block", null, "coffee_beans_from_block", "coffee_beans");

            offerReversibleCompactingRecipes(exporter, ModItems.GROUND_COFFEE, ModBlocks.GROUND_COFFEE_BLOCK, "ground_coffee_block", null, "ground_coffee_from_block", "ground_coffee");

            ShapelessRecipeJsonBuilder.create(ModItems.COFFEE_BERRIES, 9).input(ModBlocks.COFFEE_BERRY_CRATE).criterion(hasItem(ModBlocks.COFFEE_BERRY_CRATE), conditionsFromItem(ModBlocks.COFFEE_BERRY_CRATE)).offerTo(exporter);

            ShapelessRecipeJsonBuilder.create(ModItems.COFFEE_BEANS).input(ModItems.COFFEE_BERRIES).group("coffee_beans").criterion(hasItem(ModItems.COFFEE_BERRIES), conditionsFromItem(ModItems.COFFEE_BERRIES)).offerTo(exporter, new Identifier(Caffeinated.MODID, "coffee_beans_from_coffee_berries"));

            ShapedRecipeJsonBuilder.create(ModItems.GROUND_COFFEE).input('#', ModItems.COFFEE_BEANS).pattern("###").group("ground_coffee").criterion(hasItem(ModItems.COFFEE_BEANS), conditionsFromItem(ModItems.COFFEE_BEANS)).offerTo(exporter, new Identifier(Caffeinated.MODID, "ground_coffee_from_coffee_beans"));

            ShapedRecipeJsonBuilder.create(ModItems.TIRAMISU, 2).input('#', ModItems.COFFEE_BOTTLE).input('C', Items.COCOA_BEANS).input('E', Items.EGG).input('M', Items.MILK_BUCKET).input('W', Items.WHEAT).pattern("C#C").pattern("EME").pattern("WWW").group("tiramisu").criterion(hasItem(ModItems.COFFEE_BOTTLE), conditionsFromItem(ModItems.COFFEE_BOTTLE)).offerTo(exporter);
        }

        public static void offerReversibleCompactingRecipes(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible compacted, String compactingRecipeName, @Nullable String compactingRecipeGroup, String reverseRecipeName, @Nullable String reverseRecipeGroup) {
            ShapelessRecipeJsonBuilder.create(input, 9).input(compacted).group(reverseRecipeGroup).criterion(hasItem(compacted), conditionsFromItem(compacted)).offerTo(exporter, new Identifier(Caffeinated.MODID, reverseRecipeName));
            ShapedRecipeJsonBuilder.create(compacted).input('#', input).pattern("###").pattern("###").pattern("###").group(compactingRecipeGroup).criterion(hasItem(input), conditionsFromItem(input)).offerTo(exporter, new Identifier(Caffeinated.MODID, compactingRecipeName));
        }
    }
}
