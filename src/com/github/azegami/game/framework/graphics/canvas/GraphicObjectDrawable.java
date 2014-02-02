package com.github.azegami.game.framework.graphics.canvas;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.github.azegami.game.framework.GraphicObject;
import com.github.azegami.game.framework.LoadMode;


public class GraphicObjectDrawable extends GraphicObject{
	private BitmapDrawable drawable;
	private boolean using;		//使っているか？
	private boolean released;//解放済みか？
	protected int width, height;	//画像の幅と高さ

	public GraphicObjectDrawable(int hashId, String filepath) {
		super(hashId, filepath);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public BitmapDrawable getDrawable() {
		if(released) return null;

		return drawable;
	}

	void attach(Resources resources, Bitmap bitmap){
		//解放されてなかったら解放
		if(!released){
			relese();
		}

		drawable = new BitmapDrawable(resources, bitmap);

		width = bitmap.getWidth();
		height = bitmap.getHeight();

		logger.debug(
				String.format("ファイル読み込み ID %d ファイルパス %s 幅 %d 高さ %d", hashId, filepath, width, height)
		);

		using = true;
		released = false;
	}

	public void relese(){
		//ユーザのビットマップ以外なら
		if(loadMode != LoadMode.NOLOAD && drawable != null){
			Bitmap bitmap = drawable.getBitmap();

			//リサイクルしてないなら
			if(!bitmap.isRecycled())
				bitmap.recycle();

			drawable.setCallback(null);

			logger.debug("グラフィック解放 id = " + hashId);

			drawable = null;
			released = true;
		}
	}

	//releaseからキャッシュも消える
	//完全削除させる
	public void deleteListOn(){
		using = false;
		//relese();
	}

	boolean getUsing(){
		return using;
	}
}

