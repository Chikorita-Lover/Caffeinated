package com.chikoritalover.caffeinated.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import net.minecraft.client.recipebook.RecipeBookGroup;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(RecipeBookGroup.class)
public class RecipeBookGroupMixin {
    @Unique
    private static final RecipeBookGroup SMOKER_MISC = ClassTinkerers.getEnum(RecipeBookGroup.class, "CAFFEINATED_SMOKER_MISC");

    @Mutable
    @Shadow
    @Final
    public static Map<RecipeBookGroup, List<RecipeBookGroup>> SEARCH_MAP;

    @Shadow
    @Final
    public static RecipeBookGroup SMOKER_SEARCH;

    @Mutable
    @Shadow
    @Final
    public static List<RecipeBookGroup> SMOKER;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void setSearchMap(CallbackInfo ci) {
        ArrayList<RecipeBookGroup> recipeBookGroups = new ArrayList<>(SMOKER);
        recipeBookGroups.add(SMOKER_MISC);
        SMOKER = recipeBookGroups;

        Map<RecipeBookGroup, List<RecipeBookGroup>> newMap = new HashMap<>();
        SEARCH_MAP.forEach((group, groups) -> {
            ArrayList<RecipeBookGroup> arrayList = new ArrayList<>(groups);
            if (group == SMOKER_SEARCH) {
                arrayList.add(SMOKER_MISC);
            }
            newMap.put(group, arrayList);
        });
        SEARCH_MAP = newMap;
    }
}
