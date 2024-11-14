package me.mixces.statuseffects.mixin;

import me.mixces.statuseffects.StatusEffectsHUD;
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
		if (!minecraft.options.debugEnabled) {
			StatusEffectsHUD hud = new StatusEffectsHUD();
			hud.drawStatusEffects();
		}
	}
}
