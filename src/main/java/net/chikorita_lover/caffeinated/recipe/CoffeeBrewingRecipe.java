package com.chikoritalover.caffeinated.recipe;

import com.chikoritalover.caffeinated.Caffeinated;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CoffeeBrewingRecipe implements Recipe<Inventory> {
    protected final RecipeType<?> type;
    protected final Identifier id;
    protected final String group;
    protected final Ingredient input;
    protected final Ingredient reagent;
    protected final ItemStack output;
    protected final float experience;
    protected final int brewTime;

    public CoffeeBrewingRecipe(Identifier id, String group, Ingredient input, Ingredient reagent, ItemStack output, float experience, int brewTime) {
        this.type = Caffeinated.COFFEE_BREWING;
        this.id = id;
        this.group = group;
        this.input = input;
        this.reagent = reagent;
        this.output = output;
        this.experience = experience;
        this.brewTime = brewTime;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.input.test(inventory.getStack(0)) && this.reagent.test(inventory.getStack(1));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return this.output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
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
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.output;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    public int getBrewTime() {
        return this.brewTime;
    }

    @Override
    public Identifier getId() {
        return this.id;
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
        private final int brewingTime;
        final CoffeeBrewingRecipe.Serializer.RecipeFactory<T> recipeFactory;

        protected Serializer(CoffeeBrewingRecipe.Serializer.RecipeFactory<T> recipeFactory, int brewingTime) {
            this.brewingTime = brewingTime;
            this.recipeFactory = recipeFactory;
        }

        @Override
        public T read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
            JsonElement jsonElement2 = JsonHelper.hasArray(jsonObject, "reagent") ? JsonHelper.getArray(jsonObject, "reagent") : JsonHelper.getObject(jsonObject, "reagent");
            Ingredient ingredient = Ingredient.fromJson(jsonElement, false);
            Ingredient ingredient2 = Ingredient.fromJson(jsonElement2, false);
            String string2 = JsonHelper.getString(jsonObject, "result");
            Identifier identifier2 = new Identifier(string2);
            ItemStack itemStack = new ItemStack(Registries.ITEM.getOrEmpty(identifier2).orElseThrow(() -> new IllegalStateException("Item: " + string2 + " does not exist")));
            float f = JsonHelper.getFloat(jsonObject, "experience", 0.0F);
            int i = JsonHelper.getInt(jsonObject, "brewingtime", this.brewingTime);
            return this.recipeFactory.create(identifier, string, ingredient, ingredient2, itemStack, f, i);
        }

        @Override
        public T read(Identifier identifier, PacketByteBuf packetByteBuf) {
            String string = packetByteBuf.readString();
            Ingredient ingredient = Ingredient.fromPacket(packetByteBuf);
            Ingredient ingredient2 = Ingredient.fromPacket(packetByteBuf);
            ItemStack itemStack = packetByteBuf.readItemStack();
            float f = packetByteBuf.readFloat();
            int i = packetByteBuf.readVarInt();
            return this.recipeFactory.create(identifier, string, ingredient, ingredient2, itemStack, f, i);
        }

        @Override
        public void write(PacketByteBuf packetByteBuf, T coffeeBrewingRecipe) {
            packetByteBuf.writeString(coffeeBrewingRecipe.group);
            coffeeBrewingRecipe.input.write(packetByteBuf);
            coffeeBrewingRecipe.reagent.write(packetByteBuf);
            packetByteBuf.writeItemStack(coffeeBrewingRecipe.output);
            packetByteBuf.writeFloat(coffeeBrewingRecipe.experience);
            packetByteBuf.writeVarInt(coffeeBrewingRecipe.brewTime);
        }

        public interface RecipeFactory<T extends CoffeeBrewingRecipe> {
            T create(Identifier identifier, String group, Ingredient input, Ingredient reagent, ItemStack output, float experience, int brewingTime);
        }
    }
}
