package me.mixces.statuseffects.mixin;

import net.minecraft.client.gui.screen.inventory.menu.InventoryMenuScreen;
import net.minecraft.client.gui.screen.inventory.menu.PlayerInventoryScreen;
import net.minecraft.inventory.menu.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventoryScreen.class)
public abstract class PlayerInventoryScreenMixin extends InventoryMenuScreen  {

	public PlayerInventoryScreenMixin(InventoryMenu menu) {
		super(menu);
	}

	@Inject(
		method = "checkStatusEffects",
		at = @At("HEAD"),
		cancellable = true)
	private void statusEffects$disableOffset(CallbackInfo ci) {
		ci.cancel();
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
		ci.cancel();
	}
}
