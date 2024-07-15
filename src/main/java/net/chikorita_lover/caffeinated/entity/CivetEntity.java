package net.chikorita_lover.caffeinated.entity;

import net.chikorita_lover.caffeinated.registry.CaffeinatedEntities;
import net.chikorita_lover.caffeinated.registry.CaffeinatedSoundEvents;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedBlockTags;
import net.chikorita_lover.caffeinated.registry.tag.CaffeinatedItemTags;
import net.minecraft.block.BlockState;
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
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public class CivetEntity extends AnimalEntity {
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.fromTag(CaffeinatedItemTags.CIVET_FOOD);

    public CivetEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createCivetAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(2, new PounceAtTargetGoal(this, 0.3F));
        this.goalSelector.add(3, new AttackGoal(this));
        this.goalSelector.add(4, new AnimalMateGoal(this, 0.8));
        this.goalSelector.add(5, new TemptGoal(this, 1.0F, BREEDING_INGREDIENT, false));
        this.goalSelector.add(6, new FleeEntityGoal<>(this, PlayerEntity.class, 4.0F, 0.8F, 1.0F));
        this.goalSelector.add(6, new FleeEntityGoal<>(this, WolfEntity.class, 8.0F, 1.6F, 1.6F));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, RabbitEntity.class, false));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, ChickenEntity.class, false));
    }

    @Override
    public CivetEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity) {
        return CaffeinatedEntities.CIVET.create(serverWorld);
    }

    public static boolean canSpawn(EntityType<CivetEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).isIn(CaffeinatedBlockTags.CIVETS_SPAWNABLE_ON) && isLightLevelValidForNaturalSpawn(world, pos);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    @Override
    public boolean canSpawn(WorldView world) {
        if (!world.doesNotIntersectEntities(this) || world.containsFluid(this.getBoundingBox())) {
            return false;
        }
        BlockPos blockPos = this.getBlockPos();
        if (blockPos.getY() < world.getSeaLevel()) {
            return false;
        }
        BlockState blockState = world.getBlockState(blockPos.down());
        return blockState.isIn(CaffeinatedBlockTags.CIVETS_SPAWNABLE_ON);
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
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        boolean bl = this.isBreedingItem(itemStack);
        ActionResult actionResult = super.interactMob(player, hand);
        if (actionResult.isAccepted() && bl) {
            this.getWorld().playSoundFromEntity(null, this, this.getEatSound(itemStack), SoundCategory.NEUTRAL, 1.0F, MathHelper.nextBetween(this.getWorld().getRandom(), 0.8F, 1.2F));
        }
        return actionResult;
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return super.computeFallDamage(fallDistance, damageMultiplier) - 5;
    }
}
