package com.chikoritalover.caffeinated;

import com.chikoritalover.caffeinated.registry.ModItems;
import com.chikoritalover.caffeinated.registry.ModSoundEvents;
import com.chikoritalover.caffeinated.registry.ModStatusEffects;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Caffeinated implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "caffeinated";
	public static final Logger LOGGER = LoggerFactory.getLogger("modid");

	@Override
	public void onInitialize() {
		ModItems.register();
		ModSoundEvents.register();
		ModStatusEffects.register();
	}
}
