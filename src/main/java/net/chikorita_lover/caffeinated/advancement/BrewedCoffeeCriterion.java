package net.chikorita_lover.caffeinated.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.chikorita_lover.caffeinated.Caffeinated;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.block.BlockState;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class BrewedCoffeeCriterion extends AbstractCriterion<BrewedCoffeeCriterion.Conditions> {
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, BlockPos pos) {
        ServerWorld world = player.getServerWorld();
        BlockState state = world.getBlockState(pos);
        LootContextParameterSet parameters = new LootContextParameterSet.Builder(world).add(LootContextParameters.ORIGIN, pos.toCenterPos()).add(LootContextParameters.THIS_ENTITY, player).add(LootContextParameters.BLOCK_STATE, state).build(LootContextTypes.BLOCK_USE);
        final LootContext location = new LootContext.Builder(parameters).build(Optional.empty());
        this.trigger(player, conditions -> conditions.test(location));
    }

    public record Conditions(Optional<LootContextPredicate> player,
                             Optional<LootContextPredicate> location) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC.optionalFieldOf("player").forGetter(Conditions::player), LootContextPredicate.CODEC.optionalFieldOf("location").forGetter(Conditions::location)).apply(instance, Conditions::new));

        public static AdvancementCriterion<Conditions> create() {
            return Caffeinated.BREWED_COFFEE_CRITERION.create(new Conditions(Optional.empty(), Optional.empty()));
        }

        public boolean test(LootContext location) {
            return this.location.isEmpty() || this.location.get().test(location);
        }
    }
}
