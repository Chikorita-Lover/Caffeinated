package com.chikoritalover.caffeinated.registry;

import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;

public class CaffeinatedBlockSoundGroup {
    public static final BlockSoundGroup COFFEE_BEAN_BLOCK = new BlockSoundGroup(1.0F, 1.0F,
            CaffeinatedSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_BREAK,
            CaffeinatedSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_STEP,
            CaffeinatedSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_PLACE,
            CaffeinatedSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_HIT,
            CaffeinatedSoundEvents.BLOCK_COFFEE_BEAN_BLOCK_FALL
    );
    public static final BlockSoundGroup GROUND_COFFEE_BLOCK = new BlockSoundGroup(1.0F, 1.0F,
            CaffeinatedSoundEvents.BLOCK_GROUND_COFFEE_BREAK,
            SoundEvents.BLOCK_ROOTED_DIRT_STEP,
            CaffeinatedSoundEvents.BLOCK_GROUND_COFFEE_PLACE,
            SoundEvents.BLOCK_ROOTED_DIRT_HIT,
            SoundEvents.BLOCK_ROOTED_DIRT_FALL
    );

    public static void register() {
    }
}
