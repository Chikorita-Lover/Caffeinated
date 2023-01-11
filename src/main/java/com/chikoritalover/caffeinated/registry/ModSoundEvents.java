package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSoundEvents {
    public static final SoundEvent BLOCK_CAULDRON_BREW = register("block.cauldron.brew");
    public static final SoundEvent BLOCK_CAULDRON_BUBBLE = register("block.cauldron.bubble");

    public static final SoundEvent BLOCK_COFFEE_BEAN_BLOCK_BREAK = register("block.coffee_bean_block.break");
    public static final SoundEvent BLOCK_COFFEE_BEAN_BLOCK_FALL = register("block.coffee_bean_block.fall");
    public static final SoundEvent BLOCK_COFFEE_BEAN_BLOCK_HIT = register("block.coffee_bean_block.hit");
    public static final SoundEvent BLOCK_COFFEE_BEAN_BLOCK_PLACE = register("block.coffee_bean_block.place");
    public static final SoundEvent BLOCK_COFFEE_BEAN_BLOCK_STEP = register("block.coffee_bean_block.step");

    public static final SoundEvent BLOCK_COFFEE_SHRUB_PICK_BERRIES = register("block.coffee_shrub.pick_berries");

    public static final SoundEvent BLOCK_GROUND_COFFEE_BREAK = register("block.ground_coffee_block.break");
    public static final SoundEvent BLOCK_GROUND_COFFEE_PLACE = register("block.ground_coffee_block.place");

    public static final SoundEvent ITEM_COFFEE_BOTTLE_DRINK = register("item.coffee_bottle.drink");
    public static final SoundEvent ITEM_GROUND_COFFEE_SPLASH = register("item.ground_coffee.splash");

    private static SoundEvent register(String id) {
        return Registry.register(Registry.SOUND_EVENT, new Identifier(Caffeinated.MODID, id), new SoundEvent(new Identifier(Caffeinated.MODID, id)));
    }

    public static void register() {}
}
