package com.github.azegami.game.framework.graphics.canvas;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.github.azegami.game.framework.Image;


public class ImageDrawable implements Image{
	public int x, y;
	private float rate;
	private float alpha;

	private GraphicObjectDrawable obj;

	private static Canvas c;

	public static void setCanvas(Canvas canvas) {
		ImageDrawable.c = canvas;
	}

	public ImageDrawable() {
		x = y = 0;
		alpha = 1.0f;
	}

	void setObj(GraphicObjectDrawable obj) {
		this.obj = obj;
	}

	BitmapDrawable getDrawable(){
		return obj.getDrawable();
	}

	/*
	 * Drawableの互換用
	 * *************************************
	 */
	public void setBounds(int left, int top, int right, int bottom) {
		Drawable drawable = getDrawable();
		if(drawable == null) return;

		drawable.setBounds(left, top, right, bottom);
	}

	public void setBounds(Rect image_r) {
		setBounds(image_r.left, image_r.top, image_r.right, image_r.bottom);
	}

	public void draw() {
		Drawable drawable = getDrawable();
		if(drawable == null) return;

		drawable.draw(c);
	}

	public Rect getBounds(){
		Drawable drawable = getDrawable();
		if(drawable == null) return null;

		return drawable.getBounds();
	}
	/*
	 * *************************************
	 */

	public void setAlpha(int alpha) {
		Drawable drawable = getDrawable();
		if(drawable == null) return;

		drawable.setAlpha(alpha);
	}

	public void setAlpha(double alpha) {
		if(getDrawable() == null) return;

		alpha = alpha > 1.0 ? 1.0 : alpha;
		alpha = alpha < 0.0 ? 0.0 : alpha;

		setAlpha((int)(255 * alpha));
	}

	public void draw(int left, int top){
		int right = left + obj.width;
		int bottom = top + obj.height;

		drawBounds(left, top, right, bottom);
	}

	public void draw(int left, int top, int width, int height){
		drawBounds(left, top, left + width, top + height);
	}

	public void drawBounds(int left, int top, int right, int bottom){
		int center_x = (left + right) / 2;
		int center_y = (top + bottom) / 2;

		draw(left, top, right, bottom, center_x, center_y);
	}

	public void draw(Rect rect){
		drawBounds(rect.left, rect.top, rect.right, rect.bottom);
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	private void draw(int left, int top, int right, int bottom, int center_x, int center_y){
		Drawable drawable = getDrawable();
		if(drawable == null) return;

		c.save();

		c.rotate(rate, center_x, center_y);

		if(alpha != 1.0f){
			drawable.setAlpha((int)(255 * alpha));
		}

		drawable.setBounds(left, top, right, bottom);
		drawable.draw(c);

		if(alpha != 1.0f){
			drawable.setAlpha(255);
		}

		c.restore();
	}

	@Override
	public int getWidth() {
		return obj.width;
	}

	@Override
	public int getHeight() {
		return obj.height;
	}
}
