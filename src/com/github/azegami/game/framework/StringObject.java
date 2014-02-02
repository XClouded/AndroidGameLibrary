package com.github.azegami.game.framework;

import android.graphics.Paint.FontMetrics;

public abstract class StringObject{
	//画像の幅、高さ
	protected int bitmapWidth, bitmapHeight;

	public int getWidth() {
		return bitmapWidth;
	}

	public int getHeight(){
		return bitmapHeight;
	}

	public int getHeight(FontMetrics fm){
		return (int)(fm.descent - fm.ascent);
	}

	public int getDrawHeight(FontMetrics fm){
		return (int)Math.abs(fm.ascent);
	}

	/**
	 * 文字列を左上（x,y)を原点に描く
	 * @param str
	 * @param x
	 * @param y
	 */
	public abstract void draw(int x, int y);
}