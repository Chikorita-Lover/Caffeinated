package net.chikorita_lover.caffeinated;

import com.chocohead.mm.api.ClassTinkerers;
import net.chikorita_lover.caffeinated.registry.CaffeinatedItems;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CaffeinatedEarlyRiser implements Runnable {
    @Override
    public void run() {
        MappingResolver mappingResolver = FabricLoader.getInstance().getMappingResolver();

        String recipeBookGroup = mappingResolver.mapClassName("intermediary", "net.minecraft.class_314");
        String itemStack = "[L" + mappingResolver.mapClassName("intermediary", "net.minecraft.class_1799") + ';';
        ClassTinkerers.enumBuilder(recipeBookGroup, itemStack).addEnum("CAFFEINATED_SMOKER_MISC", () -> new Object[]{new ItemStack[]{new ItemStack(Items.LAVA_BUCKET), new ItemStack(CaffeinatedItems.COFFEE_BERRIES)}}).build();
        ClassTinkerers.enumBuilder(recipeBookGroup, itemStack).addEnum("CAFFEINATED_COFFEE_BREWING", () -> new Object[]{new ItemStack[]{new ItemStack(CaffeinatedItems.COFFEE_BOTTLE)}}).build();
    }
}
