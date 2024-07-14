package net.chikorita_lover.caffeinated;

import net.chikorita_lover.caffeinated.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caffeinated implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Caffeinated");
    public static final String NAMESPACE = "caffeinated";

    public static Identifier of(String id) {
        return Identifier.of(NAMESPACE, id);
    }

    @Override
    public void onInitialize() {
        CaffeinatedBlocks.register();
        CaffeinatedCauldronBehavior.register();
        CaffeinatedItems.register();
        CaffeinatedParticleTypes.register();
        CaffeinatedPlacedFeatures.register();
        CaffeinatedSoundEvents.register();
        CaffeinatedStats.register();
        CaffeinatedStatusEffects.register();
        CaffeinatedTradeOffers.register();

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (key.equals(LootTables.JUNGLE_TEMPLE_CHEST)) {
                tableBuilder.pool(LootPool.builder().with(ItemEntry.builder(CaffeinatedItems.COFFEE_BERRIES)).with(EmptyEntry.builder()));
            }
        });
    }
}
