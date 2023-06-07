package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class CaffeinatedBannerPatterns {
    public static final RegistryKey<BannerPattern> JAVA = of("java");

    private static RegistryKey<BannerPattern> of(String id) {
        return RegistryKey.of(RegistryKeys.BANNER_PATTERN, new Identifier(Caffeinated.MODID, id));
    }

    public static BannerPattern initAndGetDefault(Registry<BannerPattern> registry) {
        return Registry.register(registry, JAVA, new BannerPattern("jav"));
    }

    public static void register() {
    }
}
