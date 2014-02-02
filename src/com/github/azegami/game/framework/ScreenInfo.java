package com.github.azegami.game.framework;

public class ScreenInfo{
	@SuppressWarnings("unused")
	private int format, width, height;

	public ScreenInfo() {
		width = height = -1;
	}

	public void setScreenInfo(int format, int width, int height){
		this.width = width;
		this.height = height;

		this.format = format;
	}

	/**
	 * 画面幅を返す
	 * @return 画面の幅
	 */
	public int getWidth(){
		return width;
	}

	/**
	 * 画面高さを返す
	 * @return 画面の高さ
	 */
	public int getHeight(){
		return height;
	}
}