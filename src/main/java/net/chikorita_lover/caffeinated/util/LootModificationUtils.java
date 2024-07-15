package net.chikorita_lover.caffeinated.util;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.loot.v3.FabricLootPoolBuilder;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class LootModificationUtils {
    /**
     * Modifies a single loot pool present in the provided builder.
     *
     * <p>This method can be used instead of simply adding a new pool
     * when you want the loot table to only drop items from one of the loot pool entries
     * instead of multiple.
     *
     * <p>Calling this method turns the loot pool at the specified index into a builder and rebuilds it back into a loot pool afterward.
     *
     * @param builder  the loot table builder
     * @param index    the list index of the target loot pool
     * @param modifier the modifying function
     */
    public static void modifyPool(LootTable.Builder builder, int index, Consumer<? super LootPool.Builder> modifier) {
        ArrayList<LootPool> list = new ArrayList<>(builder.pools.build());

        LootPool.Builder poolBuilder = FabricLootPoolBuilder.copyOf(list.get(index));
        modifier.accept(poolBuilder);
        list.set(index, poolBuilder.build());

        builder.pools = ImmutableList.<LootPool>builder().addAll(list);
    }

    /**
     * Modifies the loot pool entries of a provided loot pool builder.
     *
     * <p>Calling this method creates a new mutable list from the builder's
     * entries and assigns the new list to the builder after making modifications.
     *
     * @param builder  the loot pool builder
     * @param modifier the modifying function
     */
    public static void modifyPoolEntries(LootPool.Builder builder, Consumer<ArrayList<LootPoolEntry>> modifier) {
        ArrayList<LootPoolEntry> entries = new ArrayList<>(builder.build().entries);
        modifier.accept(entries);
        builder.entries = ImmutableList.<LootPoolEntry>builder().addAll(entries);
    }

    /**
     * Removes all item entries in a loot pool builder that contain a matching item.
     *
     * <p>This method accepts a predicate for testing the item of each
     * loot entry. Any entry that passes the test is removed from the loot pool.
     *
     * <p>Calling this method creates a new mutable list from the builder's
     * entries and assigns the new list to the builder after making modifications.
     *
     * @param builder       the loot pool builder
     * @param itemPredicate the predicate to test entry items against
     */
    public static void removeItemIf(LootPool.Builder builder, Predicate<Item> itemPredicate) {
        modifyPoolEntries(builder, entries -> entries.removeIf(entry -> entry instanceof ItemEntry itemEntry && itemPredicate.test(itemEntry.item.value())));
    }
}
