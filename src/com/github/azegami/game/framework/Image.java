package com.github.azegami.game.framework;

import android.graphics.Rect;

public interface Image {
	public void setAlpha(int alpha);
	public void setAlpha(double alpha);
	public void draw(int left, int top);
	public void draw(int left, int top, int width, int height);
	public void drawBounds(int left, int top, int right, int bottom);
	public void draw(Rect rect);
	public void draw();

	public Rect getBounds();
	public int getWidth();
	public int getHeight();
}
