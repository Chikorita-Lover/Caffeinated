package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CaffeinatedParticleTypes {
    public static final ParticleType<SimpleParticleType> COFFEE_POP = register("coffee_pop", FabricParticleTypes.simple());

    public static void register() {
    }

    private static <T extends ParticleEffect> ParticleType<T> register(String id, ParticleType<T> particle) {
        return Registry.register(Registries.PARTICLE_TYPE, Caffeinated.of(id), particle);
    }
}
