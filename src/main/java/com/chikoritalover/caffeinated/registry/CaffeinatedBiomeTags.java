package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

public class CaffeinatedBiomeTags {
    public static final TagKey<Biome> SPAWNS_CIVETS = of("spawns_civets");

    private static TagKey<Biome> of(String id) {
        return TagKey.of(RegistryKeys.BIOME, new Identifier(Caffeinated.MODID, id));
    }

    public static void register() {
    }
}
