package net.chikorita_lover.caffeinated.registry.tag;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class CaffeinatedEntityTypeTags {
    public static final TagKey<EntityType<?>> COFFEE_INFLICTS_POISON = of("coffee_inflicts_poison");

    private static TagKey<EntityType<?>> of(String id) {
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Caffeinated.of(id));
    }
}
