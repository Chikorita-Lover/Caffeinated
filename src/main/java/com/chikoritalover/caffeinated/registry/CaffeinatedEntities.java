package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.entity.CivetEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CaffeinatedEntities {
    public static final EntityType<CivetEntity> CIVET = Registry.register(Registries.ENTITY_TYPE, new Identifier(Caffeinated.MODID, "civet"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, CivetEntity::new).dimensions(EntityDimensions.fixed(0.7F, 0.6F)).build());

    public static void register() {
        FabricDefaultAttributeRegistry.register(CIVET, CivetEntity.createCivetAttributes());
    }
}
