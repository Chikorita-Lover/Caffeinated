package net.chikorita_lover.caffeinated.data;

import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedItems;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class CaffeinatedItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public CaffeinatedItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries) {
        this.getOrCreateTagBuilder(CaffeinatedItemTags.CIVET_FOOD).forceAddTag(ConventionalItemTags.BERRY_FOODS);
        this.getOrCreateTagBuilder(CaffeinatedItemTags.COFFEE_BOTTLES).add(CaffeinatedItems.COFFEE_BOTTLE, CaffeinatedItems.LATTE_COFFEE_BOTTLE, CaffeinatedItems.CAFE_MIEL_COFFEE_BOTTLE);
        this.getOrCreateTagBuilder(CaffeinatedItemTags.COFFEE_FOOD).addTag(CaffeinatedItemTags.COFFEE_BOTTLES).add(CaffeinatedItems.COFFEE_BERRIES).add(CaffeinatedItems.TIRAMISU).add(CaffeinatedItems.TIRAMISU_SLICE);
        this.getOrCreateTagBuilder(ItemTags.WOOL).add(CaffeinatedBlocks.COFFEE_STAINED_WOOL.asItem());
        this.getOrCreateTagBuilder(ItemTags.WOOL_CARPETS).add(CaffeinatedBlocks.COFFEE_STAINED_CARPET.asItem());
    }
}
