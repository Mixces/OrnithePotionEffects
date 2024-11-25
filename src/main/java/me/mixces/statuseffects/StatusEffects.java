package me.mixces.statuseffects;

import me.mixces.statuseffects.config.Config;
import net.ornithemc.osl.config.api.ConfigManager;

import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class StatusEffects implements ModInitializer {

	@Override
	public void init() {
		ConfigManager.register(new Config());
	}
}
