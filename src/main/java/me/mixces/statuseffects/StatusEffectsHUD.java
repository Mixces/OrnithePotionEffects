package me.mixces.statuseffects;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.render.Window;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.entity.living.effect.StatusEffectInstance;
import net.minecraft.resource.Identifier;

import java.util.Collection;

public class StatusEffectsHUD {

	private final Identifier MENU_LOCATION = new Identifier("textures/gui/container/inventory.png");

	public void drawStatusEffects() {
		Minecraft minecraft = Minecraft.getInstance();
		Window window = new Window(minecraft);
		GuiElement gui = new GuiElement();
		Collection<StatusEffectInstance> collection = minecraft.player.getStatusEffects();
		if (collection.isEmpty()) {
			return;
		}
		int iconPixels = 18;
		int entrySpacing = 4;
		int hudX = 0;
		int hudY = (window.getHeight() / 2) - ((iconPixels + (collection.size() > 1 ? entrySpacing : 0)) / 2) * (collection.size() + 1);
		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.disableLighting();
		int space = iconPixels + entrySpacing;
		for (StatusEffectInstance statusEffectInstance : collection) {
			StatusEffect statusEffect = StatusEffect.BY_ID[statusEffectInstance.getId()];
			GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
			minecraft.getTextureManager().bind(MENU_LOCATION);
			if (statusEffect.hasIcon()) {
				int index = statusEffect.getIconIndex();
				gui.drawTexture(hudX + 6, hudY + 7, index % 8 * iconPixels, 198 + index / 8 * iconPixels, iconPixels, iconPixels);
			}
			String string = I18n.translate(statusEffect.getTranslationKey());
			if (statusEffectInstance.getAmplifier() == 1) {
				string = string + " " + I18n.translate("enchantment.level.2");
			} else if (statusEffectInstance.getAmplifier() == 2) {
				string = string + " " + I18n.translate("enchantment.level.3");
			} else if (statusEffectInstance.getAmplifier() == 3) {
				string = string + " " + I18n.translate("enchantment.level.4");
			}
			minecraft.textRenderer.drawWithShadow(string, hudX + 10 + iconPixels, hudY + 6, 0xFFFFFF);
			String string2 = StatusEffect.getDurationString(statusEffectInstance);
			minecraft.textRenderer.drawWithShadow(string2, hudX + 10 + iconPixels, hudY + 6 + 10, 0x7F7F7F);
			hudY += space;
		}
	}
}
