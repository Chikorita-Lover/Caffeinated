package com.chikoritalover.caffeinated;

import com.chikoritalover.caffeinated.block.CauldronCampfireBlock;
import com.chikoritalover.caffeinated.block.TiramisuBlock;
import com.chikoritalover.caffeinated.recipe.CoffeeBrewingRecipeJsonBuilder;
import com.chikoritalover.caffeinated.registry.CaffeinatedBlocks;
import com.chikoritalover.caffeinated.registry.CaffeinatedItemTags;
import com.chikoritalover.caffeinated.registry.CaffeinatedItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.Optional;
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

        public final void registerCauldronCampfire(BlockStateModelGenerator blockStateModelGenerator, Block cauldronCampfire, Block campfire) {
            Identifier identifier = new Identifier(Caffeinated.MODID, "block/cauldron_campfire_off");
            Identifier identifier2 = new Model(Optional.of(new Identifier(Caffeinated.MODID, "block/template_cauldron_campfire")), Optional.empty(), TextureKey.FIRE, TextureKey.LIT_LOG).upload(cauldronCampfire, TextureMap.campfire(campfire), blockStateModelGenerator.modelCollector);
            Identifier identifier3 = new Identifier(Caffeinated.MODID, "block/cauldron_campfire_off_filled");
            Identifier identifier4 = new Model(Optional.of(new Identifier(Caffeinated.MODID, "block/template_cauldron_campfire_filled")), Optional.empty(), TextureKey.FIRE, TextureKey.LIT_LOG).upload(ModelIds.getBlockSubModelId(cauldronCampfire, "_filled"), TextureMap.campfire(campfire), blockStateModelGenerator.modelCollector);
            blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(cauldronCampfire).coordinate(BlockStateModelGenerator.createSouthDefaultHorizontalRotationStates()).coordinate(BlockStateVariantMap.create(Properties.LIT, CauldronCampfireBlock.FILLED).register(false, false, BlockStateVariant.create().put(VariantSettings.MODEL, identifier)).register(true, false, BlockStateVariant.create().put(VariantSettings.MODEL, identifier2)).register(false, true, BlockStateVariant.create().put(VariantSettings.MODEL, identifier3)).register(true, true, BlockStateVariant.create().put(VariantSettings.MODEL, identifier4))));
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

        private void registerTiramisu(BlockStateModelGenerator generator) {
            generator.registerItemModel(CaffeinatedBlocks.TIRAMISU.asItem());
            generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(CaffeinatedBlocks.TIRAMISU).coordinate(BlockStateVariantMap.create(TiramisuBlock.SLICES).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(CaffeinatedBlocks.TIRAMISU, "_slice3"))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(CaffeinatedBlocks.TIRAMISU, "_slice2"))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockSubModelId(CaffeinatedBlocks.TIRAMISU, "_slice1"))).register(4, BlockStateVariant.create().put(VariantSettings.MODEL, ModelIds.getBlockModelId(CaffeinatedBlocks.TIRAMISU)))));
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
            blockStateModelGenerator.registerSimpleCubeAll(CaffeinatedBlocks.COFFEE_BEAN_BLOCK);
            blockStateModelGenerator.registerSimpleCubeAll(CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
            blockStateModelGenerator.registerSingleton(CaffeinatedBlocks.COFFEE_BERRY_CRATE, new TextureMap().put(TextureKey.SIDE, new Identifier(Caffeinated.MODID, "block/coffee_berry_crate_side")).put(TextureKey.TOP, new Identifier(Caffeinated.MODID, "block/coffee_berry_crate_top")).put(TextureKey.BOTTOM, new Identifier("farmersdelight", "block/crate_bottom")), Models.CUBE_BOTTOM_TOP);

            blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(CaffeinatedBlocks.COFFEE_CAULDRON).coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_level1", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_level2", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL.upload(CaffeinatedBlocks.COFFEE_CAULDRON, "_full", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/coffee")), blockStateModelGenerator.modelCollector)))));

            blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON).coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL).register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_level1", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/ground_coffee_cauldron")), blockStateModelGenerator.modelCollector))).register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_level2", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/ground_coffee_cauldron")), blockStateModelGenerator.modelCollector))).register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL.upload(CaffeinatedBlocks.GROUND_COFFEE_CAULDRON, "_full", TextureMap.cauldron(new Identifier(Caffeinated.MODID, "block/ground_coffee_cauldron")), blockStateModelGenerator.modelCollector)))));

            registerCoffeeShrub(blockStateModelGenerator, CaffeinatedBlocks.COFFEE_SHRUB, CaffeinatedBlocks.POTTED_COFFEE_SHRUB, Properties.AGE_3, 0, 0, 1, 1);

            registerTiramisu(blockStateModelGenerator);

            for (Block campfireBlock : CauldronCampfireBlock.CAMPFIRE_TO_CAULDRON_CAMPFIRE.keySet()) {
                registerCauldronCampfire(blockStateModelGenerator, CauldronCampfireBlock.CAMPFIRE_TO_CAULDRON_CAMPFIRE.get(campfireBlock), campfireBlock);
            }
        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            itemModelGenerator.register(CaffeinatedItems.COFFEE_BEANS, Models.GENERATED);
            itemModelGenerator.register(CaffeinatedItems.BLACK_COFFEE_BOTTLE, Models.GENERATED);
            itemModelGenerator.register(CaffeinatedItems.LATTE_COFFEE_BOTTLE, Models.GENERATED);
            itemModelGenerator.register(CaffeinatedItems.GROUND_COFFEE, Models.GENERATED);
            itemModelGenerator.register(CaffeinatedItems.JAVA_BANNER_PATTERN, Models.GENERATED);
            itemModelGenerator.register(CaffeinatedItems.TIRAMISU_SLICE, Models.GENERATED);
            itemModelGenerator.register(CaffeinatedItems.CIVET_SPAWN_EGG, Models.item("template_spawn_egg"));
        }
    }

    private static class CaffeinatedRecipeGenerator extends FabricRecipeProvider {
        private CaffeinatedRecipeGenerator(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generate(Consumer<RecipeJsonProvider> exporter) {
            offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, CaffeinatedItems.COFFEE_BEANS);
            offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, CaffeinatedBlocks.GROUND_COFFEE_BLOCK, CaffeinatedItems.GROUND_COFFEE);

            CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(CaffeinatedItems.COFFEE_BERRIES), RecipeCategory.MISC, CaffeinatedItems.COFFEE_BEANS, 0.3F, 200).criterion(hasItem(CaffeinatedItems.COFFEE_BERRIES), conditionsFromItem(CaffeinatedItems.COFFEE_BERRIES)).offerTo(exporter);
            CookingRecipeJsonBuilder.createSmoking(Ingredient.ofItems(CaffeinatedItems.COFFEE_BERRIES), RecipeCategory.MISC, CaffeinatedItems.COFFEE_BEANS, 0.3F, 100).criterion(hasItem(CaffeinatedItems.COFFEE_BERRIES), conditionsFromItem(CaffeinatedItems.COFFEE_BERRIES)).offerTo(exporter, new Identifier(Caffeinated.MODID, getItemPath(CaffeinatedItems.COFFEE_BEANS) + "_from_smoking"));
            CookingRecipeJsonBuilder.createCampfireCooking(Ingredient.ofItems(CaffeinatedItems.COFFEE_BERRIES), RecipeCategory.MISC, CaffeinatedItems.COFFEE_BEANS, 0.3F, 600).criterion(hasItem(CaffeinatedItems.COFFEE_BERRIES), conditionsFromItem(CaffeinatedItems.COFFEE_BERRIES)).offerTo(exporter, new Identifier(Caffeinated.MODID, getItemPath(CaffeinatedItems.COFFEE_BEANS) + "_from_campfire_cooking"));

            ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.GROUND_COFFEE, 3).input('#', CaffeinatedItems.COFFEE_BEANS).pattern("###").criterion(hasItem(CaffeinatedItems.COFFEE_BEANS), conditionsFromItem(CaffeinatedItems.COFFEE_BEANS)).offerTo(exporter);
            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.JAVA_BANNER_PATTERN).input(Items.PAPER).input(CaffeinatedItemTags.COFFEE_BOTTLES).criterion("has_coffee_bottle", conditionsFromTag(CaffeinatedItemTags.COFFEE_BOTTLES)).offerTo(exporter);
            ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.BROWN_DYE).input(CaffeinatedItems.GROUND_COFFEE).group("brown_dye").criterion("has_ground_coffee", conditionsFromItem(CaffeinatedItems.GROUND_COFFEE)).offerTo(exporter, new Identifier(Caffeinated.MODID, "brown_dye_from_ground_coffee"));

            ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedItems.COFFEE_BERRIES, 9).input(CaffeinatedBlocks.COFFEE_BERRY_CRATE).criterion("has_coffee_berry_crate", conditionsFromItem(CaffeinatedBlocks.COFFEE_BERRY_CRATE)).offerTo(exporter);
            ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedBlocks.TIRAMISU).input(CaffeinatedItemTags.BLACK_COFFEE_BOTTLES).input(Items.WHEAT).input(Items.SUGAR).input(ConventionalItemTags.MILK_BUCKETS).input(Items.EGG).group("tiramisu").criterion("has_black_coffee_bottle", conditionsFromTag(CaffeinatedItemTags.BLACK_COFFEE_BOTTLES)).offerTo(exporter);
            ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedBlocks.TIRAMISU).input('#', CaffeinatedItems.TIRAMISU_SLICE).pattern("##").pattern("##").group("tiramisu").criterion("has_tiramisu_slice", conditionsFromItem(CaffeinatedItems.TIRAMISU_SLICE)).offerTo(exporter, new Identifier(Caffeinated.MODID, "tiramisu_from_slices"));

            CoffeeBrewingRecipeJsonBuilder.create(Ingredient.ofItems(Items.POTION), Ingredient.ofItems(CaffeinatedItems.GROUND_COFFEE), RecipeCategory.FOOD, CaffeinatedItems.BLACK_COFFEE_BOTTLE, 1.0F, 600).criterion(hasItem(CaffeinatedItems.GROUND_COFFEE), conditionsFromItem(CaffeinatedItems.GROUND_COFFEE)).offerTo(exporter);
            CoffeeBrewingRecipeJsonBuilder.create(Ingredient.fromTag(CaffeinatedItemTags.BLACK_COFFEE_BOTTLES), Ingredient.fromTag(ConventionalItemTags.MILK_BUCKETS), RecipeCategory.FOOD, CaffeinatedItems.LATTE_COFFEE_BOTTLE, 1.0F, 600).criterion(hasItem(Items.MILK_BUCKET), conditionsFromTag(ConventionalItemTags.MILK_BUCKETS)).offerTo(exporter);
        }
    }
}
