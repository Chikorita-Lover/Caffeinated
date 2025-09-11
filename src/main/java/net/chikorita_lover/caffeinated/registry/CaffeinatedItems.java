package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.item.CoffeeBottleItem;
import net.chikorita_lover.caffeinated.item.HoneyCoffeeBottleItem;
import net.chikorita_lover.caffeinated.item.MilkCoffeeBottleItem;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedBannerPatternTags;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class CaffeinatedItems {
    public static final Item COFFEE_BERRIES = register("coffee_berries", new AliasedBlockItem(CaffeinatedBlocks.COFFEE_SHRUB, new Item.Settings().food(CaffeinatedFoodComponents.COFFEE_BERRIES)));
    public static final Item COFFEE_BEANS = register("coffee_beans", new Item(new Item.Settings()));
    public static final Item GROUND_COFFEE = register("ground_coffee", new Item(new Item.Settings()));

    public static final Item COFFEE_BOTTLE = register("coffee_bottle", new CoffeeBottleItem(new Item.Settings().food(CaffeinatedFoodComponents.COFFEE_BOTTLE).maxCount(16).recipeRemainder(Items.GLASS_BOTTLE)));
    public static final Item MILK_COFFEE_BOTTLE = register("milk_coffee_bottle", new MilkCoffeeBottleItem(new Item.Settings().food(CaffeinatedFoodComponents.MILK_COFFEE_BOTTLE).maxCount(16).recipeRemainder(Items.GLASS_BOTTLE)));
    public static final Item HONEY_COFFEE_BOTTLE = register("honey_coffee_bottle", new HoneyCoffeeBottleItem(new Item.Settings().food(CaffeinatedFoodComponents.HONEY_COFFEE_BOTTLE).maxCount(16).recipeRemainder(Items.GLASS_BOTTLE)));

    public static final Item JAVA_BANNER_PATTERN = register("java_banner_pattern", new BannerPatternItem(CaffeinatedBannerPatternTags.JAVA_PATTERN_ITEM, new Item.Settings().maxCount(1)));
    public static final Item TIRAMISU = register("tiramisu", new BlockItem(CaffeinatedBlocks.TIRAMISU, new Item.Settings().maxCount(1)));
    public static final Item TIRAMISU_SLICE = register("tiramisu_slice", new Item(new Item.Settings().food(CaffeinatedFoodComponents.TIRAMISU_SLICE)));

    public static final Item CIVET_SPAWN_EGG = register("civet_spawn_egg", new SpawnEggItem(CaffeinatedEntityTypes.CIVET, 0xDCCE95, 0x6F6A5F, new Item.Settings()));

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, Caffeinated.of(id), item);
    }

    public static void register() {
        CompostingChanceRegistry.INSTANCE.add(COFFEE_BEANS, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(GROUND_COFFEE, 0.5F);
        CompostingChanceRegistry.INSTANCE.add(COFFEE_BERRIES, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(TIRAMISU, 1.0F);
        CompostingChanceRegistry.INSTANCE.add(TIRAMISU_SLICE, 0.85F);
    }
}
