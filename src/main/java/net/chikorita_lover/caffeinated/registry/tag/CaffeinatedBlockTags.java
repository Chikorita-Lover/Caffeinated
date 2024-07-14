package net.chikorita_lover.caffeinated.registry.tag;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class CaffeinatedBlockTags {
    public static final TagKey<Block> CAULDRON_CAMPFIRES = of("cauldron_campfires");
    public static final TagKey<Block> CIVETS_SPAWNABLE_ON = of("civets_spawnable_on");
    public static final TagKey<Block> LIT_FIRES = of("lit_fires");

    private static TagKey<Block> of(String id) {
        return TagKey.of(RegistryKeys.BLOCK, Caffeinated.of(id));
    }
}
