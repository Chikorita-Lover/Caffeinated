package net.chikorita_lover.caffeinated.data;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class CaffeinatedRecipeProvider extends FabricRecipeProvider {
    protected static final Identifier COFFEE_BEANS_FROM_CIVET_SCAT = Caffeinated.of(convertBetween(CaffeinatedItems.COFFEE_BEANS, CaffeinatedBlocks.CIVET_SCAT));

    public CaffeinatedRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        final RecipeExporter farmersDelightExporter = this.withConditions(exporter, ResourceConditions.allModsLoaded("farmersdelight"));

        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(CaffeinatedItems.COFFEE_BERRIES), RecipeCategory.MISC, CaffeinatedItems.COFFEE_BEANS, 0.35F, 200).group(getItemPath(CaffeinatedItems.COFFEE_BEANS)).criterion(hasItem(CaffeinatedItems.COFFEE_BERRIES), conditionsFromItem(CaffeinatedItems.COFFEE_BERRIES)).offerTo(exporter);
        CookingRecipeJsonBuilder.createSmelting(Ingredient.ofItems(CaffeinatedBlocks.CIVET_SCAT), RecipeCategory.MISC, CaffeinatedItems.COFFEE_BEANS, 0.35F, 200).criterion(hasItem(CaffeinatedBlocks.CIVET_SCAT), conditionsFromItem(CaffeinatedBlocks.CIVET_SCAT)).offerTo(exporter, COFFEE_BEANS_FROM_CIVET_SCAT);

        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, CaffeinatedItems.COFFEE_BEANS);
        offer2x2CompactingRecipe(exporter, RecipeCategory.BUILDING_BLOCKS, CaffeinatedBlocks.GROUND_COFFEE_BLOCK, CaffeinatedItems.GROUND_COFFEE);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedItems.COFFEE_BERRIES, 9).input(CaffeinatedBlocks.COFFEE_BERRY_CRATE).criterion(hasItem(CaffeinatedBlocks.COFFEE_BERRY_CRATE), conditionsFromItem(CaffeinatedBlocks.COFFEE_BERRY_CRATE)).offerTo(farmersDelightExporter, Caffeinated.of(getItemPath(CaffeinatedItems.COFFEE_BERRIES)));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, CaffeinatedBlocks.COFFEE_BERRY_CRATE).input('#', CaffeinatedItems.COFFEE_BERRIES).pattern("###").pattern("###").pattern("###").criterion(hasItem(CaffeinatedItems.COFFEE_BERRIES), conditionsFromItem(CaffeinatedItems.COFFEE_BERRIES)).offerTo(farmersDelightExporter, Caffeinated.of(getItemPath(CaffeinatedBlocks.COFFEE_BERRY_CRATE)));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.GROUND_COFFEE).input('#', CaffeinatedItems.COFFEE_BEANS).pattern("###").group("ground_coffee").criterion(hasItem(CaffeinatedItems.COFFEE_BEANS), conditionsFromItem(CaffeinatedItems.COFFEE_BEANS)).offerTo(exporter, Caffeinated.of("ground_coffee_from_coffee_beans"));
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.BROWN_DYE).input(CaffeinatedItems.GROUND_COFFEE).group("brown_dye").criterion("has_ground_coffee", conditionsFromItem(CaffeinatedItems.GROUND_COFFEE)).offerTo(exporter, Caffeinated.of("brown_dye_from_ground_coffee"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, CaffeinatedBlocks.COFFEE_STAINED_WOOL).input(CaffeinatedItems.COFFEE_BOTTLE).input(Blocks.WHITE_WOOL).criterion(hasItem(CaffeinatedItems.COFFEE_BOTTLE), conditionsFromItem(CaffeinatedItems.COFFEE_BOTTLE)).offerTo(exporter);
        offerCarpetRecipe(exporter, CaffeinatedBlocks.COFFEE_STAINED_CARPET, CaffeinatedBlocks.COFFEE_STAINED_WOOL);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, CaffeinatedBlocks.COFFEE_STAINED_CARPET).input(CaffeinatedItems.COFFEE_BOTTLE).input(Blocks.WHITE_CARPET).criterion(hasItem(CaffeinatedItems.COFFEE_BOTTLE), conditionsFromItem(CaffeinatedItems.COFFEE_BOTTLE)).offerTo(exporter, Caffeinated.of("coffee_stained_carpet_from_white_carpet"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.JAVA_BANNER_PATTERN).input(Items.PAPER).input(CaffeinatedItems.COFFEE_BOTTLE).criterion(hasItem(CaffeinatedItems.COFFEE_BOTTLE), conditionsFromItem(CaffeinatedItems.COFFEE_BOTTLE)).offerTo(exporter);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedItems.TIRAMISU).input(CaffeinatedItems.COFFEE_BOTTLE).input(Items.WHEAT).input(Items.SUGAR).input(ConventionalItemTags.MILK_BUCKETS).input(Items.EGG).group(getItemPath(CaffeinatedItems.TIRAMISU)).criterion(hasItem(CaffeinatedItems.COFFEE_BOTTLE), conditionsFromItem(CaffeinatedItems.COFFEE_BOTTLE)).offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedItems.TIRAMISU).input('#', CaffeinatedItems.TIRAMISU_SLICE).pattern("##").pattern("##").group(getItemPath(CaffeinatedItems.TIRAMISU)).criterion(hasItem(CaffeinatedItems.TIRAMISU_SLICE), conditionsFromItem(CaffeinatedItems.TIRAMISU_SLICE)).offerTo(farmersDelightExporter, Caffeinated.of("tiramisu_from_slices"));
    }
}
