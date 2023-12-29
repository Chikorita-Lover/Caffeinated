package com.chikoritalover.caffeinated.advancement;

import com.chikoritalover.caffeinated.Caffeinated;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class BrewCoffeeCriterion extends AbstractCriterion<BrewCoffeeCriterion.Conditions> {
    static final Identifier ID = new Identifier(Caffeinated.MODID, "brew_coffee");

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(obj.get("item"));
        return new Conditions(playerPredicate, itemPredicate);
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        this.trigger(player, conditions -> conditions.matches(stack));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final ItemPredicate itemPredicate;

        public Conditions(LootContextPredicate player, ItemPredicate item) {
            super(ID, player);
            this.itemPredicate = item;
        }

        public boolean matches(ItemStack stack) {
            return this.itemPredicate.test(stack);
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("item", this.itemPredicate.toJson());
            return jsonObject;
        }
    }
}
