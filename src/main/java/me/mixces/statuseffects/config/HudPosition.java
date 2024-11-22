package me.mixces.statuseffects.config;

public class HudPosition {

	private RelativePosition position;
	public float x;
	public float y;

	public HudPosition(float x, float y, int width, int height, int hudWidth, int hudHeight) {
		setPosition(x, y, width, height, hudWidth, hudHeight);
	}

	public void setPosition(float x, float y, float screenWidth, float screenHeight, float hudWidth, float hudHeight) {
		float rightX = x + hudWidth;
		float bottomY = y + hudHeight;

		if (x <= screenWidth / 3f && y <= screenHeight / 3f) {
			position = RelativePosition.TOP_LEFT;
		} else if (rightX >= screenWidth / 3f * 2f && y <= screenHeight / 3f) {
			position = RelativePosition.TOP_RIGHT;
		} else if (x <= screenWidth / 3f && bottomY >= screenHeight / 3f * 2f) {
			position = RelativePosition.BOTTOM_LEFT;
		} else if (rightX >= screenWidth / 3f * 2f && bottomY >= screenHeight / 3f * 2f) {
			position = RelativePosition.BOTTOM_RIGHT;
		} else if (y <= screenHeight / 3f) {
			position = RelativePosition.TOP_CENTER;
		} else if (x <= screenWidth / 3f) {
			position = RelativePosition.MIDDLE_LEFT;
		} else if (rightX >= screenWidth / 3f * 2f) {
			position = RelativePosition.MIDDLE_RIGHT;
		} else if (bottomY >= screenHeight / 3f * 2f) {
			position = RelativePosition.BOTTOM_CENTER;
		} else {
			position = RelativePosition.MIDDLE_CENTER;
		}

		this.x = x + position.x * screenWidth / 3;
		this.y = y + position.y * screenHeight / 3;
	}

	public enum RelativePosition {
		TOP_LEFT(0f, 0f),
		TOP_CENTER(0.5f, 0f),
		TOP_RIGHT(1f, 0f),
		MIDDLE_LEFT(0f, 0.5f),
		MIDDLE_CENTER(0.5f, 0.5f),
		MIDDLE_RIGHT(1f, 0.5f),
		BOTTOM_LEFT(0f, 1f),
		BOTTOM_CENTER(0.5f, 1f),
		BOTTOM_RIGHT(1f, 1f);

		public final float x;
		public final float y;

		RelativePosition(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}
}
