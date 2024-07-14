package net.chikorita_lover.caffeinated.registry.tag;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class CaffeinatedItemTags {
    public static final TagKey<Item> COFFEE_FOOD = of("coffee_food");

    private static TagKey<Item> of(String id) {
        return TagKey.of(RegistryKeys.ITEM, Caffeinated.of(id));
    }
}
