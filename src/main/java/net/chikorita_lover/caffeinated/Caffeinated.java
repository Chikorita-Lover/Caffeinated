package net.chikorita_lover.caffeinated;

import net.chikorita_lover.caffeinated.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caffeinated implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Caffeinated");
    public static final String MODID = "caffeinated";

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
        CaffeinatedBlockSoundGroup.register();
        CaffeinatedEntityTypeTags.register();
        CaffeinatedBlockTags.register();
        CaffeinatedItemTags.register();
        CaffeinatedBannerPatternTags.register();

        registerLootTableEvents();
    }

    public void registerLootTableEvents() {
        addLootTablePool(1, 1, 0.5F, LootTables.JUNGLE_TEMPLE_CHEST, CaffeinatedItems.COFFEE_BEANS, 1, 3);
    }

    private void addLootTablePool(int minRolls, int maxRolls, float chance, RegistryKey<LootTable> lootTable, ItemConvertible item, int minCount, int maxCount) {
        UniformLootNumberProvider lootTableRange = UniformLootNumberProvider.create(minRolls, maxRolls);
        LootCondition condition = RandomChanceLootCondition.builder(chance).build();
        LootTableEvents.MODIFY.register((key, builder, source) -> {
            if (lootTable.equals(key)) {
                LootPool lootPool = LootPool.builder()
                        .rolls(lootTableRange)
                        .conditionally(condition)
                        .with(ItemEntry.builder(item).build()).build();

                builder.pool(lootPool);
            }
        });
    }
}
