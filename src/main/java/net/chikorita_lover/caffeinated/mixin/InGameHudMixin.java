package net.chikorita_lover.caffeinated.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.chikorita_lover.caffeinated.registry.CaffeinatedStatusEffects;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow private int ticks;

    @ModifyVariable(method = "renderFood", at = @At("STORE"), ordinal = 4)
    private int offsetCaffeineHunger(int k, @Local(argsOnly = true) PlayerEntity player, @Local(ordinal = 3) int j) {
        if (player.hasStatusEffect(CaffeinatedStatusEffects.CAFFEINE) && j == this.ticks % MathHelper.ceil(50.0F)) {
            k -= 2;
        }
        return k;
    }
}
