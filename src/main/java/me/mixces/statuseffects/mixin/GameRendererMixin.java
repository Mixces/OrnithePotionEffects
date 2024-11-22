package me.mixces.statuseffects.mixin;

import me.mixces.statuseffects.StatusEffects;
import me.mixces.statuseffects.config.Config;
import me.mixces.statuseffects.config.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

	@Shadow
	private Minecraft minecraft;

	@Inject(
		method = "render(FJ)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GameGui;render(F)V",
			shift = At.Shift.AFTER
		)
	)
	private void statusEffects$drawStatusEffects(float tickDelta, long startTime, CallbackInfo ci) {
		if (!minecraft.options.debugEnabled && !(minecraft.screen instanceof ConfigScreen) && Config.ENABLED.get()) {
			StatusEffects.drawStatusEffects(Config.HUD_X.get(), Config.HUD_Y.get(), false);
		}
	}
}
