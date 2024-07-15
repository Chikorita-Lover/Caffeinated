package net.chikorita_lover.caffeinated.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.registry.CaffeinatedRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Objects;

public class CoffeeBrewingRecipe implements Recipe<CoffeeBrewingRecipeInput> {
    protected final RecipeType<?> type;
    protected final String group;
    protected final Ingredient input;
    protected final Ingredient reagent;
    protected final ItemStack output;
    protected final float experience;
    protected final int brewTime;

    public CoffeeBrewingRecipe(String group, Ingredient input, Ingredient reagent, ItemStack output, float experience, int brewTime) {
        this.type = CaffeinatedRecipeTypes.COFFEE_BREWING;
        this.group = group;
        this.input = input;
        this.reagent = reagent;
        this.output = output;
        this.experience = experience;
        this.brewTime = brewTime;
    }

    @Override
    public boolean matches(CoffeeBrewingRecipeInput input, World world) {
        return this.input.test(input.input()) && this.reagent.test(input.reagent());
    }

    @Override
    public ItemStack craft(CoffeeBrewingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return this.output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.output;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> defaultedList = DefaultedList.of();
        defaultedList.add(this.input);
        defaultedList.add(this.reagent);
        return defaultedList;
    }

    public float getExperience() {
        return this.experience;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public int getBrewTime() {
        return this.brewTime;
    }

    @Override
    public RecipeType<?> getType() {
        return this.type;
    }

    @Override
    public ItemStack createIcon() {
        return Items.CAULDRON.getDefaultStack();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Caffeinated.COFFEE_BREWING_SERIALIZER;
    }

    public static class Serializer<T extends CoffeeBrewingRecipe> implements RecipeSerializer<T> {
        private final CoffeeBrewingRecipe.Serializer.RecipeFactory<T> recipeFactory;
        private final MapCodec<T> codec;
        private final PacketCodec<RegistryByteBuf, T> packetCodec;

        public Serializer(CoffeeBrewingRecipe.Serializer.RecipeFactory<T> recipeFactory, int brewingTime) {
            this.recipeFactory = recipeFactory;
            this.codec = RecordCodecBuilder.mapCodec((instance) -> {
                var var10000 = instance.group(Codec.STRING.optionalFieldOf("group", "").forGetter((recipe) -> recipe.group), Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("ingredient").forGetter((recipe) -> recipe.input), Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("reagent").forGetter((recipe) -> recipe.reagent), ItemStack.VALIDATED_UNCOUNTED_CODEC.fieldOf("result").forGetter((recipe) -> recipe.output), Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter((recipe) -> recipe.experience), Codec.INT.fieldOf("brewingtime").orElse(brewingTime).forGetter((recipe) -> recipe.brewTime));
                Objects.requireNonNull(recipeFactory);
                return var10000.apply(instance, recipeFactory::create);
            });
            this.packetCodec = PacketCodec.ofStatic(this::write, this::read);
        }

        public MapCodec<T> codec() {
            return this.codec;
        }

        public PacketCodec<RegistryByteBuf, T> packetCodec() {
            return this.packetCodec;
        }

        public T read(RegistryByteBuf buf) {
            String group = buf.readString();
            Ingredient input = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient reagent = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack output = ItemStack.PACKET_CODEC.decode(buf);
            float experience = buf.readFloat();
            int brewingTime = buf.readVarInt();
            return this.recipeFactory.create(group, input, reagent, output, experience, brewingTime);
        }

        public void write(RegistryByteBuf buf, T coffeeBrewingRecipe) {
            buf.writeString(coffeeBrewingRecipe.group);
            Ingredient.PACKET_CODEC.encode(buf, coffeeBrewingRecipe.input);
            Ingredient.PACKET_CODEC.encode(buf, coffeeBrewingRecipe.reagent);
            ItemStack.PACKET_CODEC.encode(buf, coffeeBrewingRecipe.output);
            buf.writeFloat(coffeeBrewingRecipe.experience);
            buf.writeVarInt(coffeeBrewingRecipe.brewTime);
        }

        public interface RecipeFactory<T extends CoffeeBrewingRecipe> {
            T create(String group, Ingredient input, Ingredient reagent, ItemStack output, float experience, int brewingTime);
        }
    }
}
