package net.chikorita_lover.caffeinated.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import net.chikorita_lover.caffeinated.registry.CaffeinatedRecipeTypes;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.RecipeEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Unique
    private static final RecipeBookGroup COFFEE_BREWING = ClassTinkerers.getEnum(RecipeBookGroup.class, "CAFFEINATED_COFFEE_BREWING");

    @Inject(method = "getGroupForRecipe", at = @At("HEAD"), cancellable = true)
    private static void getGroupForRecipe(RecipeEntry<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if (recipe.value().getType() == CaffeinatedRecipeTypes.COFFEE_BREWING) {
            cir.setReturnValue(COFFEE_BREWING);
        }
    }
}
