package com.chikoritalover.caffeinated.registry;

import net.minecraft.sound.BlockSoundGroup;

public class ModBlockSoundGroup {
    public static final BlockSoundGroup COFFEE_BEAN_BLOCK = new BlockSoundGroup(1.0F, 1.0F,
            ModSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_BREAK,
            ModSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_STEP,
            ModSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_PLACE,
            ModSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_HIT,
            ModSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_FALL
    );
}
