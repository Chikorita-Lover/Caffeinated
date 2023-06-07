package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CaffeinatedEntityTypeTags {
    public static final TagKey<EntityType<?>> COFFEE_INFLICTS_POISON = of("coffee_inflicts_poison");

    private static TagKey<EntityType<?>> of(String id) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(Caffeinated.MODID, id));
    }

    public static void register() {
    }
}
