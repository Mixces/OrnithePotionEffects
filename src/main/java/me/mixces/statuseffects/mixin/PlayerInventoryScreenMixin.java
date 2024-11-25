package me.mixces.statuseffects.mixin;

import me.mixces.statuseffects.config.Config;
import net.minecraft.client.gui.screen.inventory.menu.PlayerInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventoryScreen.class)
public abstract class PlayerInventoryScreenMixin  {

	@Inject(
		method = "checkStatusEffects",
		at = @At("HEAD"),
		cancellable = true)
	private void statusEffects$disableOffset(CallbackInfo ci) {
		if (Config.ENABLED.get()) {
			ci.cancel();
		}
	}

	@Inject(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/screen/inventory/menu/InventoryMenuScreen;render(IIF)V",
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
	private void statusEffects$disableInventoryHUD(CallbackInfo ci) {
		if (Config.ENABLED.get()) {
			ci.cancel();
		}
	}
}
