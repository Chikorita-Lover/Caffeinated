package net.chikorita_lover.caffeinated.block.entity;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.block.CauldronCampfireBlock;
import net.chikorita_lover.caffeinated.recipe.CoffeeBrewingRecipe;
import net.chikorita_lover.caffeinated.recipe.CoffeeBrewingRecipeInput;
import net.chikorita_lover.caffeinated.registry.CaffeinatedBlockEntityTypes;
import net.chikorita_lover.caffeinated.registry.CaffeinatedRecipeTypes;
import net.chikorita_lover.caffeinated.registry.CaffeinatedSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;

public class CauldronCampfireBlockEntity extends BlockEntity implements RecipeUnlocker {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private final ArrayList<Identifier> recipesUsed = new ArrayList<>();
    private final RecipeManager.MatchGetter<CoffeeBrewingRecipeInput, CoffeeBrewingRecipe> matchGetter = RecipeManager.createCachedMatchGetter(CaffeinatedRecipeTypes.COFFEE_BREWING);
    private int brewingTime;
    private int brewingTimeTotal;

    public CauldronCampfireBlockEntity(BlockPos pos, BlockState state) {
        super(CaffeinatedBlockEntityTypes.CAULDRON_CAMPFIRE, pos, state);
        this.brewingTime = 0;
        this.brewingTimeTotal = 600;
    }

    public static void litServerTick(World world, BlockPos pos, BlockState state, CauldronCampfireBlockEntity cauldronCampfire) {
        ItemStack inputStack = cauldronCampfire.getStack(0);
        if (state.get(CauldronCampfireBlock.FILLED) && cauldronCampfire.canBrewTogether(inputStack, cauldronCampfire.getStack(1))) {
            cauldronCampfire.brewingTime++;
            if (cauldronCampfire.brewingTime >= cauldronCampfire.brewingTimeTotal) {
                Optional<RecipeEntry<CoffeeBrewingRecipe>> optional;
                CoffeeBrewingRecipeInput recipeInput;
                cauldronCampfire.brewingTime = 0;
                ItemStack outputStack = (optional = cauldronCampfire.matchGetter.getFirstMatch(recipeInput = new CoffeeBrewingRecipeInput(inputStack, cauldronCampfire.getStack(1)), world)).map(recipe -> recipe.value().craft(recipeInput, world.getRegistryManager())).orElse(inputStack);
                cauldronCampfire.clear();
                cauldronCampfire.setStack(0, outputStack);
                optional.ifPresent(recipe -> cauldronCampfire.recipesUsed.add(recipe.id()));
                world.playSound(null, pos, CaffeinatedSoundEvents.BLOCK_CAULDRON_CAMPFIRE_BREW, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
            }
            CauldronCampfireBlockEntity.markDirty(world, pos, state);
        }
    }

    public static void clientTick(World world, BlockPos pos, BlockState state, CauldronCampfireBlockEntity cauldronCampfire) {
        Random random = world.getRandom();
        if (random.nextFloat() < 0.06F) {
            for (int i = 0; i < random.nextInt(2) + 2; i++) {
                CauldronCampfireBlock.spawnSmokeParticle(world, pos, false);
            }
        }
        if (state.get(CauldronCampfireBlock.FILLED)) {
            boolean hasReagent = cauldronCampfire.hasReagent();
            if (hasReagent) {
                float f = random.nextFloat() * MathHelper.PI * 2.0F;
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5 + MathHelper.sin(f) * 0.45, pos.getY() + random.nextBetween(6, 8) / 16.0, pos.getZ() + 0.5 + MathHelper.cos(f) * 0.45, 0.0, 5.0E-4, 0.0);
            }
            if (random.nextInt(50) == 0) {
                world.playSoundAtBlockCenter(pos, CaffeinatedSoundEvents.BLOCK_CAULDRON_CAMPFIRE_BUBBLE, SoundCategory.BLOCKS, hasReagent ? 0.2F : 0.1F, hasReagent ? 1.0F : 2.0F, false);
            }
        }
    }

    private static void dropExperience(ServerWorld world, Vec3d pos, float experience) {
        int i = MathHelper.floor(experience);
        float f = MathHelper.fractionalPart(experience);
        if (Math.random() < f) {
            i++;
        }
        ExperienceOrbEntity.spawn(world, pos, i);
    }

    private static boolean isOfWaterBottle(ItemStack stack) {
        if (stack.isOf(Items.POTION)) {
            PotionContentsComponent component = stack.get(DataComponentTypes.POTION_CONTENTS);
            return component != null && component.potion().isPresent() && component.potion().get() == Potions.WATER;
        }
        return false;
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.inventory.clear();
        Inventories.readNbt(nbt, this.inventory, registryLookup);
        this.brewingTime = nbt.getInt("BrewingTime");
        this.brewingTimeTotal = nbt.getInt("BrewingTimeTotal");
        NbtList nbtList = nbt.getList("RecipesUsed", NbtElement.STRING_TYPE);
        for (NbtElement nbtElement : nbtList) {
            this.recipesUsed.add(Identifier.of(nbtElement.asString()));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        Inventories.writeNbt(nbt, this.inventory, registryLookup);
        nbt.putInt("BrewingTime", this.brewingTime);
        nbt.putInt("BrewingTimeTotal", this.brewingTimeTotal);
        NbtList nbtList = new NbtList();
        this.recipesUsed.forEach(identifier -> nbtList.add(NbtString.of(identifier.toString())));
        nbt.put("RecipesUsed", nbtList);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, this.inventory, registryLookup);
        return nbtCompound;
    }

    public Optional<RecipeEntry<CoffeeBrewingRecipe>> getRecipeFor(ItemStack input, ItemStack reagent) {
        return this.matchGetter.getFirstMatch(new CoffeeBrewingRecipeInput(input, reagent), this.world);
    }

    public ItemStack getStack(int slot) {
        return slot >= 0 && slot < this.inventory.size() ? this.inventory.get(slot) : ItemStack.EMPTY;
    }

    private void setStack(int slot, ItemStack stack) {
        if (slot >= 0 && slot < this.inventory.size()) {
            this.inventory.set(slot, stack);
            if (slot == 0) {
                BlockState state = this.getWorld().getBlockState(this.getPos());
                if (state.get(CauldronCampfireBlock.FILLED) == stack.isEmpty()) {
                    this.getWorld().setBlockState(this.getPos(), state.with(CauldronCampfireBlock.FILLED, !stack.isEmpty()));
                }
            }
        }
    }

    public void addBaseIngredient(@Nullable Entity user, ItemStack stack) {
        this.clear();
        this.brewingTime = 0;
        this.setStack(0, stack.copyWithCount(1));
        this.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, this.getPos(), GameEvent.Emitter.of(user, this.getCachedState()));
        this.updateListeners();
    }

    public void addReagent(@Nullable Entity user, ItemStack stack) {
        this.setStack(1, stack.copyWithCount(1));
        Optional<RecipeEntry<CoffeeBrewingRecipe>> optional;
        if ((optional = getRecipeFor(this.getStack(0), stack)).isPresent()) {
            this.brewingTimeTotal = optional.get().value().getBrewTime();
        }
        this.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, this.getPos(), GameEvent.Emitter.of(user, this.getCachedState()));
        this.updateListeners();
    }

    public boolean isBaseIngredient(ItemStack stack) {
        if (stack.isOf(Items.POTION)) {
            return isOfWaterBottle(stack);
        }
        return this.getWorld().getRecipeManager().listAllOfType(CaffeinatedRecipeTypes.COFFEE_BREWING).stream().anyMatch(coffeeBrewingRecipe -> coffeeBrewingRecipe.value().getIngredients().get(0).test(stack));
    }

    public boolean canBrewTogether(ItemStack input, ItemStack reagent) {
        return this.getRecipeFor(input, reagent).isPresent() && (!input.isOf(Items.POTION) || isOfWaterBottle(input));
    }

    public boolean hasReagent() {
        return !this.getStack(1).isEmpty();
    }

    private void updateListeners() {
        this.markDirty();
        this.getWorld().updateListeners(this.getPos(), this.getCachedState(), this.getCachedState(), Block.NOTIFY_ALL);
    }

    public void clear() {
        this.inventory.clear();
        BlockState state = this.getWorld().getBlockState(this.getPos());
        if (state.get(CauldronCampfireBlock.FILLED)) {
            this.getWorld().setBlockState(this.getPos(), state.with(CauldronCampfireBlock.FILLED, false));
        }
    }

    public void onRecipesCrafted(ServerPlayerEntity player) {
        ArrayList<RecipeEntry<?>> recipeEntries = new ArrayList<>();
        for (Identifier recipeId : this.recipesUsed) {
            this.getWorld().getRecipeManager().get(recipeId).ifPresent(recipeEntries::add);
        }
        player.unlockRecipes(recipeEntries);
        for (RecipeEntry<?> recipe : recipeEntries) {
            if (recipe == null || !(recipe.value() instanceof CoffeeBrewingRecipe coffeeBrewingRecipe)) {
                continue;
            }
            player.onRecipeCrafted(recipe, this.inventory);
            player.incrementStat(Stats.CRAFTED.getOrCreateStat(coffeeBrewingRecipe.getResult(this.world.getRegistryManager()).getItem()));
            Caffeinated.BREW_COFFEE_CRITERION.trigger(player, coffeeBrewingRecipe.getResult(this.getWorld().getRegistryManager()));
            dropExperience(player.getServerWorld(), player.getPos(), coffeeBrewingRecipe.getExperience());
        }
        this.recipesUsed.clear();
    }

    @Override
    public RecipeEntry<?> getLastRecipe() {
        return null;
    }

    @Override
    public void setLastRecipe(@Nullable RecipeEntry<?> recipe) {
        if (recipe != null) {
            this.recipesUsed.add(recipe.id());
        }
    }
}
