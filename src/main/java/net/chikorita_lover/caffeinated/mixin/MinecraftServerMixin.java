package net.chikorita_lover.caffeinated.mixin;

import com.google.common.collect.ImmutableList;
import net.chikorita_lover.caffeinated.world.CivetSpawner;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.spawner.SpecialSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @ModifyArg(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/level/ServerWorldProperties;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/world/dimension/DimensionOptions;Lnet/minecraft/server/WorldGenerationProgressListener;ZJLjava/util/List;ZLnet/minecraft/util/math/random/RandomSequencesState;)V"), index = 9)
    private List<SpecialSpawner> addCivetSpawner(List<SpecialSpawner> list) {
        if (list.isEmpty()) {
            return list;
        }
        ImmutableList.Builder<SpecialSpawner> spawners = ImmutableList.<SpecialSpawner>builder().addAll(list);
        spawners.add(new CivetSpawner());
        return spawners.build();
    }
}
