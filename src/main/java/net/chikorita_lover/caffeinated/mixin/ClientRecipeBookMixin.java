package net.chikorita_lover.caffeinated.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.chikorita_lover.caffeinated.registry.CaffeinatedRecipeTypes;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeEntry;
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
    private static void getGroupForRecipe(RecipeEntry<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if (recipe.value().getType() == CaffeinatedRecipeTypes.COFFEE_BREWING) {
            cir.setReturnValue(COFFEE_BREWING);
        }
    }

    @ModifyReturnValue(method = "getGroupForRecipe", at = @At("RETURN"))
    private static RecipeBookGroup getGroupForCookingRecipe(RecipeBookGroup group, @Local(argsOnly = true) RecipeEntry<?> recipe) {
        if (recipe.value() instanceof AbstractCookingRecipe cookingRecipe) {
            CookingRecipeCategory category = cookingRecipe.getCategory();
            if (group == RecipeBookGroup.SMOKER_FOOD && category != CookingRecipeCategory.FOOD) {
                return SMOKER_MISC;
            }
        }
        return group;
    }
}
