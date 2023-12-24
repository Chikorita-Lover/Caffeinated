package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CaffeinatedItemGroups {
    private static final RegistryKey<ItemGroup> CAFFEINATED = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Caffeinated.MODID, "caffeinated"));

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, CAFFEINATED, FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.caffeinated.caffeinated"))
                .icon(CaffeinatedItems.COFFEE_BOTTLE::getDefaultStack)
                .build()
        );

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addBefore(Items.NETHER_WART, CaffeinatedItems.COFFEE_BERRIES);
            entries.addBefore(Blocks.SCULK, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.addBefore(Items.CHORUS_FRUIT, CaffeinatedItems.COFFEE_BERRIES);
            entries.addBefore(Items.PUMPKIN_PIE, CaffeinatedBlocks.TIRAMISU, CaffeinatedItems.TIRAMISU_SLICE);
            entries.add(CaffeinatedItems.COFFEE_BOTTLE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.GLASS_BOTTLE, CaffeinatedItems.COFFEE_BEANS, CaffeinatedItems.GROUND_COFFEE);
            entries.addAfter(Items.PIGLIN_BANNER_PATTERN, CaffeinatedItems.JAVA_BANNER_PATTERN);
        });
        ItemGroupEvents.modifyEntriesEvent(CAFFEINATED).register(entries -> {
            entries.add(CaffeinatedItems.COFFEE_BERRIES);
            entries.addAfter(CaffeinatedItems.COFFEE_BERRIES, CaffeinatedItems.COFFEE_BEANS, CaffeinatedItems.GROUND_COFFEE, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, CaffeinatedBlocks.GROUND_COFFEE_BLOCK, CaffeinatedItems.COFFEE_BOTTLE, CaffeinatedBlocks.TIRAMISU, CaffeinatedItems.TIRAMISU_SLICE, CaffeinatedItems.JAVA_BANNER_PATTERN);
        });
    }
}
