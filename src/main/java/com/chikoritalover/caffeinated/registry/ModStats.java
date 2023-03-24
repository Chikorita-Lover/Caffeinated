package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStats {
    public static final Identifier COFFEE_TAKEN;

    public ModStats() {
    }

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = new Identifier(Caffeinated.MODID, id);
        Registry.register(Registry.CUSTOM_STAT, id, identifier);
        Stats.CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }

    static {
        COFFEE_TAKEN = register("coffee_taken", StatFormatter.DEFAULT);
    }

    public static void register() {}
}
