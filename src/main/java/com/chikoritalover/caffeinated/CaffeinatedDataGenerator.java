package com.chikoritalover.caffeinated;

import com.chikoritalover.caffeinated.registry.CaffeinatedBlocks;
import com.chikoritalover.caffeinated.registry.CaffeinatedItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
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
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CaffeinatedDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(CaffeinatedModelGenerator::new);
        pack.addProvider(CaffeinatedRecipeGenerator::new);
    }

    private static class CaffeinatedModelGenerator extends FabricModelProvider {
        private CaffeinatedModelGenerator(FabricDataOutput output) {
            super(output);
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
            blockStateModelGenerator.registerSimpleCubeAll(CaffeinatedBlocks.COFFEE_BEAN_BLOCK);
            blockStateModelGenerator.registerSimpleCubeAll(CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
            blockStateModelGenerator.registerSingleton(CaffeinatedBlocks.COFFEE_BERRY_CRATE, new TextureMap().put(TextureKey.SIDE, new Identifier(Caffeinated.MODID, "block/coffee_berry_crate_side")).put(TextureKey.TOP, new Identifier(Caffeinated.MODID, "block/coffee_berry_crate_top")).put(TextureKey.BOTTOM, new Identifier("farmersdelight", "block/crate_bottom")), Models.CUBE_BOTTOM_TOP);

            blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(CaffeinatedBlocks.COFFEE_CAULDRON).coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_level1", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_level2", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_full", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector)))));

            blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON).coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_level1", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/ground_coffee_cauldron")), blockStateModelGenerator.modelCollector))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_level2", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/ground_coffee_cauldron")), blockStateModelGenerator.modelCollector))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_full", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/ground_coffee_cauldron")), blockStateModelGenerator.modelCollector)))));

            registerCoffeeShrub(blockStateModelGenerator, CaffeinatedBlocks.COFFEE_SHRUB, CaffeinatedBlocks.POTTED_COFFEE_SHRUB, Properties.AGE_3, 0, 0, 1, 1);
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            itemModelGenerator.register(CaffeinatedItems.GROUND_COFFEE, Models.GENERATED);
            itemModelGenerator.register(CaffeinatedItems.JAVA_BANNER_PATTERN, Models.GENERATED);

            itemModelGenerator.register(CaffeinatedItems.COFFEE_BERRIES, Models.GENERATED);
            itemModelGenerator.register(CaffeinatedItems.COFFEE_BOTTLE, Models.GENERATED);
            itemModelGenerator.register(CaffeinatedItems.TIRAMISU, Models.GENERATED);
        }
    }

    private static class CaffeinatedRecipeGenerator extends FabricRecipeProvider {
        private CaffeinatedRecipeGenerator(FabricDataOutput output) {
            super(output);
        }

        public static void offerReversibleCompactingRecipes(Consumer<RecipeJsonProvider> exporter, ItemConvertible input, ItemConvertible compacted, String compactingRecipeName, @Nullable String compactingRecipeGroup, String reverseRecipeName, @Nullable String reverseRecipeGroup) {
            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, input, 9).input(compacted).group(reverseRecipeGroup).criterion(hasItem(compacted), conditionsFromItem(compacted)).offerTo(exporter, new Identifier(Caffeinated.MODID, reverseRecipeName));
            ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, compacted).input('#', input).pattern("###").pattern("###").pattern("###").group(compactingRecipeGroup).criterion(hasItem(input), conditionsFromItem(input)).offerTo(exporter, new Identifier(Caffeinated.MODID, compactingRecipeName));
        }

        @Override
        public void generate(Consumer<RecipeJsonProvider> exporter) {
            offerReversibleCompactingRecipes(exporter, CaffeinatedItems.COFFEE_BEANS, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, "coffee_bean_block", null, "coffee_beans_from_block", "coffee_beans");

            offerReversibleCompactingRecipes(exporter, CaffeinatedItems.GROUND_COFFEE, CaffeinatedBlocks.GROUND_COFFEE_BLOCK, "ground_coffee_block", null, "ground_coffee_from_block", "ground_coffee");

            ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedItems.COFFEE_BERRIES, 9).input(CaffeinatedBlocks.COFFEE_BERRY_CRATE).criterion(hasItem(CaffeinatedBlocks.COFFEE_BERRY_CRATE), conditionsFromItem(CaffeinatedBlocks.COFFEE_BERRY_CRATE)).offerTo(exporter);

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.COFFEE_BEANS).input(CaffeinatedItems.COFFEE_BERRIES).group("coffee_beans").criterion(hasItem(CaffeinatedItems.COFFEE_BERRIES), conditionsFromItem(CaffeinatedItems.COFFEE_BERRIES)).offerTo(exporter, new Identifier(Caffeinated.MODID, "coffee_beans_from_coffee_berries"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.GROUND_COFFEE).input('#', CaffeinatedItems.COFFEE_BEANS).pattern("###").group("ground_coffee").criterion(hasItem(CaffeinatedItems.COFFEE_BEANS), conditionsFromItem(CaffeinatedItems.COFFEE_BEANS)).offerTo(exporter, new Identifier(Caffeinated.MODID, "ground_coffee_from_coffee_beans"));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.JAVA_BANNER_PATTERN).input(Items.PAPER).input(CaffeinatedItems.COFFEE_BOTTLE).criterion(hasItem(CaffeinatedItems.COFFEE_BOTTLE), conditionsFromItem(CaffeinatedItems.COFFEE_BOTTLE)).offerTo(exporter);

            ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedItems.TIRAMISU, 2).input('#', CaffeinatedItems.COFFEE_BOTTLE).input('C', Items.COCOA_BEANS).input('E', Items.EGG).input('M', Items.MILK_BUCKET).input('W', Items.WHEAT).pattern("C#C").pattern("EME").pattern("WWW").group("tiramisu").criterion(hasItem(CaffeinatedItems.COFFEE_BOTTLE), conditionsFromItem(CaffeinatedItems.COFFEE_BOTTLE)).offerTo(exporter);
        }
    }
}
