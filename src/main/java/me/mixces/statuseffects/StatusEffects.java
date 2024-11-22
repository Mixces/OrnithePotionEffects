package me.mixces.statuseffects;

import com.mojang.blaze3d.platform.GlStateManager;
import me.mixces.statuseffects.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.entity.living.effect.StatusEffectInstance;
import net.minecraft.resource.Identifier;
import net.ornithemc.osl.config.api.ConfigManager;

import net.ornithemc.osl.entrypoints.api.ModInitializer;

import java.util.ArrayList;
import java.util.Collection;

public class StatusEffects implements ModInitializer {

	private static final Identifier MENU_LOCATION = new Identifier("textures/gui/container/inventory.png");
	/* each icon is 18 x 18 pixels */
	private static final int iconPixels = 18;
	/* space between each potion entry */
	private static final int entrySpacing = 4;

	public static void drawStatusEffects(int hudX, int hudY, boolean editing) {
		final Minecraft minecraft = Minecraft.getInstance();
		final GuiElement gui = new GuiElement();

		Collection<StatusEffectInstance> collection = new ArrayList<>();

		if (!editing && minecraft.player != null) {
			collection = minecraft.player.getStatusEffects();
		} else {
			collection.add(new StatusEffectInstance(StatusEffect.SPEED.getId(), 1200, 1));
			collection.add(new StatusEffectInstance(StatusEffect.STRENGTH.getId(), 30, 3));
		}

		if (collection.isEmpty()) {
			/* why render anything when the collection is empty */
			return;
		}

		int x = hudX;
		int y = hudY - ((iconPixels + (collection.size() > 1 ? entrySpacing : 0)) / 2) * (collection.size());

		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.disableLighting();
		for (final StatusEffectInstance statusEffectInstance : collection) {
			final StatusEffect statusEffect = StatusEffect.BY_ID[statusEffectInstance.getId()];

			/* render icon */
			GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
			minecraft.getTextureManager().bind(MENU_LOCATION);
			if (statusEffect.hasIcon()) {
				final int index = statusEffect.getIconIndex();
				gui.drawTexture(x, y + 7, index % 8 * iconPixels, 198 + index / 8 * iconPixels, iconPixels, iconPixels);
			}

			/* effect name */
			String name = I18n.translate(statusEffect.getTranslationKey());
			int amplifier = statusEffectInstance.getAmplifier();
			if (amplifier > 0 && amplifier < 4) {
				name += " " + I18n.translate("enchantment.level." + (amplifier + 1));
			}
			minecraft.textRenderer.drawWithShadow(name, x - 4 - minecraft.textRenderer.getWidth(name), y + 6, 0xFFFFFF);

			/* effect duration*/
			String duration = StatusEffect.getDurationString(statusEffectInstance);
			if (statusEffectInstance.getDuration() > 32147) {
				/* duration threshold taken from https://modrinth.com/mod/statuseffecttimer */
				duration = "**:**";
			}
			minecraft.textRenderer.drawWithShadow(duration, x - 4 - minecraft.textRenderer.getWidth(duration), y + 6 + 10, 0x7F7F7F);

			/* spacing */
			y += iconPixels + entrySpacing;
		}
	}

	@Override
	public void init() {
		ConfigManager.register(new Config());
	}
}
