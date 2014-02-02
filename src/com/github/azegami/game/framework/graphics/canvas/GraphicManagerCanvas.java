package com.github.azegami.game.framework.graphics.canvas;

import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;

import com.github.azegami.game.framework.GraphicManager;
import com.github.azegami.game.framework.Image;
import com.github.azegami.game.framework.LoadMode;
import com.github.azegami.game.framework.StringObject;
import com.github.azegami.game.framework.Logger.Logger;
import com.github.azegami.game.framework.Logger.LoggerManager;

public class GraphicManagerCanvas extends GraphicManager {
	private final HashMap<Integer, GraphicObjectDrawable> map;
	private Logger logger = LoggerManager.getFrameworkLogger("GraphicManagerCanvas");

	@SuppressLint("UseSparseArrays")
	public GraphicManagerCanvas() {
		map = new HashMap<Integer, GraphicObjectDrawable>();
	}

	public void reset(){
		map.clear();
	}

	@Override
	public void init() {

	}

	@Override
	public void goToBack() {
		Iterator<Integer> itr = map.keySet().iterator();

		while(itr.hasNext())
		{
			int key = itr.next();

			GraphicObjectDrawable obj = map.get(key);

			//使われないものをリストから解放
			if(!obj.getUsing()){
				int id = obj.getId();

				if (map.containsKey(id)) {
					itr.remove();
				}
			}

			//グラフィックを解放
			obj.relese();
		}
	}

	@Override
	public void reLoad() {
		Iterator<Integer> itr = map.keySet().iterator();

		while(itr.hasNext())
		{
			int key = itr.next();

			GraphicObjectDrawable obj = map.get(key);

			Bitmap bitmap = getBitmap(obj.getFilepath(), obj.getLoadMode());

			if(bitmap != null){
				obj.attach(resources, bitmap);
			}
		}
	}

	private Image load(LoadMode loadMode, String path){
		ImageDrawable image = new ImageDrawable();

		//画像をファイルとして取り出す
		GraphicObjectDrawable obj = null;

		int hash = path.hashCode();

		//マップにデータがあるなら
		if (map.containsKey(hash)) {
			logger.debug("再利用" + path);

			image.setObj(map.get(image));
			return image;
		}

		Bitmap bitmap = getBitmap(path, LoadMode.ASSET);

		if(bitmap == null){
			return null;
		}

		obj = new GraphicObjectDrawable(hash, path);
		obj.attach(resources, bitmap);
		obj.setLoadMode(loadMode);
		map.put(hash, obj);

		image.setObj(obj);
		return image;
	}

	public Image loadFromAssets(String path){
		return load(LoadMode.ASSET, path);
	}

	//ローカルのファイルから読み込む
	public Image loadFromLocalFile(String path){
		return load(LoadMode.FILE, path);
	}

	private GraphicObjectDrawable userBitmap(Bitmap bitmap){
		GraphicObjectDrawable obj = new GraphicObjectDrawable(-1, null);
		obj.setLoadMode(LoadMode.NOLOAD);
		obj.attach(resources, bitmap);

		return obj;
	}

	private Canvas canvas;

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	Paint paint = new Paint();
	{
		paint.setTextSize(32);
	}

	@Override
	public void drawRect(int left, int top, int right, int bottom) {
		canvas.drawRect(left, top, right, bottom, paint);
	}

	@Override
	public void setColor(int color) {
		paint.setColor(color);
	}

	@Override
	public void drawRect(Rect rect) {
		drawRect(rect.left, rect.top, rect.right, rect.bottom);
	}

	private class StringObjectCanvas extends StringObject{
		private String str;
		private int color;
		private int drawHeight;

		public StringObjectCanvas(String str) {
			this.str = str;
			FontMetrics fm = paint.getFontMetrics();

			Rect rect = new Rect();
			paint.getTextBounds(str, 0, str.length(), rect);

			bitmapWidth = rect.width();//(int) paint.measureText(str);
			bitmapHeight = rect.height();//getHeight(fm);
			drawHeight = getDrawHeight(fm);

			color = paint.getColor();
		}

		public void draw(int x, int y){
			int backColor = paint.getColor();

			paint.setColor(color);
			canvas.drawText(str, x, y + drawHeight, paint);
			paint.setColor(backColor);
		}
	};

	@Override
	public StringObject getStringObject(String str) {
		StringObject object = new StringObjectCanvas(str);
		return object;
	}

	@Override
	public Image getStringGraphicObject(String str){
		ImageDrawable imageDrawable = new ImageDrawable();
		Bitmap bitmap = getStingBitmap(paint, str);
		GraphicObjectDrawable obj = userBitmap(bitmap);
		obj.attach(resources, bitmap);
		obj.setLoadMode(LoadMode.NOLOAD);

		imageDrawable.setObj(obj);

		return imageDrawable;
	}

}
