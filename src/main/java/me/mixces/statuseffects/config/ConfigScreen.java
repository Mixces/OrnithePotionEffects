package me.mixces.statuseffects.config;

import me.mixces.statuseffects.hud.StatusEffectsHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

public class ConfigScreen extends Screen {

	private final StatusEffectsHud hud;
	private final Screen parentScreen;
	/* hud editor properties */
	private final int hudWidth = 79;
	private final int hudHeight = 40;
	private final int hudBound = 4;
	/* mouse movement */
	private boolean isDragging = false;
	private int dragOffsetX;
	private int dragOffsetY;

	public ConfigScreen(Screen parentScreen) {
		this.parentScreen = parentScreen;
		this.hud = new StatusEffectsHud();
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

		if (Config.ENABLED.get()) {
			int x = Config.HUD_X.get();
			int y = Config.HUD_Y.get();

			if (isMouseOver(mouseX, mouseY)) {
				drawSelectionBox(x, y);
			}
			drawOutlineBox(x, y);
			hud.drawStatusEffects(x, y, true);
		}

		if (!isDragging) {
			drawCenteredString(textRenderer, "Status Effects", width / 2, 15, 0xFFFFFF);
			super.render(mouseX, mouseY, tickDelta);
		}
	}

	@Override
	public void init() {
		super.init();
		buttons.add(new ButtonWidget(0, width / 2 - 75, height - 75, 150, 20, Config.getToggleState()));
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
				button.message = Config.getToggleState();
				break;
			case 1:
				Config.HUD_X.set(4 /* entry space */);
				Config.HUD_Y.set(height / 2);
				break;
			case 2:
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
	}

	private void drawGridlines() {
		drawVerticalLine(width / 3, 0, height, 0x80FFFFFF);
		drawVerticalLine(width * 2 / 3, 0, height, 0x80FFFFFF);
		drawHorizontalLine(0, width, height / 3, 0x80FFFFFF);
		drawHorizontalLine(0, width, height * 2 / 3, 0x80FFFFFF);
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		int x = Config.HUD_X.get();
		int y = Config.HUD_Y.get() - hudHeight / 2;
		return mouseX >= x - hudBound && mouseY >= y - hudBound && mouseX < x + hudBound + hudWidth && mouseY < y + hudBound + hudHeight;
	}

	private void drawSelectionBox(int x, int y) {
		fill(x - hudBound, y - hudHeight / 2 - hudBound, x + hudWidth + hudBound, y + hudHeight / 2 + hudBound, 0x40FFFFFF);
	}

	private void drawOutlineBox(int x, int y) {
		drawHorizontalLine(x - hudBound, x + hudWidth + hudBound, y - hudHeight / 2 - hudBound, 0x80FFFFFF);
		drawVerticalLine(x - hudBound, y - hudHeight / 2 - hudBound, y + hudHeight / 2 + hudBound, 0x80FFFFFF);
		drawHorizontalLine(x - hudBound, x + hudWidth + hudBound, y + hudHeight / 2 + hudBound, 0x80FFFFFF);
		drawVerticalLine(x + hudWidth + hudBound, y - hudHeight / 2 - hudBound, y + hudHeight / 2 + hudBound, 0x80FFFFFF);
	}
}
