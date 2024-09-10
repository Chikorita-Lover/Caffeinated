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
import net.minecraft.util.Identifier;

import java.util.Set;

public class CaffeinatedItemGroups {
    private static final RegistryKey<ItemGroup> CAFFEINATED = RegistryKey.of(RegistryKeys.ITEM_GROUP, Caffeinated.of("caffeinated"));

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, CAFFEINATED, FabricItemGroup.builder().displayName(Text.translatable("itemGroup.caffeinated.caffeinated")).icon(CaffeinatedItems.BLACK_COFFEE_BOTTLE::getDefaultStack).build());

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addBefore(Items.GLOW_BERRIES, CaffeinatedItems.COFFEE_BERRIES);
            entries.addBefore(Blocks.BEE_NEST, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.addBefore(Items.SWEET_BERRIES, CaffeinatedItems.COFFEE_BERRIES);
            entries.addBefore(Items.ROTTEN_FLESH, CaffeinatedBlocks.TIRAMISU);
            entries.addBefore(Items.OMINOUS_BOTTLE, CaffeinatedItems.BLACK_COFFEE_BOTTLE, CaffeinatedItems.LATTE_COFFEE_BOTTLE, CaffeinatedItems.CAFE_MIEL_COFFEE_BOTTLE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.WHEAT, CaffeinatedItems.COFFEE_BEANS, CaffeinatedItems.GROUND_COFFEE);
            entries.addBefore(Items.PIGLIN_BANNER_PATTERN, CaffeinatedItems.JAVA_BANNER_PATTERN);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> {
            entries.addAfter(Items.CHICKEN_SPAWN_EGG, CaffeinatedItems.CIVET_SPAWN_EGG);
        });
        ItemGroupEvents.modifyEntriesEvent(CAFFEINATED).register(entries -> {
            ItemGroup.StackVisibility parentTabOnly = ItemGroup.StackVisibility.PARENT_TAB_ONLY;
            entries.add(CaffeinatedItems.COFFEE_BERRIES, parentTabOnly);
            entries.add(CaffeinatedItems.COFFEE_BEANS, parentTabOnly);
            entries.add(CaffeinatedItems.GROUND_COFFEE, parentTabOnly);
            entries.add(CaffeinatedBlocks.COFFEE_BEAN_BLOCK, parentTabOnly);
            entries.add(CaffeinatedBlocks.GROUND_COFFEE_BLOCK, parentTabOnly);
            registerCampfiresCauldron(entries, parentTabOnly);
            entries.add(Items.GLASS_BOTTLE, parentTabOnly);
            entries.add(CaffeinatedItems.BLACK_COFFEE_BOTTLE, parentTabOnly);
            entries.add(CaffeinatedItems.LATTE_COFFEE_BOTTLE, parentTabOnly);
            entries.add(CaffeinatedItems.CAFE_MIEL_COFFEE_BOTTLE, parentTabOnly);
            entries.add(CaffeinatedBlocks.TIRAMISU, parentTabOnly);
            entries.add(CaffeinatedItems.JAVA_BANNER_PATTERN, parentTabOnly);
            entries.add(CaffeinatedItems.CIVET_SPAWN_EGG, parentTabOnly);
        });
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            final String farmersDelight = "farmersdelight";
            if (Registries.ITEM_GROUP.getId(group).equals(Identifier.of(farmersDelight, farmersDelight))) {
                for (int i = 0; i < entries.getDisplayStacks().size(); ++i) {
                    ItemStack displayStack = entries.getDisplayStacks().get(i);
                    Identifier id = Registries.ITEM.getId(displayStack.getItem());
                    if (id.equals(Identifier.of(farmersDelight, "carrot_crate"))) {
                        entries.addBefore(displayStack, CaffeinatedBlocks.COFFEE_BERRY_CRATE);
                        ++i;
                    } else if (id.equals(Identifier.of(farmersDelight, "cake_slice"))) {
                        entries.addAfter(displayStack, CaffeinatedItems.TIRAMISU_SLICE);
                        ++i;
                    }
                }
            }
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
