package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class CaffeinatedPlacedFeatures {
    public static final RegistryKey<PlacedFeature> PATCH_COFFEE_SHRUB = RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(Caffeinated.MODID, "patch_coffee_shrub"));

    public static void register() {
        BiomeModifications.addFeature(BiomeSelectors.tag(BiomeTags.IS_JUNGLE), GenerationStep.Feature.VEGETAL_DECORATION, PATCH_COFFEE_SHRUB);
    }
}
