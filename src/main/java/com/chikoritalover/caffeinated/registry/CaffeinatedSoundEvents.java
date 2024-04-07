package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class CaffeinatedSoundEvents {
    public static final SoundEvent BLOCK_CAULDRON_CAMPFIRE_BREW = register("block.cauldron_campfire.brew");
    public static final SoundEvent BLOCK_CAULDRON_CAMPFIRE_BUBBLE = register("block.cauldron_campfire.bubble");

    public static final SoundEvent BLOCK_COFFEE_BEAN_BLOCK_BREAK = register("block.coffee_bean_block.break");
    public static final SoundEvent BLOCK_COFFEE_BEAN_BLOCK_FALL = register("block.coffee_bean_block.fall");
    public static final SoundEvent BLOCK_COFFEE_BEAN_BLOCK_HIT = register("block.coffee_bean_block.hit");
    public static final SoundEvent BLOCK_COFFEE_BEAN_BLOCK_PLACE = register("block.coffee_bean_block.place");
    public static final SoundEvent BLOCK_COFFEE_BEAN_BLOCK_STEP = register("block.coffee_bean_block.step");

    public static final SoundEvent BLOCK_COFFEE_SHRUB_PICK_BERRIES = register("block.coffee_shrub.pick_berries");

    public static final SoundEvent BLOCK_GROUND_COFFEE_BREAK = register("block.ground_coffee_block.break");
    public static final SoundEvent BLOCK_GROUND_COFFEE_PLACE = register("block.ground_coffee_block.place");

    public static final SoundEvent ENTITY_CIVET_DEATH = register("entity.civet.death");
    public static final SoundEvent ENTITY_CIVET_EAT = register("entity.civet.eat");
    public static final SoundEvent ENTITY_CIVET_HURT = register("entity.civet.hurt");
    public static final SoundEvent ENTITY_CIVET_IDLE = register("entity.civet.idle");

    public static final SoundEvent ITEM_COFFEE_BOTTLE_DRINK = register("item.coffee_bottle.drink");
    public static final SoundEvent ITEM_GROUND_COFFEE_SPLASH = register("item.ground_coffee.splash");

    private static SoundEvent register(String id) {
        return Registry.register(Registries.SOUND_EVENT, new Identifier(Caffeinated.MODID, id), SoundEvent.of(new Identifier(Caffeinated.MODID, id)));
    }

    public static void register() {
    }
}
