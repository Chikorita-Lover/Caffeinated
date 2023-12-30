package com.chikoritalover.caffeinated.block.entity;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.block.CauldronCampfireBlock;
import com.chikoritalover.caffeinated.recipe.CoffeeBrewingRecipe;
import com.chikoritalover.caffeinated.registry.CaffeinatedItems;
import com.chikoritalover.caffeinated.registry.CaffeinatedSoundEvents;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
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
import java.util.List;
import java.util.Optional;

public class CauldronCampfireBlockEntity extends BlockEntity implements RecipeUnlocker {
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    int brewingTime;
    int brewingTimeTotal;
    private final ArrayList<Identifier> recipesUsed = new ArrayList<>();
    private final RecipeManager.MatchGetter<Inventory, CoffeeBrewingRecipe> matchGetter = RecipeManager.createCachedMatchGetter(Caffeinated.COFFEE_BREWING);

    public CauldronCampfireBlockEntity(BlockPos pos, BlockState state) {
        super(Caffeinated.CAULDRON_CAMPFIRE, pos, state);
        this.brewingTime = 0;
        this.brewingTimeTotal = 600;
    }

    public static void litServerTick(World world, BlockPos pos, BlockState state, CauldronCampfireBlockEntity cauldronCampfire) {
        ItemStack stack = cauldronCampfire.getStack(0);
        if (state.get(CauldronCampfireBlock.FILLED) && cauldronCampfire.canBrewTogether(stack, cauldronCampfire.getStack(1))) {
            cauldronCampfire.brewingTime++;
            if (cauldronCampfire.brewingTime >= cauldronCampfire.brewingTimeTotal) {
                Optional<CoffeeBrewingRecipe> optional;
                SimpleInventory inventory;
                cauldronCampfire.brewingTime = 0;
                ItemStack itemStack = (optional = cauldronCampfire.matchGetter.getFirstMatch(inventory = new SimpleInventory(stack, cauldronCampfire.getStack(1)), world)).map(recipe -> recipe.craft(inventory, world.getRegistryManager())).orElse(stack);
                cauldronCampfire.clear();
                cauldronCampfire.setStack(0, itemStack);
                optional.ifPresent(recipe -> cauldronCampfire.recipesUsed.add(recipe.getId()));
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
            boolean bl = cauldronCampfire.hasReagent();
            if (bl) {
                float f = random.nextFloat() * MathHelper.PI * 2.0F;
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5 + MathHelper.sin(f) * 0.45, pos.getY() + random.nextBetween(6, 8) / 16.0, pos.getZ() + 0.5 + MathHelper.cos(f) * 0.45, 0.0, 5.0E-4, 0.0);
            }

            if (random.nextInt(50) == 0) {
                world.playSoundAtBlockCenter(pos, CaffeinatedSoundEvents.BLOCK_CAULDRON_CAMPFIRE_BUBBLE, SoundCategory.BLOCKS, bl ? 0.2F : 0.1F, bl ? 1.0F : 2.0F, false);
            }
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory.clear();
        Inventories.readNbt(nbt, this.inventory);
        this.brewingTime = nbt.getInt("BrewingTime");
        this.brewingTimeTotal = nbt.getInt("BrewingTimeTotal");
        NbtList nbtList = nbt.getList("RecipesUsed", NbtElement.STRING_TYPE);
        for (NbtElement nbtElement : nbtList) {
            this.recipesUsed.add(new Identifier(nbtElement.asString()));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory, true);
        nbt.putInt("BrewingTime", this.brewingTime);
        nbt.putInt("BrewingTimeTotal", this.brewingTimeTotal);
        NbtList nbtList = new NbtList();
        this.recipesUsed.forEach(identifier -> nbtList.add(NbtString.of(identifier.toString())));
        nbt.put("RecipesUsed", nbtList);
    }

    @Override
    public Packet toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbtCompound = new NbtCompound();
        Inventories.writeNbt(nbtCompound, this.inventory, true);
        return nbtCompound;
    }

    public Optional<CoffeeBrewingRecipe> getRecipeFor(ItemStack... stacks) {
        return this.matchGetter.getFirstMatch(new SimpleInventory(stacks), this.world);
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
        Optional<CoffeeBrewingRecipe> optional;
        if ((optional = getRecipeFor(this.getStack(0), stack)).isPresent()) {
            this.brewingTimeTotal = optional.get().getBrewTime();
        }
        this.getWorld().emitGameEvent(GameEvent.BLOCK_CHANGE, this.getPos(), GameEvent.Emitter.of(user, this.getCachedState()));
        this.updateListeners();
    }

    public boolean isBaseIngredient(ItemStack stack) {
        if (stack.isOf(Items.POTION)) {
            return PotionUtil.getPotion(stack) == Potions.WATER;
        }
        return this.getWorld().getRecipeManager().listAllOfType(Caffeinated.COFFEE_BREWING).stream().anyMatch(coffeeBrewingRecipe -> coffeeBrewingRecipe.getIngredients().get(0).test(stack));
    }

    public boolean canBrewTogether(ItemStack stack, ItemStack stack2) {
        return getRecipeFor(stack, stack2).isPresent() && (!stack.isOf(Items.POTION) || PotionUtil.getPotion(stack) == Potions.WATER);
    }

    public boolean hasReagent() {
        return !this.getStack(1).isEmpty();
    }

    public int getColor() {
        if (this.getStack(0).isOf(Items.POTION)) {
            return BiomeColors.getWaterColor(this.getWorld(), this.getPos());
        }
        if (this.getStack(0).isOf(CaffeinatedItems.COFFEE_BOTTLE)) {
            return 0x4A2E20;
        }
        return -1;
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

    public void dropExperienceForRecipesUsed(ServerPlayerEntity player) {
        ArrayList<Recipe<?>> arrayList = new ArrayList<>();
        for (Identifier identifier : this.recipesUsed) {
            this.getWorld().getRecipeManager().get(identifier).ifPresent(arrayList::add);
        }
        player.unlockRecipes(arrayList);
        for (Recipe<?> recipe : arrayList) {
            if (recipe == null) continue;
            player.onRecipeCrafted(recipe, this.inventory);
            dropExperience(player.getServerWorld(), player.getPos(), ((CoffeeBrewingRecipe) recipe).getExperience());
        }
        this.recipesUsed.clear();
    }

    private static void dropExperience(ServerWorld world, Vec3d pos, float experience) {
        int i = MathHelper.floor(experience);
        float f = MathHelper.fractionalPart(experience);
        if (f != 0.0f && Math.random() < f) {
            i++;
        }
        ExperienceOrbEntity.spawn(world, pos, i);
    }

    @Override
    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            this.recipesUsed.add(recipe.getId());
        }
    }

    @Nullable
    @Override
    public Recipe<?> getLastRecipe() {
        return null;
    }
}
