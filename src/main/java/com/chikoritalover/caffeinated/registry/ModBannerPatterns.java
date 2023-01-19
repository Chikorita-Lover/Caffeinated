package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ModBannerPatterns {
    public static final RegistryKey<BannerPattern> JAVA = of("java");

    public ModBannerPatterns() {
    }

    private static RegistryKey<BannerPattern> of(String id) {
        return RegistryKey.of(Registry.BANNER_PATTERN_KEY, new Identifier(Caffeinated.MODID, id));
    }

    public static BannerPattern initAndGetDefault(Registry<BannerPattern> registry) {
        return Registry.register(registry, JAVA, new BannerPattern("jav"));
    }
}
