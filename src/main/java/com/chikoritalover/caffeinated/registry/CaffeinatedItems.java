package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.item.CoffeeBottleItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CaffeinatedItems {
    public static final Item COFFEE_BERRIES = register("coffee_berries", new AliasedBlockItem(CaffeinatedBlocks.COFFEE_SHRUB, new Item.Settings().food(CaffeinatedFoodComponents.COFFEE_BERRIES)));
    public static final Item COFFEE_BEANS = register("coffee_beans", new Item(new Item.Settings()));
    public static final Item GROUND_COFFEE = register("ground_coffee", new Item(new Item.Settings()));
    public static final Item COFFEE_BOTTLE = register("coffee_bottle", new CoffeeBottleItem(new Item.Settings().food(CaffeinatedFoodComponents.COFFEE_BOTTLE).maxCount(16).recipeRemainder(Items.GLASS_BOTTLE)));
    public static final Item JAVA_BANNER_PATTERN = register("java_banner_pattern", new BannerPatternItem(CaffeinatedBannerPatternTags.JAVA_PATTERN_ITEM, (new Item.Settings()).maxCount(1)));
    public static final Item TIRAMISU_SLICE = register("tiramisu_slice", new Item(new Item.Settings().food(CaffeinatedFoodComponents.TIRAMISU_SLICE)));

    public static Item register(Block block) {
        return register(new BlockItem(block, new Item.Settings()));
    }

    public static Item register(BlockItem item) {
        return register(item.getBlock(), item);
    }

    private static Item register(Block block, Item item) {
        return register(Registries.BLOCK.getId(block), item);
    }

    private static Item register(String id, Item item) {
        return register(new Identifier(Caffeinated.MODID, id), item);
    }

    private static Item register(Identifier id, Item item) {
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }
        return Registry.register(Registries.ITEM, id, item);
    }

    public static void register() {
        registerCompostingChances();
        registerItemGroups();
    }

    public static void registerCompostingChances() {
        CompostingChanceRegistry.INSTANCE.add(CaffeinatedBlocks.COFFEE_BEAN_BLOCK, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(CaffeinatedBlocks.GROUND_COFFEE_BLOCK, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(COFFEE_BEANS, 0.5F);
        CompostingChanceRegistry.INSTANCE.add(GROUND_COFFEE, 0.5F);
        CompostingChanceRegistry.INSTANCE.add(COFFEE_BERRIES, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(CaffeinatedBlocks.TIRAMISU, 1.0F);
        CompostingChanceRegistry.INSTANCE.add(TIRAMISU_SLICE, 0.85F);
    }

    public static void registerItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addAfter(Blocks.HONEYCOMB_BLOCK, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
            entries.addAfter(Items.SWEET_BERRIES, COFFEE_BERRIES);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.addAfter(Items.GLOW_BERRIES, COFFEE_BERRIES);
            entries.addAfter(Items.CAKE, CaffeinatedBlocks.TIRAMISU, TIRAMISU_SLICE);
            entries.addAfter(Items.HONEY_BOTTLE, COFFEE_BOTTLE);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.WHEAT, COFFEE_BEANS, GROUND_COFFEE);
            entries.addAfter(Items.PIGLIN_BANNER_PATTERN, JAVA_BANNER_PATTERN);
        });
    }
}
