package net.chikorita_lover.caffeinated.data;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.advancement.BrewCoffeeCriterion;
import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedItems;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.item.Item;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CaffeinatedAdvancementProvider extends FabricAdvancementProvider {
    public CaffeinatedAdvancementProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(output, registryLookup);
    }

    private static Advancement.Builder requireAllCoffeeBottlesBrewed(Advancement.Builder builder, RegistryWrapper.WrapperLookup registryLookup, Item... items) {
        for (Item item : items) {
            builder.criterion("brew_" + Registries.ITEM.getId(item).getPath(), BrewCoffeeCriterion.Conditions.create(ItemPredicate.Builder.create().items(item)));
        }
        return builder;
    }

    @Override
    public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
        AdvancementEntry husbandryRoot = new AdvancementEntry(Identifier.ofVanilla("husbandry/root"), null);
        AdvancementEntry brewCoffee = Advancement.Builder.create().parent(husbandryRoot).display(CaffeinatedItems.BLACK_COFFEE_BOTTLE, Text.translatable("advancements.husbandry.brew_coffee.title"), Text.translatable("advancements.husbandry.brew_coffee.description"), null, AdvancementFrame.TASK, true, true, false).criterion("brew_coffee", BrewCoffeeCriterion.Conditions.create(ItemPredicate.Builder.create().tag(CaffeinatedItemTags.BLACK_COFFEE_BOTTLES))).build(consumer, Caffeinated.NAMESPACE + ":husbandry/brew_coffee");
        requireAllCoffeeBottlesBrewed(Advancement.Builder.create().parent(brewCoffee).display(CaffeinatedItems.LATTE_COFFEE_BOTTLE, Text.translatable("advancements.husbandry.brew_all_coffee.title"), Text.translatable("advancements.husbandry.brew_all_coffee.description"), null, AdvancementFrame.CHALLENGE, true, true, false), registryLookup, CaffeinatedItems.BLACK_COFFEE_BOTTLE, CaffeinatedItems.LATTE_COFFEE_BOTTLE).rewards(AdvancementRewards.Builder.experience(100)).build(consumer, Caffeinated.NAMESPACE + ":husbandry/brew_all_coffee");
        Advancement.Builder.create().parent(brewCoffee).display(CaffeinatedBlocks.TIRAMISU, Text.translatable("advancements.husbandry.bake_tiramisu.title"), Text.translatable("advancements.husbandry.bake_tiramisu.description"), null, AdvancementFrame.TASK, true, true, false).criterion("bake_tiramisu", InventoryChangedCriterion.Conditions.items(CaffeinatedBlocks.TIRAMISU)).build(consumer, Caffeinated.NAMESPACE + ":husbandry/bake_tiramisu");
    }
}
