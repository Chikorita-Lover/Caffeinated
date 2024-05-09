package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CaffeinatedBannerPatternTags {
    public static final TagKey<BannerPattern> JAVA_PATTERN_ITEM = of("pattern_item/java");

    private static TagKey<BannerPattern> of(String id) {
        return TagKey.of(RegistryKeys.BANNER_PATTERN, new Identifier(Caffeinated.MODID, id));
    }

    public static void register() {
    }
}
