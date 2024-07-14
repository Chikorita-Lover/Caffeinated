package net.chikorita_lover.caffeinated.data;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CaffeinatedRecipeProvider extends FabricRecipeProvider {
    public CaffeinatedRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    private static void offerReversibleCompactingRecipes(RecipeExporter exporter, ItemConvertible input, ItemConvertible compacted, String compactingRecipeName, @Nullable String compactingRecipeGroup, String reverseRecipeName, @Nullable String reverseRecipeGroup) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, input, 9).input(compacted).group(reverseRecipeGroup).criterion(hasItem(compacted), conditionsFromItem(compacted)).offerTo(exporter, Caffeinated.of(reverseRecipeName));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, compacted).input('#', input).pattern("###").pattern("###").pattern("###").group(compactingRecipeGroup).criterion(hasItem(input), conditionsFromItem(input)).offerTo(exporter, Caffeinated.of(compactingRecipeName));
    }

    @Override
    public void generate(RecipeExporter exporter) {
        final RecipeExporter farmersDelightExporter = this.withConditions(exporter, ResourceConditions.allModsLoaded("farmersdelight"));

        offerReversibleCompactingRecipes(exporter, CaffeinatedItems.COFFEE_BEANS, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, "coffee_bean_block", null, "coffee_beans_from_block", "coffee_beans");
        offerReversibleCompactingRecipes(exporter, CaffeinatedItems.GROUND_COFFEE, CaffeinatedBlocks.GROUND_COFFEE_BLOCK, "ground_coffee_block", null, "ground_coffee_from_block", "ground_coffee");
        offerReversibleCompactingRecipes(farmersDelightExporter, RecipeCategory.FOOD, CaffeinatedItems.COFFEE_BERRIES, RecipeCategory.DECORATIONS, CaffeinatedBlocks.COFFEE_BERRY_CRATE);

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.COFFEE_BEANS).input(CaffeinatedItems.COFFEE_BERRIES).group("coffee_beans").criterion(hasItem(CaffeinatedItems.COFFEE_BERRIES), conditionsFromItem(CaffeinatedItems.COFFEE_BERRIES)).offerTo(exporter, Caffeinated.of("coffee_beans_from_coffee_berries"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.GROUND_COFFEE).input('#', CaffeinatedItems.COFFEE_BEANS).pattern("###").group("ground_coffee").criterion(hasItem(CaffeinatedItems.COFFEE_BEANS), conditionsFromItem(CaffeinatedItems.COFFEE_BEANS)).offerTo(exporter, Caffeinated.of("ground_coffee_from_coffee_beans"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, CaffeinatedItems.JAVA_BANNER_PATTERN).input(Items.PAPER).input(CaffeinatedItems.COFFEE_BOTTLE).criterion(hasItem(CaffeinatedItems.COFFEE_BOTTLE), conditionsFromItem(CaffeinatedItems.COFFEE_BOTTLE)).offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, CaffeinatedItems.TIRAMISU, 2).input('#', CaffeinatedItems.COFFEE_BOTTLE).input('C', Items.COCOA_BEANS).input('E', Items.EGG).input('M', Items.MILK_BUCKET).input('W', Items.WHEAT).pattern("C#C").pattern("EME").pattern("WWW").group("tiramisu").criterion(hasItem(CaffeinatedItems.COFFEE_BOTTLE), conditionsFromItem(CaffeinatedItems.COFFEE_BOTTLE)).offerTo(exporter);
    }
}
