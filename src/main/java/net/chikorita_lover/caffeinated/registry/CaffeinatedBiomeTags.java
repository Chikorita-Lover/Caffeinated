package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public class CaffeinatedBiomeTags {
    public static final TagKey<Biome> SPAWNS_CIVETS = of("spawns_civets");

    public static void register() {
    }

    private static TagKey<Biome> of(String id) {
        return TagKey.of(RegistryKeys.BIOME, Caffeinated.of(id));
    }
}
