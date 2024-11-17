package me.mixces.statuseffects.config;

import me.mixces.statuseffects.StatusEffects;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.ornithemc.osl.config.api.ConfigManager;

public class ConfigScreen extends Screen {

	private final Screen parentScreen;
	private boolean isDragging = false;
	private int dragOffsetX = 0;
	private int dragOffsetY = 0;

	public ConfigScreen(Screen parentScreen) {
		this.parentScreen = parentScreen;
	}

	@Override
	public void render(int mouseX, int mouseY, float tickDelta) {
		renderBackground();
		drawString(textRenderer, "Drag me!", Config.HUD_X.get(), Config.HUD_Y.get(), 0xFFFFFFFF);

		StatusEffects.drawStatusEffects(Config.HUD_X.get(), Config.HUD_Y.get());
		super.render(mouseX, mouseY, tickDelta);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton == 0 && isMouseOver(mouseX, mouseY)) {
			isDragging = true;
			dragOffsetX = mouseX - Config.HUD_X.get();
			dragOffsetY = mouseY - Config.HUD_Y.get();
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		super.mouseReleased(mouseX, mouseY, mouseButton);
		isDragging = false;
	}

	@Override
	protected void mouseDragged(int mouseX, int mouseY, int mouseButton, long duration) {
		super.mouseDragged(mouseX, mouseY, mouseButton, duration);
		if (isDragging) {
			Config.HUD_X.set(mouseX - dragOffsetX);
			Config.HUD_Y.set(mouseY - dragOffsetY);
		}
	}

	@Override
	protected void buttonClicked(ButtonWidget button) {
		super.buttonClicked(button);
		if (!button.active) {
			return;
		}
		if (button.id == 0) {
			Config.HUD_X.set(0);
			Config.HUD_Y.set(0);
		}
		if (button.id == 1) {
			ConfigManager.save(new Config());
			minecraft.openScreen(parentScreen);
		}
	}

	@Override
	public void init() {
		super.init();
		buttons.add(new ButtonWidget(0, width / 2 - 100, height - 51, "Reset Position"));
		buttons.add(new ButtonWidget(1, width / 2 - 100, height - 27, "Done"));
	}

	@Override
	public void removed() {
		super.removed();
		ConfigManager.save(new Config());
	}

	public boolean isMouseOver(int mouseX, int mouseY) {
		String text = "Drag Me!";
		int textWidth = textRenderer.getWidth(text);
		int textHeight = textRenderer.fontHeight;
		int x = Config.HUD_X.get();
		int y = Config.HUD_Y.get();

		//todo: fix height and width
		return mouseX >= x && mouseY >= y && mouseX < x + textWidth && mouseY < y + textHeight;
	}
}
