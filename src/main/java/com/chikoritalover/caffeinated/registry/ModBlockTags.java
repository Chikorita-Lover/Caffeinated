package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModBlockTags {
    public static final TagKey<Block> LIT_FIRES = TagKey.of(Registry.BLOCK_KEY, new Identifier(Caffeinated.MODID, "lit_fires"));
}
