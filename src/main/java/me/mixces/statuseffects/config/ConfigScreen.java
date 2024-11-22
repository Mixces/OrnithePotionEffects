package me.mixces.statuseffects.config;

import me.mixces.statuseffects.StatusEffects;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Formatting;
import net.ornithemc.osl.config.api.ConfigManager;

public class ConfigScreen extends Screen {

	private final Screen parentScreen;
	private int hudWidth;
	private int hudHeight;
	private boolean isDragging = false;
	private int dragOffsetX;
	private int dragOffsetY;

	public ConfigScreen(Screen parentScreen) {
		this.parentScreen = parentScreen;
	}

	@Override
	public void render(int mouseX, int mouseY, float tickDelta) {
		renderBackground();
		if (isDragging) {
			drawGridlines();
			Config.HUD_X.set(Config.HUD_X.get() + (mouseX - dragOffsetX));
			Config.HUD_Y.set(Config.HUD_Y.get() + (mouseY - dragOffsetY));
		}
		dragOffsetX = mouseX;
		dragOffsetY = mouseY;

		int x = Config.HUD_X.get();
		int y = Config.HUD_Y.get();

		drawOutlineBox(x, y, hudWidth, hudHeight);
		StatusEffects.drawStatusEffects(x,y, true);

		if (!isDragging) {
			super.render(mouseX, mouseY, tickDelta);
		}
	}

	@Override
	public void init() {
		super.init();
		hudWidth = 80;
		hudHeight = 50;

		buttons.add(new ButtonWidget(0, width / 2 - 75, height - 75, 150, 20, getToggleState()));
		buttons.add(new ButtonWidget(1, width / 2 - 75, height - 51, 150, 20, "Reset Position"));
		buttons.add(new ButtonWidget(2, width / 2 - 75, height - 27, 150, 20, "Done"));
	}

	@Override
	protected void buttonClicked(ButtonWidget button) {
		super.buttonClicked(button);
		if (!button.active) {
			return;
		}
		switch (button.id) {
			case 0:
				Config.ENABLED.set(!Config.ENABLED.get());
				button.message = getToggleState();
				break;
			case 1:
				Config.HUD_X.set(0);
				Config.HUD_Y.set(height / 2 - 18);
				break;
			case 2:
				ConfigManager.save(new Config());
				minecraft.openScreen(parentScreen);
				break;
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (!isDragging && mouseButton == 0 && isMouseOver(mouseX, mouseY)) {
			isDragging = true;
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		super.mouseReleased(mouseX, mouseY, mouseButton);
		isDragging = false;
	}

	@Override
	public void removed() {
		super.removed();
		ConfigManager.save(new Config());
	}

	private void drawGridlines() {
		drawVerticalLine(width / 3, 0, height, 0x80FFFFFF);
		drawVerticalLine(width * 2 / 3, 0, height, 0x80FFFFFF);
		drawHorizontalLine(0, width, height / 3, 0x80FFFFFF);
		drawHorizontalLine(0, width, height * 2 / 3, 0x80FFFFFF);
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		int offset = 4;
		int x = Config.HUD_X.get();
		int y = Config.HUD_Y.get() - hudHeight / 2;
		return mouseX >= x - offset && mouseY >= y - offset && mouseX < x + offset + hudWidth && mouseY < y + offset + hudHeight;
	}

	private void drawOutlineBox(int x, int y, int width, int height) {
		int offset = 4;
		drawHorizontalLine(x - offset, x + width + offset, y - height / 2 - offset, 0x80FFFFFF);
		drawVerticalLine(x - offset, y - height / 2 - offset, y + height / 2 + offset, 0x80FFFFFF);
		drawHorizontalLine(x - offset, x + width + offset, y + height / 2 + offset, 0x80FFFFFF);
		drawVerticalLine(x + width + offset, y - height / 2 - offset, y + height / 2 + offset, 0x80FFFFFF);
	}

	private String getToggleState() {
		return "Mod: " + (Config.ENABLED.get() ? Formatting.GREEN + "Enabled" : Formatting.RED + "Disabled");
	}
}
