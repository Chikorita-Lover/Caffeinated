package com.chikoritalover.caffeinated.mixin;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.CookingRecipeCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Unique
    private static final RecipeBookGroup SMOKER_MISC = ClassTinkerers.getEnum(RecipeBookGroup.class, "CAFFEINATED_SMOKER_MISC");
    @Unique
    private static final RecipeBookGroup COFFEE_BREWING = ClassTinkerers.getEnum(RecipeBookGroup.class, "CAFFEINATED_COFFEE_BREWING");

    @Inject(method = "getGroupForRecipe", at = @At("HEAD"), cancellable = true)
    private static void getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if (recipe.getType() == Caffeinated.COFFEE_BREWING) {
            cir.setReturnValue(COFFEE_BREWING);
        }
    }

    @Inject(method = "getGroupForRecipe", at = @At("RETURN"), cancellable = true)
    private static void getGroupForCookingRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if (recipe instanceof AbstractCookingRecipe abstractCookingRecipe) {
            CookingRecipeCategory cookingRecipeCategory = abstractCookingRecipe.getCategory();
            if (cir.getReturnValue() == RecipeBookGroup.SMOKER_FOOD && cookingRecipeCategory == CookingRecipeCategory.MISC) {
                cir.setReturnValue(SMOKER_MISC);
            }
        }
    }
}
