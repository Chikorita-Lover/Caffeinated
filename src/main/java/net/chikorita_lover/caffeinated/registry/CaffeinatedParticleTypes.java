package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CaffeinatedParticleTypes {
    public static final SimpleParticleType COFFEE_POP = register("coffee_pop", false);

    public static void register() {
    }

    private static SimpleParticleType register(String id, boolean alwaysSpawn) {
        return Registry.register(Registries.PARTICLE_TYPE, Caffeinated.of(id), FabricParticleTypes.simple(alwaysSpawn));
    }
}
