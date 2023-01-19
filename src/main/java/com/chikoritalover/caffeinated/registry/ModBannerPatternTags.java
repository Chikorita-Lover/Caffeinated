package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBannerPatternTags {
    public static final TagKey<BannerPattern> JAVA_PATTERN_ITEM = of("pattern_item/java");

    private ModBannerPatternTags() {
    }

    private static TagKey<BannerPattern> of(String id) {
        return TagKey.of(Registry.BANNER_PATTERN_KEY, new Identifier(Caffeinated.MODID, id));
    }
}
