package com.chikoritalover.caffeinated;

import com.chikoritalover.caffeinated.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caffeinated implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MODID = "caffeinated";
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");

    @Override
    public void onInitialize() {
        CaffeinatedBannerPatterns.initAndGetDefault(Registries.BANNER_PATTERN);
        CaffeinatedBlocks.register();
        CaffeinatedCauldronBehavior.register();
        CaffeinatedItems.register();
        CaffeinatedParticleTypes.register();
        CaffeinatedPlacedFeatures.register();
        CaffeinatedSoundEvents.register();
        CaffeinatedStats.register();
        CaffeinatedStatusEffects.register();
        CaffeinatedTradeOffers.register();
        CaffeinatedBlockSoundGroup.register();
        CaffeinatedEntityTypeTags.register();
        CaffeinatedBlockTags.register();
        CaffeinatedItemTags.register();
        CaffeinatedBannerPatternTags.register();
        CaffeinatedBannerPatterns.register();

        registerLootTableEvents();
    }

    public void registerLootTableEvents() {
        addLootTablePool(1, 1, 0.5F, LootTables.JUNGLE_TEMPLE_CHEST, CaffeinatedItems.COFFEE_BEANS, 1, 3);
    }

    private void addLootTablePool(int minRolls, int maxRolls, float chance, Identifier lootTable, ItemConvertible item, int minCount, int maxCount) {
        UniformLootNumberProvider lootTableRange = UniformLootNumberProvider.create(minRolls, maxRolls);
        LootCondition condition = RandomChanceLootCondition.builder(chance).build();
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (lootTable.equals(id)) {
                LootPool lootPool = LootPool.builder().rolls(lootTableRange).conditionally(condition).with(ItemEntry.builder(item).build()).build();

                supplier.pool(lootPool);
            }
        });
    }
}
