package com.chikoritalover.caffeinated;

import com.chikoritalover.caffeinated.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caffeinated implements ModInitializer {
	public static final String MODID = "caffeinated";
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	@Override
	public void onInitialize() {
		ModBannerPatterns.initAndGetDefault(Registry.BANNER_PATTERN);
		ModBlocks.register();
		ModBlocks.registerFlammableBlocks();
		ModCauldronBehavior.register();
		ModItems.register();
		ModItems.registerCompostingChances();
		ModParticleTypes.register();
		ModPlacedFeatures.register();
		ModSoundEvents.register();
		ModStatusEffects.register();
		ModTradeOffers.register();
		ModBiomes.init();
	}
}
