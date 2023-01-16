package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.integration.farmersdelight.FarmersDelightItemGroup;
import com.chikoritalover.caffeinated.item.CoffeeBottleItem;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class ModItems {
    public static final Item COFFEE_BEAN_BLOCK = new BlockItem(ModBlocks.COFFEE_BEAN_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Item GROUND_COFFEE_BLOCK = new BlockItem(ModBlocks.GROUND_COFFEE_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS));
    public static final Item COFFEE_BERRY_CRATE = new BlockItem(ModBlocks.COFFEE_BERRY_CRATE, new Item.Settings().group(getFarmersDelightItemGroup()));

    public static final Item COFFEE_BEANS = new AliasedBlockItem(ModBlocks.COFFEE_SHRUB, new Item.Settings().group(ItemGroup.MISC));
    public static final Item GROUND_COFFEE = new Item(new Item.Settings().group(ItemGroup.MISC));

    public static final Item COFFEE_BERRIES = new Item(new Item.Settings().food(ModFoodComponents.COFFEE_BERRIES).group(ItemGroup.FOOD));
    public static final Item COFFEE_BOTTLE = new CoffeeBottleItem(new Item.Settings().food(ModFoodComponents.COFFEE_BOTTLE).maxCount(16).group(ItemGroup.FOOD));
    public static final Item TIRAMISU = new Item(new Item.Settings().food(ModFoodComponents.TIRAMISU).group(ItemGroup.FOOD));

    public static void register() {
        Registry.register(Registry.ITEM, new Identifier(Caffeinated.MODID, "coffee_bean_block"), COFFEE_BEAN_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(Caffeinated.MODID, "ground_coffee_block"), GROUND_COFFEE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(Caffeinated.MODID, "coffee_berry_crate"), COFFEE_BERRY_CRATE);

        Registry.register(Registry.ITEM, new Identifier(Caffeinated.MODID, "coffee_beans"), COFFEE_BEANS);
        Registry.register(Registry.ITEM, new Identifier(Caffeinated.MODID, "ground_coffee"), GROUND_COFFEE);

        Registry.register(Registry.ITEM, new Identifier(Caffeinated.MODID, "coffee_berries"), COFFEE_BERRIES);
        Registry.register(Registry.ITEM, new Identifier(Caffeinated.MODID, "coffee_bottle"), COFFEE_BOTTLE);
        Registry.register(Registry.ITEM, new Identifier(Caffeinated.MODID, "tiramisu"), TIRAMISU);
    }

    public static void registerCompostingChances() {
        CompostingChanceRegistry.INSTANCE.add(COFFEE_BEAN_BLOCK, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(GROUND_COFFEE_BLOCK, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(COFFEE_BEANS, 0.5F);
        CompostingChanceRegistry.INSTANCE.add(GROUND_COFFEE, 0.5F);
        CompostingChanceRegistry.INSTANCE.add(COFFEE_BERRIES, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(TIRAMISU, 1.0F);
    }

    @Nullable
    public static ItemGroup getFarmersDelightItemGroup() {
        return FabricLoader.getInstance().isModLoaded("farmersdelight") ? FarmersDelightItemGroup.getFarmersDelightItemGroup() : null;
    }
}
