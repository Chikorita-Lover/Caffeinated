package net.chikorita_lover.caffeinated.data;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.recipe.CoffeeBrewingRecipeJsonBuilder;
import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedItems;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class CaffeinatedRecipeProvider extends FabricRecipeProvider {
    public CaffeinatedRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        final RecipeExporter farmersDelightExporter = this.withConditions(exporter, ResourceConditions.allModsLoaded("farmersdelight"));

        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(CaffeinatedItems.COFFEE_BERRIES), RecipeCategory.MISC, CaffeinatedItems.COFFEE_BEANS, 0.3F, 200).criterion(hasItem(CaffeinatedItems.COFFEE_BERRIES), conditionsFromItem(CaffeinatedItems.COFFEE_BERRIES)).offerTo(exporter);
        CookingRecipeJsonBuilder.create(Ingredient.ofItems(CaffeinatedItems.COFFEE_BERRIES), RecipeCategory.MISC, CaffeinatedItems.COFFEE_BEANS, 0.3F, 100, RecipeSerializer.SMOKING, SmokingRecipe::new).criterion(hasItem(CaffeinatedItems.COFFEE_BERRIES), conditionsFromItem(CaffeinatedItems.COFFEE_BERRIES)).offerTo(exporter, Caffeinated.of(getItemPath(CaffeinatedItems.COFFEE_BEANS) + "_from_smoking"));
        CookingRecipeJsonBuilder.create(Ingredient.ofItems(CaffeinatedItems.COFFEE_BERRIES), RecipeCategory.MISC, CaffeinatedItems.COFFEE_BEANS, 0.3F, 600, RecipeSerializer.CAMPFIRE_COOKING, CampfireCookingRecipe::new).criterion(hasItem(CaffeinatedItems.COFFEE_BERRIES), conditionsFromItem(CaffeinatedItems.COFFEE_BERRIES)).offerTo(exporter, Caffeinated.of(getItemPath(CaffeinatedItems.COFFEE_BEANS) + "_from_campfire_cooking"));

        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, CaffeinatedItems.COFFEE_BEANS);
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, CaffeinatedBlocks.GROUND_COFFEE_BLOCK, CaffeinatedItems.GROUND_COFFEE);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedItems.COFFEE_BERRIES, 9).input(CaffeinatedBlocks.COFFEE_BERRY_CRATE).criterion(hasItem(CaffeinatedBlocks.COFFEE_BERRY_CRATE), conditionsFromItem(CaffeinatedBlocks.COFFEE_BERRY_CRATE)).offerTo(farmersDelightExporter, Caffeinated.of(getItemPath(CaffeinatedItems.COFFEE_BERRIES)));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, CaffeinatedBlocks.COFFEE_BERRY_CRATE).input('#', CaffeinatedItems.COFFEE_BERRIES).pattern("###").pattern("###").pattern("###").criterion(hasItem(CaffeinatedItems.COFFEE_BERRIES), conditionsFromItem(CaffeinatedItems.COFFEE_BERRIES)).offerTo(farmersDelightExporter, Caffeinated.of(getItemPath(CaffeinatedBlocks.COFFEE_BERRY_CRATE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.GROUND_COFFEE).input('#', CaffeinatedItems.COFFEE_BEANS).pattern("###").group("ground_coffee").criterion(hasItem(CaffeinatedItems.COFFEE_BEANS), conditionsFromItem(CaffeinatedItems.COFFEE_BEANS)).offerTo(exporter, Caffeinated.of("ground_coffee_from_coffee_beans"));
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.BROWN_DYE).input(CaffeinatedItems.GROUND_COFFEE).group("brown_dye").criterion("has_ground_coffee", conditionsFromItem(CaffeinatedItems.GROUND_COFFEE)).offerTo(exporter, Caffeinated.of("brown_dye_from_ground_coffee"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.JAVA_BANNER_PATTERN).input(Items.PAPER).input(CaffeinatedItemTags.BLACK_COFFEE_BOTTLES).criterion("has_black_coffee_bottle", conditionsFromTag(CaffeinatedItemTags.BLACK_COFFEE_BOTTLES)).offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedBlocks.TIRAMISU).input(CaffeinatedItemTags.BLACK_COFFEE_BOTTLES).input(Items.WHEAT).input(Items.SUGAR).input(ConventionalItemTags.MILK_BUCKETS).input(Items.EGG).group(getItemPath(CaffeinatedBlocks.TIRAMISU)).criterion("has_black_coffee_bottle", conditionsFromTag(CaffeinatedItemTags.BLACK_COFFEE_BOTTLES)).offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedBlocks.TIRAMISU).input('#', CaffeinatedItems.TIRAMISU_SLICE).pattern("##").pattern("##").group(getItemPath(CaffeinatedBlocks.TIRAMISU)).criterion(hasItem(CaffeinatedItems.TIRAMISU_SLICE), conditionsFromItem(CaffeinatedItems.TIRAMISU_SLICE)).offerTo(farmersDelightExporter, Caffeinated.of("tiramisu_from_slices"));

        CoffeeBrewingRecipeJsonBuilder.create(Ingredient.ofItems(Items.POTION), Ingredient.ofItems(CaffeinatedItems.GROUND_COFFEE), RecipeCategory.FOOD, CaffeinatedItems.COFFEE_BOTTLE, 1.0F, 600).criterion(hasItem(CaffeinatedItems.GROUND_COFFEE), conditionsFromItem(CaffeinatedItems.GROUND_COFFEE)).offerTo(exporter);
        CoffeeBrewingRecipeJsonBuilder.create(Ingredient.fromTag(CaffeinatedItemTags.BLACK_COFFEE_BOTTLES), Ingredient.fromTag(ConventionalItemTags.MILK_BUCKETS), RecipeCategory.FOOD, CaffeinatedItems.LATTE_COFFEE_BOTTLE, 1.0F, 600).criterion(hasItem(Items.MILK_BUCKET), conditionsFromTag(ConventionalItemTags.MILK_BUCKETS)).offerTo(exporter);
        CoffeeBrewingRecipeJsonBuilder.create(Ingredient.fromTag(CaffeinatedItemTags.BLACK_COFFEE_BOTTLES), Ingredient.ofItems(Items.HONEY_BOTTLE), RecipeCategory.FOOD, CaffeinatedItems.CAFE_MIEL_COFFEE_BOTTLE, 1.0F, 600).criterion(hasItem(Items.HONEY_BOTTLE), conditionsFromItem(Items.HONEY_BOTTLE)).offerTo(exporter);
    }
}
