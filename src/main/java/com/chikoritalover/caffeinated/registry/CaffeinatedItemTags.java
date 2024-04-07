package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CaffeinatedItemTags {
    public static final TagKey<Item> BLACK_COFFEE_BOTTLES = of("black_coffee_bottles");
    public static final TagKey<Item> CIVET_FOOD = of("civet_food");
    public static final TagKey<Item> COFFEE_BOTTLES = of("coffee_bottles");
    public static final TagKey<Item> COFFEE_FOOD = of("coffee_food");

    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, new Identifier(Caffeinated.MODID, id));
    }

    public static void register() {
    }
}
