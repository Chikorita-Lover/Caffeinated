package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.OverworldBiomeCreator;

public class ModBiomes {
    public static final RegistryKey<Biome> COFFEE_FOREST = RegistryKey.of(Registry.BIOME_KEY, new Identifier(Caffeinated.MODID, "coffee_forest"));

    public static void init() {
        BuiltinRegistries.add(BuiltinRegistries.BIOME, new Identifier(Caffeinated.MODID, "coffee_forest"), OverworldBiomeCreator.createJungle());
    }
}
