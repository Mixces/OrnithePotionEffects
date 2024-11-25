package me.mixces.statuseffects.config;

public class HudPosition {

	public Alignment alignment;

	public HudPosition(int x, int y, int width, int height, int hudWidth, int hudHeight) {
		float rightX = x + hudWidth;
		float bottomY = y + hudHeight;
		float oneThirdWidth = width / 3f;
		float twoThirdWidth = width * 2f / 3f;
		float oneThirdHeight = height / 3f;
		float twoThirdHeight = height * 2f / 3f;

		if (x <= oneThirdWidth && y <= oneThirdHeight) {
			alignment = Alignment.TOP_LEFT;
		} else if (rightX >= twoThirdWidth && y <= oneThirdHeight) {
			alignment = Alignment.TOP_RIGHT;
		} else if (x <= oneThirdWidth && bottomY >= twoThirdHeight) {
			alignment = Alignment.BOTTOM_LEFT;
		} else if (rightX >= twoThirdWidth && bottomY >= twoThirdHeight) {
			alignment = Alignment.BOTTOM_RIGHT;
		} else if (y <= oneThirdHeight) {
			alignment = Alignment.TOP_CENTER;
		} else if (x <= oneThirdWidth) {
			alignment = Alignment.MIDDLE_LEFT;
		} else if (rightX >= twoThirdWidth) {
			alignment = Alignment.MIDDLE_RIGHT;
		} else if (bottomY >= twoThirdHeight) {
			alignment = Alignment.BOTTOM_CENTER;
		} else {
			alignment = Alignment.MIDDLE_CENTER;
		}
	}

	public enum Alignment {
		TOP_LEFT,
		TOP_CENTER,
		TOP_RIGHT,
		MIDDLE_LEFT,
		MIDDLE_CENTER,
		MIDDLE_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_CENTER,
		BOTTOM_RIGHT
	}
}
