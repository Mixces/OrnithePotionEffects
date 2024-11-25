package me.mixces.statuseffects.mixin;

import me.mixces.statuseffects.config.Config;
import me.mixces.statuseffects.config.ConfigScreen;
import me.mixces.statuseffects.hud.StatusEffectsHud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.resource.manager.ResourceManager;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Shadow
	private Minecraft minecraft;

	@Unique
	private StatusEffectsHud statusEffects$hud;

	@Inject(
		method = "<init>",
		at = @At(
			value = "TAIL"
		)
	)
	private void statusEffects$initStatusEffectsHud(Minecraft minecraft, ResourceManager resourceManager, CallbackInfo ci) {
		statusEffects$hud = new StatusEffectsHud();
	}

	@Inject(
		method = "render(FJ)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GameGui;render(F)V",
			shift = At.Shift.AFTER
		)
	)
	private void statusEffects$drawStatusEffects(float tickDelta, long startTime, CallbackInfo ci) {
		if (Config.ENABLED.get() && !minecraft.options.debugEnabled && !(minecraft.screen instanceof ConfigScreen)) {
			statusEffects$hud.drawStatusEffects(Config.HUD_X.get(), Config.HUD_Y.get(), false);
		}
	}
}
