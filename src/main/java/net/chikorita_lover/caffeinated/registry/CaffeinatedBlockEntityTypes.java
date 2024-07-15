package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.block.entity.CauldronCampfireBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CaffeinatedBlockEntityTypes {
    public static final BlockEntityType<CauldronCampfireBlockEntity> CAULDRON_CAMPFIRE = Registry.register(Registries.BLOCK_ENTITY_TYPE, Caffeinated.of("cauldron_campfire"), BlockEntityType.Builder.create(CauldronCampfireBlockEntity::new, CaffeinatedBlocks.CAULDRON_CAMPFIRE, CaffeinatedBlocks.SOUL_CAULDRON_CAMPFIRE).build());

    public static void register() {
    }
}
