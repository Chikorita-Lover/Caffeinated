package com.chikoritalover.caffeinated.mixin;

import com.chikoritalover.caffeinated.world.CivetSpawner;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.spawner.Spawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyArg(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/level/ServerWorldProperties;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/world/dimension/DimensionOptions;Lnet/minecraft/server/WorldGenerationProgressListener;ZJLjava/util/List;ZLnet/minecraft/util/math/random/RandomSequencesState;)V"), index = 9)
    private List<Spawner> addCivetSpawner(List<Spawner> spawners) {
        if (spawners.isEmpty()) return spawners;

        ArrayList<Spawner> spawners2 = new ArrayList<>(spawners);
        spawners2.add(new CivetSpawner());
        return spawners2;
    }
}
