package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntityTypeTags {
    public static final TagKey<EntityType<?>> COFFEE_INFLICTS_POISON = of("coffee_inflicts_poison");

    private static TagKey<EntityType<?>> of(String id) {
        return TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(Caffeinated.MODID, id));
    }
}
