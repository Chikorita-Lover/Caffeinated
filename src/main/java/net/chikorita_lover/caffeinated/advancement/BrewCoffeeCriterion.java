package net.chikorita_lover.caffeinated.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.chikorita_lover.caffeinated.Caffeinated;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

public class BrewCoffeeCriterion extends AbstractCriterion<BrewCoffeeCriterion.Conditions> {
    public Codec<Conditions> getConditionsCodec() {
        return BrewCoffeeCriterion.Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        this.trigger(player, (conditions) -> conditions.matches(stack));
    }

    public record Conditions(Optional<LootContextPredicate> player,
                             ItemPredicate predicate) implements AbstractCriterion.Conditions {
        public static final Codec<BrewCoffeeCriterion.Conditions> CODEC = RecordCodecBuilder.create((instance) -> instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(BrewCoffeeCriterion.Conditions::player), ItemPredicate.CODEC.fieldOf("result").forGetter(Conditions::predicate)).apply(instance, Conditions::new));

        public static AdvancementCriterion<BrewCoffeeCriterion.Conditions> create(ItemPredicate.Builder builder) {
            return Caffeinated.BREW_COFFEE_CRITERION.create(new BrewCoffeeCriterion.Conditions(Optional.empty(), builder.build()));
        }

        public boolean matches(ItemStack stack) {
            return this.predicate.test(stack);
        }
    }
}
