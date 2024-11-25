package me.mixces.statuseffects.hud;

import com.mojang.blaze3d.platform.GlStateManager;
import me.mixces.statuseffects.config.HudPosition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.render.Window;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.entity.living.effect.StatusEffectInstance;
import net.minecraft.resource.Identifier;

import java.util.ArrayList;
import java.util.Collection;

public class StatusEffectsHud {

	private final Identifier MENU_LOCATION = new Identifier("textures/gui/container/inventory.png");
	private final Minecraft minecraft;
	private final GuiElement guiElement;
	/* each icon is 18 x 18 pixels */
	private final int iconPixels = 18;
	/* space between each potion entry */
	private final int entrySpacing = 4;

	private Collection<StatusEffectInstance> collection = new ArrayList<>();

	public StatusEffectsHud() {
		minecraft = Minecraft.getInstance();
		guiElement = new GuiElement();
	}

	public void drawStatusEffects(int x, int y, boolean editing) {
		Window window = new Window(minecraft);
		HudPosition position = new HudPosition(x, y, window.getWidth(), window.getHeight(), 79, 40);

		if (!editing && minecraft.player != null) {
			collection = minecraft.player.getStatusEffects();
		} else {
			if (collection.isEmpty()) {
				collection.add(new StatusEffectInstance(StatusEffect.SPEED.getId(), 1200, 1));
				collection.add(new StatusEffectInstance(StatusEffect.STRENGTH.getId(), 30, 3));
			}
		}
		if (collection.isEmpty()) {
			/* why attempt to render anything when the collection is empty */
			return;
		}

		y -= getVerticalAlignment(position);

		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.disableLighting();
		for (StatusEffectInstance statusEffectInstance : collection) {
			StatusEffect statusEffect = StatusEffect.BY_ID[statusEffectInstance.getId()];
			/* render icon */
			drawIcon(statusEffect, position, x, y);
			/* effect name */
			drawName(statusEffectInstance, statusEffect, position, x, y);
			/* effect duration*/
			drawDuration(statusEffectInstance, statusEffect, position, x, y);
			/* spacing */
			y += iconPixels + entrySpacing;
		}
	}

	private void drawIcon(StatusEffect statusEffect, HudPosition position, int x, int y) {
		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		minecraft.getTextureManager().bind(MENU_LOCATION);
		if (statusEffect.hasIcon()) {
			int index = statusEffect.getIconIndex();
			guiElement.drawTexture(getHorizontalAlignment(true, position, x, null), y + 7, index % 8 * iconPixels, 198 + index / 8 * iconPixels, iconPixels, iconPixels);
		}
	}

	private void drawName(StatusEffectInstance statusEffectInstance, StatusEffect statusEffect, HudPosition position, int x, int y) {
		String name = I18n.translate(statusEffect.getTranslationKey());
		int amplifier = statusEffectInstance.getAmplifier();
		if (amplifier > 0 && amplifier < 4) {
			name += " " + I18n.translate("enchantment.level." + (amplifier + 1));
		}
		minecraft.textRenderer.drawWithShadow(name, getHorizontalAlignment(false, position, x, name), y + 6, 0xFFFFFF);
	}

	private void drawDuration(StatusEffectInstance statusEffectInstance, StatusEffect statusEffect, HudPosition position, int x, int y) {
		String duration = StatusEffect.getDurationString(statusEffectInstance);
		if (statusEffectInstance.getDuration() > 32147) {
			/* duration threshold taken from https://modrinth.com/mod/statuseffecttimer */
			duration = "**:**";
		}
		minecraft.textRenderer.drawWithShadow(duration, getHorizontalAlignment(false, position, x, duration), y + 6 + 10, 0x7F7F7F);
	}

	private int getHorizontalAlignment(boolean isIcon, HudPosition position, int x, String text) {
		if (isIcon) {
			switch (position.alignment) {
				case TOP_RIGHT:
				case MIDDLE_RIGHT:
				case BOTTOM_RIGHT:
					return x + 62;
				case TOP_CENTER:
				case BOTTOM_CENTER:
				case MIDDLE_CENTER:
				case TOP_LEFT:
				case MIDDLE_LEFT:
				case BOTTOM_LEFT:
				default:
					return x;
			}
		}
		switch (position.alignment) {
			case TOP_RIGHT:
			case MIDDLE_RIGHT:
			case BOTTOM_RIGHT:
				return x - entrySpacing - minecraft.textRenderer.getWidth(text) + 62;
			case TOP_CENTER:
			case BOTTOM_CENTER:
			case MIDDLE_CENTER:
			case TOP_LEFT:
			case MIDDLE_LEFT:
			case BOTTOM_LEFT:
			default:
				return x + entrySpacing + iconPixels;
		}
	}

	private int getVerticalAlignment(HudPosition position) {
		int totalHeight = collection.size() * (iconPixels + entrySpacing);
		switch (position.alignment) {
			case TOP_LEFT:
			case TOP_CENTER:
			case TOP_RIGHT:
				return iconPixels + entrySpacing * 2;
			case BOTTOM_LEFT:
			case BOTTOM_CENTER:
			case BOTTOM_RIGHT:
				return totalHeight - iconPixels;
			case MIDDLE_LEFT:
			case MIDDLE_CENTER:
			case MIDDLE_RIGHT:
			default:
				return totalHeight / 2 + entrySpacing;
		}
	}
}
