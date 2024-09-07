package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.item.CoffeeBottleItem;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedBannerPatternTags;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CaffeinatedItems {
    public static final Item COFFEE_BERRIES = register("coffee_berries", new Item(new Item.Settings().food(CaffeinatedFoodComponents.COFFEE_BERRIES)));
    public static final Item COFFEE_BEANS = register("coffee_beans", new AliasedBlockItem(CaffeinatedBlocks.COFFEE_SHRUB, new Item.Settings()));
    public static final Item GROUND_COFFEE = register("ground_coffee", new Item(new Item.Settings()));

    public static final Item COFFEE_BOTTLE = register("coffee_bottle", new CoffeeBottleItem(new Item.Settings().food(CaffeinatedFoodComponents.COFFEE_BOTTLE).maxCount(16).recipeRemainder(Items.GLASS_BOTTLE)));
    public static final Item TIRAMISU = register("tiramisu", new Item(new Item.Settings().food(CaffeinatedFoodComponents.TIRAMISU)));

    public static final Item JAVA_BANNER_PATTERN = register("java_banner_pattern", new BannerPatternItem(CaffeinatedBannerPatternTags.JAVA_PATTERN_ITEM, (new Item.Settings()).maxCount(1)));

    public static Item register(Block block) {
        return register(block, new BlockItem(block, new Item.Settings()));
    }

    private static Item register(Block block, Item item) {
        return register(Registries.BLOCK.getId(block), item);
    }

    private static Item register(String id, Item item) {
        return register(Caffeinated.of(id), item);
    }

    private static Item register(Identifier id, Item item) {
        if (item instanceof BlockItem) {
            ((BlockItem) item).appendBlocks(Item.BLOCK_ITEMS, item);
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
        CompostingChanceRegistry.INSTANCE.add(TIRAMISU, 1.0F);
    }

    public static void registerItemGroups() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.addBefore(Items.NETHER_WART, COFFEE_BEANS);
            entries.addBefore(Blocks.BEE_NEST, CaffeinatedBlocks.COFFEE_BEAN_BLOCK, CaffeinatedBlocks.GROUND_COFFEE_BLOCK);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.addBefore(Items.CHORUS_FRUIT, COFFEE_BERRIES);
            entries.addBefore(Items.ROTTEN_FLESH, COFFEE_BOTTLE, TIRAMISU);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.WHEAT, COFFEE_BEANS, GROUND_COFFEE);
            entries.addAfter(Items.PIGLIN_BANNER_PATTERN, JAVA_BANNER_PATTERN);
        });
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            final String farmersDelight = "farmersdelight";
            if (Registries.ITEM_GROUP.getId(group).equals(Identifier.of(farmersDelight, farmersDelight))) {
                for (ItemStack stack : entries.getDisplayStacks()) {
                    if (Registries.ITEM.getId(stack.getItem()).equals(Identifier.of(farmersDelight, "carrot_crate"))) {
                        entries.addBefore(stack, CaffeinatedBlocks.COFFEE_BERRY_CRATE);
                        break;
                    }
                }
            }
        });
    }
}
