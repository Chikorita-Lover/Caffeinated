package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.entity.CivetEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CaffeinatedEntities {
    public static final EntityType<CivetEntity> CIVET = Registry.register(Registries.ENTITY_TYPE, Caffeinated.of("civet"), EntityType.Builder.create(CivetEntity::new, SpawnGroup.CREATURE).dimensions(0.7F, 0.6F).build());

    public static void register() {
        FabricDefaultAttributeRegistry.register(CIVET, CivetEntity.createCivetAttributes());
    }
}
