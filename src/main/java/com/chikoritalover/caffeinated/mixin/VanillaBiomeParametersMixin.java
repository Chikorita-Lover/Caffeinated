package com.chikoritalover.caffeinated.mixin;

import com.chikoritalover.caffeinated.registry.ModBiomes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(VanillaBiomeParameters.class)
public abstract class VanillaBiomeParametersMixin {
    @Shadow protected abstract void writeBiomeParameters(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome);

    @Inject(method = "writeLandBiomes", at = @At("HEAD"))
    private void writeCoffeeForest(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, CallbackInfo ci) {
        this.writeBiomeParameters(
                parameters,
                inBetween(0.2F, 0.55F), // Temperature
                inBetween(0.1F, 0.3F), // Humidity
                inBetween(0.03F, 1.0F), // Continentalness
                inBetween(-1.0F, -0.45F), // Erosion
                inBetween(-1F, 0.5F), // Weirdness
                0,
                ModBiomes.COFFEE_FOREST
        );
    }

    private static MultiNoiseUtil.ParameterRange inBetween(float min, float max) {
        return MultiNoiseUtil.ParameterRange.of(min, max);
    }
}
