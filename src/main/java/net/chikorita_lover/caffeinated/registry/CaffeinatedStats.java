package net.chikorita_lover.caffeinated.registry;

import net.chikorita_lover.caffeinated.Caffeinated;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class CaffeinatedStats {
    public static final Identifier COFFEE_TAKEN = register("coffee_taken", StatFormatter.DEFAULT);

    private static Identifier register(String path, StatFormatter formatter) {
        Identifier id = Caffeinated.of(path);
        Stats.CUSTOM.getOrCreateStat(Registry.register(Registries.CUSTOM_STAT, id, id), formatter);
        return id;
    }

    public static void register() {
    }
}
