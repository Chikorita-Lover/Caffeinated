package com.chikoritalover.caffeinated.world;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.entity.CivetEntity;
import com.chikoritalover.caffeinated.registry.CaffeinatedBiomeTags;
import com.chikoritalover.caffeinated.registry.CaffeinatedEntities;
import net.minecraft.SharedConstants;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.poi.PointOfInterestStorage;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.spawner.Spawner;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CivetSpawner implements Spawner {
    private static final int SPAWN_INTERVAL = 1200;
    private static final TagKey<Structure> CIVETS_SPAWN_IN = TagKey.of(RegistryKeys.STRUCTURE, new Identifier(Caffeinated.MODID, "civets_spawn_in"));
    private int cooldown;

    private static BlockPos getRandomSpawnPos(ServerWorld world, ServerPlayerEntity playerEntity) {
        Random random = world.getRandom();
        int posX = playerEntity.getBlockX() + getRandomPosOffset(random);
        int posZ = playerEntity.getBlockZ() + getRandomPosOffset(random);
        return new BlockPos(posX, world.getTopY(Heightmap.Type.WORLD_SURFACE, posX, posZ), posZ);
    }

    private static int getRandomPosOffset(Random random) {
        return MathHelper.nextInt(random, 8, 32) * (random.nextBoolean() ? -1 : 1);
    }

    /**
     * Tries to spawn civets in villages.
     *
     * @return the number of civets spawned
     * @implNote Civets spawn when there are more than two occupied beds and less than four existing civets.
     */
    private static int spawnInVillage(ServerWorld world, BlockPos pos) {
        final int radius = 48;
        final Predicate<RegistryEntry<PointOfInterestType>> entryPredicate = entry -> entry.isIn(PointOfInterestTypeTags.VILLAGE);

        if (world.getPointOfInterestStorage().count(entryPredicate, pos, radius, PointOfInterestStorage.OccupationStatus.IS_OCCUPIED) > 2L) {
            Optional<BlockPos> optional = world.getPointOfInterestStorage().getNearestPosition(entryPredicate, pos, radius, PointOfInterestStorage.OccupationStatus.IS_OCCUPIED);
            assert optional.isPresent();

            int posX = optional.get().getX() + world.getRandom().nextInt(9) - 4;
            int posZ = optional.get().getZ() + world.getRandom().nextInt(9) - 4;
            BlockPos blockPos = new BlockPos(posX, world.getTopY(Heightmap.Type.WORLD_SURFACE, posX, posZ), posZ);

            if (world.getNonSpectatingEntities(PlayerEntity.class, new Box(blockPos).expand(8.0, 2.0, 8.0)).isEmpty() && world.getNonSpectatingEntities(CivetEntity.class, new Box(blockPos).expand(radius, 8.0, radius)).size() < 4) {
                return spawn(blockPos, world);
            }
        }
        return 0;
    }

    /**
     * Tries to spawn civets inside structures.
     *
     * @return the number of civets spawned
     * @implNote Civets spawn when there are no civets in a 4-block radius.
     */
    private static int spawnInStructure(ServerWorld world, BlockPos pos) {
        BlockPos.Mutable mutablePos = pos.down().mutableCopy();
        do {
            mutablePos.move(Direction.DOWN);
            if (mutablePos.getY() == world.getBottomY()) {
                return 0;
            }
        } while (world.getBlockState(mutablePos).isOpaque() || !world.getBlockState(mutablePos.down()).isOpaque());
        List<CivetEntity> list = world.getNonSpectatingEntities(CivetEntity.class, new Box(mutablePos).expand(16.0, 8.0, 16.0));
        return list.isEmpty() ? spawn(mutablePos, world) : 0;
    }

    /**
     * Spawns a civet.
     *
     * @return the number of civets spawned
     */
    private static int spawn(BlockPos pos, ServerWorld world) {
        if (!SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, pos, CaffeinatedEntities.CIVET)) {
            return 0;
        }
        CivetEntity civetEntity = CaffeinatedEntities.CIVET.create(world);
        if (civetEntity == null) {
            return 0;
        }
        civetEntity.initialize(world, world.getLocalDifficulty(pos), SpawnReason.NATURAL, null, null);
        civetEntity.refreshPositionAndAngles(pos, 0.0F, 0.0F);
        if (!SharedConstants.isDevelopment)
            civetEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 72000));
        world.spawnEntityAndPassengers(civetEntity);
        return 1;
    }

    @Override
    public int spawn(ServerWorld world, boolean spawnMonsters, boolean spawnAnimals) {
        if (!spawnAnimals || !world.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
            return 0;
        }
        --this.cooldown;
        if (this.cooldown > 0) {
            return 0;
        }
        this.cooldown = SPAWN_INTERVAL;
        ServerPlayerEntity playerEntity = world.getRandomAlivePlayer();
        if (playerEntity == null) {
            return 0;
        }
        BlockPos blockPos = getRandomSpawnPos(world, playerEntity);
        int k = 10;
        if (!world.isRegionLoaded(blockPos.getX() - k, blockPos.getZ() - k, blockPos.getX() + k, blockPos.getZ() + k)) {
            return 0;
        }
        if (true) { // SpawnHelper.canSpawn(SpawnRestriction.Location.ON_GROUND, world, blockPos, CaffeinatedEntities.CIVET)) {
            if (world.isNearOccupiedPointOfInterest(blockPos, 2) && world.getBiome(blockPos).isIn(CaffeinatedBiomeTags.SPAWNS_CIVETS) && world.isNight()) {
                return spawnInVillage(world, blockPos);
            }
            if (world.getStructureAccessor().getStructureContaining(blockPos, CIVETS_SPAWN_IN).hasChildren()) {
                return spawnInStructure(world, blockPos);
            }
        }
        return 0;
    }
}
