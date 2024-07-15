package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.block.CauldronCampfireBlock;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.Set;

public class CaffeinatedItemGroups {
    private static final RegistryKey<ItemGroup> CAFFEINATED = RegistryKey.of(RegistryKeys.ITEM_GROUP, Caffeinated.of("caffeinated"));

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, CAFFEINATED, FabricItemGroup.builder().displayName(Text.translatable("itemGroup.caffeinated.caffeinated")).icon(CaffeinatedItems.BLACK_COFFEE_BOTTLE::getDefaultStack).build());

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addBefore(Items.NETHER_WART, CaffeinatedItems.COFFEE_BERRIES);
            entries.addBefore(Blocks.BEE_NEST, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.addBefore(Items.CHORUS_FRUIT, CaffeinatedItems.COFFEE_BERRIES);
            entries.addBefore(Items.ROTTEN_FLESH, CaffeinatedItems.BLACK_COFFEE_BOTTLE, CaffeinatedItems.LATTE_COFFEE_BOTTLE, CaffeinatedBlocks.TIRAMISU, CaffeinatedItems.TIRAMISU_SLICE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.WHEAT, CaffeinatedItems.COFFEE_BEANS);
            entries.addBefore(Items.NETHER_WART, CaffeinatedItems.GROUND_COFFEE);
            entries.addAfter(Items.PIGLIN_BANNER_PATTERN, CaffeinatedItems.JAVA_BANNER_PATTERN);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.addAfter(Items.CHICKEN_SPAWN_EGG, CaffeinatedItems.CIVET_SPAWN_EGG);
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
            entries.add(CaffeinatedItems.BLACK_COFFEE_BOTTLE, visibility);
            entries.add(CaffeinatedItems.LATTE_COFFEE_BOTTLE, visibility);
            entries.add(CaffeinatedBlocks.TIRAMISU, visibility);
            entries.add(CaffeinatedItems.TIRAMISU_SLICE, visibility);
            entries.add(CaffeinatedItems.JAVA_BANNER_PATTERN, visibility);
            entries.add(CaffeinatedItems.CIVET_SPAWN_EGG, visibility);
        });
    }

    private static void registerCampfiresCauldron(FabricItemGroupEntries entries, ItemGroup.StackVisibility visibility) {
        Set<ItemStack> set = ItemStackSet.create();
        for (Block block : CauldronCampfireBlock.CAMPFIRE_TO_CAULDRON_CAMPFIRE.keySet()) {
            ItemStack stack = block.asItem().getDefaultStack();
            set.add(stack);
        }
        set.add(Items.CAULDRON.getDefaultStack());
        entries.addAll(set, visibility);
    }
}
