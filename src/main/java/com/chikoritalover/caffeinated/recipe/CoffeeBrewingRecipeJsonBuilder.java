package com.chikoritalover.caffeinated.recipe;

import com.chikoritalover.caffeinated.Caffeinated;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.CriterionMerger;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CoffeeBrewingRecipeJsonBuilder implements CraftingRecipeJsonBuilder {
    private final RecipeCategory category;
    private final Item output;
    private final Ingredient input;
    private final Ingredient reagent;
    private final float experience;
    private final int brewingTime;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.createUntelemetered();
    @Nullable
    private String group;

    private CoffeeBrewingRecipeJsonBuilder(RecipeCategory category, ItemConvertible output, Ingredient input, Ingredient reagent, float experience, int brewingTime) {
        this.category = category;
        this.output = output.asItem();
        this.input = input;
        this.reagent = reagent;
        this.experience = experience;
        this.brewingTime = brewingTime;
    }

    public static CoffeeBrewingRecipeJsonBuilder create(Ingredient input, Ingredient reagent, RecipeCategory category, ItemConvertible output, float experience, int cookingTime) {
        return new CoffeeBrewingRecipeJsonBuilder(category, output, input, reagent, experience, cookingTime);
    }

    @Override
    public CoffeeBrewingRecipeJsonBuilder criterion(String string, CriterionConditions criterionConditions) {
        this.advancementBuilder.criterion(string, criterionConditions);
        return this;
    }

    @Override
    public CoffeeBrewingRecipeJsonBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    @Override
    public Item getOutputItem() {
        return this.output;
    }

    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier recipeId) {
        this.validate(recipeId);
        this.advancementBuilder.parent(ROOT).criterion("has_the_recipe", RecipeUnlockedCriterion.create(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId)).criteriaMerger(CriterionMerger.OR);
        exporter.accept(new CoffeeBrewingRecipeJsonBuilder.CoffeeBrewingRecipeJsonProvider(recipeId, this.group == null ? "" : this.group, this.input, this.reagent, this.output, this.experience, this.brewingTime, this.advancementBuilder, recipeId.withPrefixedPath("recipes/" + this.category.getName() + "/")));
    }

    private void validate(Identifier recipeId) {
        if (this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
    }

    @Override
    public void offerTo(Consumer<RecipeJsonProvider> exporter, String recipePath) {
        Identifier identifier2 = new Identifier(Caffeinated.MODID, recipePath);
        Identifier identifier = CraftingRecipeJsonBuilder.getItemId(this.getOutputItem());
        if (identifier2.equals(identifier)) {
            throw new IllegalStateException("Recipe " + recipePath + " should remove its 'save' argument as it is equal to default one");
        }
        this.offerTo(exporter, identifier2);
    }

    static class CoffeeBrewingRecipeJsonProvider implements RecipeJsonProvider {
        private final Identifier recipeId;
        private final String group;
        private final Ingredient input;
        private final Ingredient reagent;
        private final Item result;
        private final float experience;
        private final int brewingtime;
        private final Advancement.Builder advancementBuilder;
        private final Identifier advancementId;
        private final RecipeSerializer<CoffeeBrewingRecipe> serializer;

        public CoffeeBrewingRecipeJsonProvider(Identifier recipeId, String group, Ingredient input, Ingredient reagent, Item result, float experience, int brewingTime, Advancement.Builder advancementBuilder, Identifier advancementId) {
            this.recipeId = recipeId;
            this.group = group;
            this.input = input;
            this.reagent = reagent;
            this.result = result;
            this.experience = experience;
            this.brewingtime = brewingTime;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
            this.serializer = Caffeinated.COFFEE_BREWING_SERIALIZER;
        }

        @Override
        public void serialize(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }
            json.add("ingredient", this.input.toJson());
            json.add("reagent", this.reagent.toJson());
            json.addProperty("result", Registries.ITEM.getId(this.result).toString());
            json.addProperty("experience", this.experience);
            json.addProperty("brewingtime", this.brewingtime);
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return this.serializer;
        }

        @Override
        public Identifier getRecipeId() {
            return this.recipeId;
        }

        @Override
        @Nullable
        public JsonObject toAdvancementJson() {
            return this.advancementBuilder.toJson();
        }

        @Override
        @Nullable
        public Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}
