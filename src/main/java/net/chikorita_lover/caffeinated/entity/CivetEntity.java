package net.chikorita_lover.caffeinated.entity;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.chikorita_lover.caffeinated.block.FloweringCoffeeShrubBlock;
import net.chikorita_lover.caffeinated.registry.CaffeinatedBlocks;
import net.chikorita_lover.caffeinated.registry.CaffeinatedEntities;
import net.chikorita_lover.caffeinated.registry.CaffeinatedSoundEvents;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedBlockTags;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;

public class CivetEntity extends AnimalEntity {
    private static final String SCAT_DROP_TIME_KEY = "ScatDropTime";
    private static final String LAST_BERRY_POS_KEY = "LastBerryPos";
    private static final String REDUCED_SCAT_DROPS_KEY = "ReducedScatDrops";
    private GlobalPos lastBerryPos;
    private int scatDropTime = -1;
    private boolean reducedScatDrops;

    public CivetEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createCivetAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    public static boolean canSpawn(EntityType<CivetEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(CaffeinatedBlockTags.CIVETS_SPAWNABLE_ON) && isLightLevelValidForNaturalSpawn(world, pos);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new PounceAtTargetGoal(this, 0.3F));
        this.goalSelector.add(3, new AttackGoal(this));
        this.goalSelector.add(4, new AnimalMateGoal(this, 0.8));
        this.goalSelector.add(5, new TemptGoal(this, 1.0F, this::isBreedingItem, false));
        this.goalSelector.add(6, new FleeEntityGoal<>(this, PlayerEntity.class, 4.0F, 0.8F, 1.0F));
        this.goalSelector.add(6, new FleeEntityGoal<>(this, WolfEntity.class, 8.0F, 1.6F, 1.6F));
        this.goalSelector.add(7, new EatCoffeeBerriesGoal(1.0, 12, 4));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, RabbitEntity.class, false));
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, ChickenEntity.class, false));
    }

    @Override
    public CivetEntity createChild(ServerWorld world, PassiveEntity entity) {
        return CaffeinatedEntities.CIVET.create(world);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(CaffeinatedItemTags.CIVET_FOOD);
    }

    @Override
    public boolean canSpawn(WorldView world) {
        if (!world.doesNotIntersectEntities(this) || world.containsFluid(this.getBoundingBox())) {
            return false;
        }
        BlockPos pos = this.getBlockPos();
        if (pos.getY() < world.getSeaLevel()) {
            return false;
        }
        return world.getBlockState(pos.down()).isIn(CaffeinatedBlockTags.CIVETS_SPAWNABLE_ON);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.getWorld().isClient() && this.isAlive() && this.scatDropTime > 0 && --this.scatDropTime == 0) {
            Random random = this.getRandom();
            int count = this.reducedScatDrops ? random.nextBetween(1, 2) : random.nextBetween(3, 5);
            this.dropStack(new ItemStack(CaffeinatedBlocks.CIVET_SCAT, count));
            this.playSound(CaffeinatedSoundEvents.ENTITY_CIVET_SCAT_DROP, 1.0F, MathHelper.nextBetween(random, 0.8F, 1.2F));
            this.emitGameEvent(GameEvent.ENTITY_PLACE);
            this.scatDropTime = -1;
        }
    }

    @Override
    public Vec3d getLeashOffset() {
        return new Vec3d(0.0, 0.5F * this.getStandingEyeHeight(), this.getWidth() * 0.5F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return CaffeinatedSoundEvents.ENTITY_CIVET_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return CaffeinatedSoundEvents.ENTITY_CIVET_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return CaffeinatedSoundEvents.ENTITY_CIVET_DEATH;
    }

    @Override
    public SoundEvent getEatSound(ItemStack stack) {
        return CaffeinatedSoundEvents.ENTITY_CIVET_EAT;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains(SCAT_DROP_TIME_KEY)) {
            this.scatDropTime = nbt.getInt(SCAT_DROP_TIME_KEY);
        }
        if (this.scatDropTime >= 0) {
            this.reducedScatDrops = nbt.getBoolean(REDUCED_SCAT_DROPS_KEY);
        }
        if (nbt.contains(LAST_BERRY_POS_KEY)) {
            NbtElement element = nbt.get(LAST_BERRY_POS_KEY);
            this.lastBerryPos = GlobalPos.CODEC.parse(this.getRegistryManager().getOps(NbtOps.INSTANCE), element).resultOrPartial(error -> Caffeinated.LOGGER.error("Tried to load invalid position: {}", error)).orElse(null);
        }
    }

    @Override
    public void writeCustomDataToNbt(final NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt(SCAT_DROP_TIME_KEY, this.scatDropTime);
        if (this.scatDropTime >= 0) {
            nbt.putBoolean(REDUCED_SCAT_DROPS_KEY, this.reducedScatDrops);
        }
        if (this.lastBerryPos != null) {
            GlobalPos.CODEC.encodeStart(this.getRegistryManager().getOps(NbtOps.INSTANCE), this.lastBerryPos).ifSuccess(element -> nbt.put(LAST_BERRY_POS_KEY, element));
        }
    }

    @Override
    protected void eat(PlayerEntity player, Hand hand, ItemStack stack) {
        if (this.isBreedingItem(stack)) {
            this.playSound(this.getEatSound(stack), 1.0F, 1.0F);
        }
        super.eat(player, hand, stack);
    }

    private void rememberBerryPos(BlockPos pos) {
        GlobalPos globalPos = GlobalPos.create(this.getWorld().getRegistryKey(), pos);
        this.reducedScatDrops = globalPos.equals(this.lastBerryPos);
        this.lastBerryPos = globalPos;
    }

    public class EatCoffeeBerriesGoal extends MoveToTargetPosGoal {
        private static final int EATING_TIME = 40;
        protected int timer;

        public EatCoffeeBerriesGoal(double speed, int range, int maxYDifference) {
            super(CivetEntity.this, speed, range, maxYDifference);
        }

        @Override
        public double getDesiredDistanceToTarget() {
            return 2.0;
        }

        @Override
        public boolean shouldResetPath() {
            return this.tryingTime % 100 == 0;
        }

        @Override
        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            return state.isOf(CaffeinatedBlocks.FLOWERING_COFFEE_SHRUB) && state.get(FloweringCoffeeShrubBlock.AGE) == FloweringCoffeeShrubBlock.MAX_AGE && state.get(FloweringCoffeeShrubBlock.HALF) == DoubleBlockHalf.LOWER;
        }

        @Override
        public void tick() {
            if (this.hasReached()) {
                if (this.timer >= EATING_TIME) {
                    this.eatBerries();
                } else {
                    ++this.timer;
                }
            }
            if (CivetEntity.this.random.nextFloat() < 0.05F) {
                SoundEvent sound = this.hasReached() ? CaffeinatedSoundEvents.ENTITY_CIVET_EAT : CaffeinatedSoundEvents.ENTITY_CIVET_SNIFF;
                CivetEntity.this.playSound(sound, 1.0F, 1.0F);
            }
            super.tick();
        }

        private void eatBerries() {
            World world = CivetEntity.this.getWorld();
            if (CivetEntity.this.scatDropTime >= 0 || !world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                return;
            }
            BlockPos pos = this.getTargetPos();
            CivetEntity.this.rememberBerryPos(pos);
            FloweringCoffeeShrubBlock.pickBerries(CivetEntity.this, world.getBlockState(pos), world, pos, true);
            CivetEntity.this.playSound(CaffeinatedSoundEvents.ENTITY_CIVET_EAT, 1.0F, 1.0F);
            CivetEntity.this.scatDropTime = CivetEntity.this.getRandom().nextInt(6000) + 6000;
        }

        @Override
        public boolean canStart() {
            return CivetEntity.this.scatDropTime < 0 && !CivetEntity.this.isBaby() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return CivetEntity.this.scatDropTime < 0 && super.shouldContinue();
        }

        @Override
        public void start() {
            this.timer = 0;
            super.start();
        }
    }
}
