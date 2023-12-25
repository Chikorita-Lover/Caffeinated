package com.chikoritalover.caffeinated;

import com.chikoritalover.caffeinated.block.entity.CauldronCampfireBlockEntity;
import com.chikoritalover.caffeinated.registry.*;
import com.mojang.datafixers.types.Type;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caffeinated implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MODID = "caffeinated";
    public static final Logger LOGGER = LoggerFactory.getLogger("modid");
    public static final BlockEntityType<CauldronCampfireBlockEntity> CAULDRON_CAMPFIRE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(MODID, "cauldron_campfire"),
            FabricBlockEntityTypeBuilder.create(CauldronCampfireBlockEntity::new, CaffeinatedBlocks.CAULDRON_CAMPFIRE, CaffeinatedBlocks.SOUL_CAULDRON_CAMPFIRE).build(null)
    );

    @Override
    public void onInitialize() {
        CaffeinatedBannerPatterns.initAndGetDefault(Registries.BANNER_PATTERN);
        CaffeinatedBlocks.register();
        CaffeinatedCauldronBehavior.register();
        CaffeinatedItemGroups.register();
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
        addLootTablePool(1, 1, 0.5F, LootTables.JUNGLE_TEMPLE_CHEST, CaffeinatedItems.COFFEE_BERRIES, 1, 3);
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
