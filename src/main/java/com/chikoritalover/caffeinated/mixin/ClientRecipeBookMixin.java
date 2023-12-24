package com.chikoritalover.caffeinated.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.CookingRecipeCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    private static final RecipeBookGroup SMOKER_MISC = ClassTinkerers.getEnum(RecipeBookGroup.class, "CAFFEINATED_SMOKER_MISC");

    @Inject(method = "getGroupForRecipe", at = @At("RETURN"), cancellable = true)
    private static void getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if (recipe instanceof AbstractCookingRecipe abstractCookingRecipe) {
            CookingRecipeCategory cookingRecipeCategory = abstractCookingRecipe.getCategory();
            if (cir.getReturnValue() == RecipeBookGroup.SMOKER_FOOD && cookingRecipeCategory == CookingRecipeCategory.MISC) {
                cir.setReturnValue(SMOKER_MISC);
            }
        }
    }
}
