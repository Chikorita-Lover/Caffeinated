package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public abstract class ModBiomeKeys {
    public static final RegistryKey<Biome> COFFEE_FOREST = register("coffee_forest");

    public ModBiomeKeys() {
    }

    private static RegistryKey<Biome> register(String name) {
        return RegistryKey.of(Registry.BIOME_KEY, new Identifier(Caffeinated.MODID, name));
    }
}
