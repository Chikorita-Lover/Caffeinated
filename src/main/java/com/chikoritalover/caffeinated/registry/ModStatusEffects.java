package com.chikoritalover.caffeinated.registry;

import com.chikoritalover.caffeinated.Caffeinated;
import com.chikoritalover.caffeinated.entity.effect.CaffeineStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModStatusEffects {
    public static final StatusEffect CAFFEINE = new CaffeineStatusEffect(StatusEffectCategory.BENEFICIAL, 0x673521);

    public static void register() {
        Registry.register(Registry.STATUS_EFFECT, new Identifier(Caffeinated.MODID, "caffeine"), CAFFEINE);
    }
}
