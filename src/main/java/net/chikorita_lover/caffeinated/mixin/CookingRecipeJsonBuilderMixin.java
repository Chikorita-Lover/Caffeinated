package net.chikorita_lover.caffeinated.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.book.CookingRecipeCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CookingRecipeJsonBuilder.class)
public class CookingRecipeJsonBuilderMixin {
    @ModifyReturnValue(method = "getCookingRecipeCategory", at = @At("RETURN"))
    private static CookingRecipeCategory getCookingRecipeCategory(CookingRecipeCategory category, @Local(argsOnly = true) ItemConvertible output) {
        if (category == CookingRecipeCategory.FOOD && !output.asItem().getComponents().contains(DataComponentTypes.FOOD)) {
            return CookingRecipeCategory.MISC;
        }
        return category;
    }
}
