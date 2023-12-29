package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Set;

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
            entries.addBefore(Blocks.BEE_NEST, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.addBefore(Items.CHORUS_FRUIT, CaffeinatedItems.COFFEE_BERRIES);
            entries.addBefore(Items.ROTTEN_FLESH, CaffeinatedItems.COFFEE_BOTTLE, CaffeinatedBlocks.TIRAMISU, CaffeinatedItems.TIRAMISU_SLICE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.WHEAT, CaffeinatedItems.COFFEE_BEANS);
            entries.addBefore(Items.NETHER_WART, CaffeinatedItems.GROUND_COFFEE);
            entries.addAfter(Items.PIGLIN_BANNER_PATTERN, CaffeinatedItems.JAVA_BANNER_PATTERN);
        });
        ItemGroupEvents.modifyEntriesEvent(CAFFEINATED).register(entries -> {
            ItemGroup.StackVisibility visibility = ItemGroup.StackVisibility.PARENT_TAB_ONLY;
            entries.add(CaffeinatedItems.COFFEE_BERRIES, visibility);
            entries.add(CaffeinatedItems.COFFEE_BEANS, visibility);
            entries.add(CaffeinatedItems.GROUND_COFFEE, visibility);
            entries.add(CaffeinatedBlocks.COFFEE_BEAN_BLOCK, visibility);
            entries.add(CaffeinatedBlocks.GROUND_COFFEE_BLOCK, visibility);
            registerCampfiresCauldron(entries, visibility);
            entries.add(Items.GLASS_BOTTLE, visibility);
            entries.add(PotionUtil.setPotion(Items.POTION.getDefaultStack(), Potions.WATER), visibility);
            entries.add(CaffeinatedItems.COFFEE_BOTTLE, visibility);
            entries.add(CaffeinatedBlocks.TIRAMISU, visibility);
            entries.add(CaffeinatedItems.TIRAMISU_SLICE, visibility);
            entries.add(CaffeinatedItems.JAVA_BANNER_PATTERN, visibility);
        });
    }

    private static void registerCampfiresCauldron(FabricItemGroupEntries entries, ItemGroup.StackVisibility visibility) {
        Set<ItemStack> set = ItemStackSet.create();
        for (Block block : Registries.BLOCK.stream().filter(block -> block instanceof CampfireBlock).toList()) {
            ItemStack itemStack = block.asItem().getDefaultStack();
            set.add(itemStack);
        }
        set.add(Items.CAULDRON.getDefaultStack());
        entries.addAll(set, visibility);
    }
}
