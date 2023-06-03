package com.chikoritalover.caffeinated.registry;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public class ModBlockSoundGroup {
    public static final BlockSoundGroup COFFEE_BEAN_BLOCK = new BlockSoundGroup(1.0F, 1.0F,
            ModSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_BREAK,
            ModSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_STEP,
            ModSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_PLACE,
            ModSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_HIT,
            ModSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_FALL
    );
    public static final BlockSoundGroup GROUND_COFFEE_BLOCK = new BlockSoundGroup(1.0F, 1.0F,
            ModSoundEvents.BLOCK_GROUND_COFFEE_BREAK,
            SoundEvents.BLOCK_ROOTED_DIRT_STEP,
            ModSoundEvents.BLOCK_GROUND_COFFEE_PLACE,
            SoundEvents.BLOCK_ROOTED_DIRT_HIT,
            SoundEvents.BLOCK_ROOTED_DIRT_FALL
    );

    public static void register() {
    }
}
