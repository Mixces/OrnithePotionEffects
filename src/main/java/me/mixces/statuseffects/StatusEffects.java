package me.mixces.statuseffects;

import com.mojang.blaze3d.platform.GlStateManager;
import me.mixces.statuseffects.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.render.Window;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.living.effect.StatusEffect;
import net.minecraft.entity.living.effect.StatusEffectInstance;
import net.minecraft.resource.Identifier;
import net.ornithemc.osl.config.api.ConfigManager;

import net.ornithemc.osl.entrypoints.api.ModInitializer;

import java.util.Collection;

public class StatusEffects implements ModInitializer {

	private static final Identifier MENU_LOCATION = new Identifier("textures/gui/container/inventory.png");

	public static void drawStatusEffects(int hudX, int hudY) {
		final Minecraft minecraft = Minecraft.getInstance();

		/* cant have a null player */
		if (minecraft.player == null) {
			return;
		}

		final Window window = new Window(minecraft);
		final GuiElement gui = new GuiElement();

		final Collection<StatusEffectInstance> collection = minecraft.player.getStatusEffects();
		if (collection.isEmpty()) {
			/* why render anything when the collection is empty */
			return;
		}

		/* each icon is 18 x 18 pixels */
		final int iconPixels = 18;
		/* space between each potion entry */
		final int entrySpacing = 4;

		int x = hudX;
		int y = hudY + (window.getHeight() / 2) - ((iconPixels + (collection.size() > 1 ? entrySpacing : 0)) / 2) * (collection.size() + 1);

		GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.disableLighting();
		for (final StatusEffectInstance statusEffectInstance : collection) {
			final StatusEffect statusEffect = StatusEffect.BY_ID[statusEffectInstance.getId()];

			/* render icon */
			GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
			minecraft.getTextureManager().bind(MENU_LOCATION);
			if (statusEffect.hasIcon()) {
				final int index = statusEffect.getIconIndex();
				gui.drawTexture(x + 6, y + 7, index % 8 * iconPixels, 198 + index / 8 * iconPixels, iconPixels, iconPixels);
			}

			/* effect name */
			String name = I18n.translate(statusEffect.getTranslationKey());
			int amplifier = statusEffectInstance.getAmplifier();
			if (amplifier > 0 && amplifier < 4) {
				name += " " + I18n.translate("enchantment.level." + (amplifier + 1));
			}
			minecraft.textRenderer.drawWithShadow(name, x + 10 + iconPixels, y + 6, 0xFFFFFF);

			/* effect duration*/
			String duration = StatusEffect.getDurationString(statusEffectInstance);
			if (statusEffectInstance.getDuration() > 32147) {
				/* duration threshold taken from https://modrinth.com/mod/statuseffecttimer */
				duration = "**:**";
			}
			minecraft.textRenderer.drawWithShadow(duration, x + 10 + iconPixels, y + 6 + 10, 0x7F7F7F);

			/* spacing */
			y += iconPixels + entrySpacing;
		}
	}

	@Override
	public void init() {
		ConfigManager.register(new Config());
	}
}
