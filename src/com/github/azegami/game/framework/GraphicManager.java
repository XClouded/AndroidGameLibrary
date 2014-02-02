package com.github.azegami.game.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;

public abstract class GraphicManager extends Manager {
	/**
	 * ファイルの入力ストリームを出力
	 * @param path
	 * アセット、ファイル名ならそのままパスを<br>
	 * リソースはIDの文字列
	 * @param loadMode ファイル読み込みモード
	 * @return 入力ストリーム
	 */
	protected InputStream getInputStream(String path, LoadMode loadMode){
		InputStream is = null;

		try{
			switch (loadMode) {
			case ASSET:
				is = resources.getAssets().open(path);
				break;
			case FILE:
				is = new FileInputStream(new File(path));
				break;
			case RESOURCE:
				int id = Integer.valueOf(path);
				is = resources.openRawResource(id);
				break;
			default:
				break;
			}
		}catch(FileNotFoundException e){
		} catch (IOException e) {
		}

		return is;
	}

	protected Bitmap getBitmap(String path, LoadMode loadMode){
		Bitmap bitmap = null;
		InputStream is = getInputStream(path, loadMode);

		if(is != null){
			bitmap = BitmapFactory.decodeStream(is, null, options);
		}

		return bitmap;
	}

    protected static final BitmapFactory.Options options =
    		new BitmapFactory.Options();

    static {
    	// リソースの自動リサイズをしない
    	options.inScaled = false;
    	// 32bit画像として読み込む
    	options.inPreferredConfig = Config.ARGB_8888;
    }

	//abstract Image load(LoadMode loadMode, String file);
	public abstract Image loadFromAssets(String file);
	public abstract Image loadFromLocalFile(String file);
	public abstract void reset();

	/**
	 * 文字や矩形の色を設定
	 * @param color 色(ARGB)
	 */
	public abstract void setColor(int color);

	public abstract void drawRect(int left, int top, int right, int bottom);
	public abstract void drawRect(Rect rect);

	public abstract StringObject getStringObject(String str);

	public int getHeight(FontMetrics fm){
		return (int)(fm.descent - fm.ascent);
	}

	public int getDrawHeight(FontMetrics fm){
		return (int)Math.abs(fm.ascent);
	}

	private static final Rect stringRect = new Rect();

	protected Bitmap getStingBitmap(Paint paint, String str){
		String[] lineArrays = str.split("\n");

		int[] drawY = new int[lineArrays.length];
		int sumY = 0;

		FontMetrics fm = paint.getFontMetrics();

		int bitmapWidth = 0;
		for(int i = 0; i < lineArrays.length; i++){
			paint.getTextBounds(lineArrays[i], 0, lineArrays[i].length(), stringRect);

			int strWidth = (int) paint.measureText(lineArrays[i]);
			int strHeight = getHeight(fm);

			drawY[i] = sumY + getDrawHeight(fm);

			sumY+= strHeight;

			//文字の幅が作成する画像の幅より大きいなら更新
			if(bitmapWidth < strWidth)
				bitmapWidth = strWidth;
		}

		int bitmapHeight = sumY;

		Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, options.inPreferredConfig);
		Canvas canvas = new Canvas(bitmap);

		for(int i = 0; i < lineArrays.length; i++){
			canvas.drawText(lineArrays[i], 0, drawY[i], paint);
		}

		return bitmap;
	}

	public abstract Image getStringGraphicObject(String str);
}
