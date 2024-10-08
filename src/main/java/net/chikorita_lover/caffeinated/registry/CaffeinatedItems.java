package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.item.CafeMielBottleItem;
import net.chikorita_lover.caffeinated.item.CaffeLatteBottleItem;
import net.chikorita_lover.caffeinated.item.CoffeeBottleItem;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedBannerPatternTags;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CaffeinatedItems {
    public static final Item COFFEE_BERRIES = register("coffee_berries", new AliasedBlockItem(CaffeinatedBlocks.COFFEE_SHRUB, new Item.Settings().food(CaffeinatedFoodComponents.COFFEE_BERRIES)));
    public static final Item COFFEE_BEANS = register("coffee_beans", new Item(new Item.Settings()));
    public static final Item GROUND_COFFEE = register("ground_coffee", new Item(new Item.Settings()));

    public static final Item COFFEE_BOTTLE = register("coffee_bottle", new CoffeeBottleItem(new Item.Settings().food(CaffeinatedFoodComponents.COFFEE_BOTTLE).maxCount(16).recipeRemainder(Items.GLASS_BOTTLE), false));
    public static final Item LATTE_COFFEE_BOTTLE = register("latte_coffee_bottle", new CaffeLatteBottleItem(new Item.Settings().food(CaffeinatedFoodComponents.LATTE_COFFEE_BOTTLE).maxCount(16).recipeRemainder(Items.GLASS_BOTTLE)));
    public static final Item CAFE_MIEL_COFFEE_BOTTLE = register("cafe_miel_coffee_bottle", new CafeMielBottleItem(new Item.Settings().food(CaffeinatedFoodComponents.CAFE_MIEL_COFFEE_BOTTLE).maxCount(16).recipeRemainder(Items.GLASS_BOTTLE)));

    public static final Item JAVA_BANNER_PATTERN = register("java_banner_pattern", new BannerPatternItem(CaffeinatedBannerPatternTags.JAVA_PATTERN_ITEM, new Item.Settings().maxCount(1)));
    public static final Item TIRAMISU_SLICE = register("tiramisu_slice", new Item(new Item.Settings().food(CaffeinatedFoodComponents.TIRAMISU_SLICE)));

    public static final Item CIVET_SPAWN_EGG = register("civet_spawn_egg", new SpawnEggItem(CaffeinatedEntities.CIVET, 0xDCCE95, 0x6F6A5F, new Item.Settings()));

    public static Item register(Block block) {
        return register(block, new BlockItem(block, new Item.Settings()));
    }

    public static Item register(Block block, Item item) {
        return register(Registries.BLOCK.getId(block), item);
    }

    private static Item register(String id, Item item) {
        return register(Caffeinated.of(id), item);
    }

    private static Item register(Identifier id, Item item) {
        if (item instanceof BlockItem blockItem) {
            blockItem.appendBlocks(Item.BLOCK_ITEMS, item);
        }
        return Registry.register(Registries.ITEM, id, item);
    }

    public static void register() {
        registerCompostingChances();
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
}
