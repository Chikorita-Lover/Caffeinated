package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CaffeinatedBlockTags {
    public static final TagKey<Block> CAULDRON_CAMPFIRES = of("cauldron_campfires");
    public static final TagKey<Block> CIVETS_SPAWNABLE_ON = of("civets_spawnable_on");
    public static final TagKey<Block> LIT_FIRES = of("lit_fires");

    private static TagKey<Block> of(String id) {
        return TagKey.of(RegistryKeys.BLOCK, new Identifier(Caffeinated.MODID, id));
    }

    public static void register() {
    }
}
