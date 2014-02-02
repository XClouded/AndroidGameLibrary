package com.github.azegami.game.sample;

import android.graphics.Point;
import android.graphics.Rect;

import com.github.azegami.game.framework.Image;
import com.github.azegami.game.framework.SceneBase;
import com.github.azegami.game.framework.Logger.Logger;
import com.github.azegami.game.framework.Logger.LoggerManager;
import com.github.azegami.game.framework.android.TouchState;

public class TestScene extends SceneBase{
	int x, y;			//画像の座標
	int vx, vy;		//画像の移動量
	Image image;	//画像オブジェクト
	boolean moving;	//画像が移動するか
	Rect screenRect;	//画像の動ける範囲
	int speed = 3;		//画像が動くスピード
	Point backTouchPoint;	//前回のタッチ座標
	Logger logger;	//ログ出力用クラス

	@Override
	public void init() {
		image = gameLib.gManager.loadFromAssets("ic_launcher.png");

		x = (gameLib.getVirtualWidth() - image.getWidth())/ 2;
		y = (gameLib.getVirtualHeight() - image.getHeight())/ 2;

		vx = 0;
		vy = speed;
		moving = false;

		screenRect = new Rect(0, 0,
				gameLib.getVirtualWidth() - image.getWidth(),
				gameLib.getVirtualHeight() - image.getHeight());

		backTouchPoint = new Point();
		logger = LoggerManager.getLogger(TestScene.class.getName());
	}

	@Override
	public void calc() {
		if(moving){
			logger.debug(String.format("x = %s y = %s", x, y));
			x+= vx;
			y+= vy;

			if(x < screenRect.left || x > screenRect.right){
				vx = -vx;
			}

			if(y < screenRect.top || y > screenRect.bottom){
				vy = -vy;
			}
		}

		TouchState touchState = gameLib.touchInfo.getTouchState();

		//タッチ開始持なら
		if(touchState == TouchState.TOUCH_START){
			moving = true;
			Point point = gameLib.touchInfo.getTouchPoint(TouchState.TOUCH_START);
			backTouchPoint.set(point.x, point.y);
			logger.info("TOUCH_START");
		}else if(touchState ==TouchState.TOUCH_MOVING){
			//タッチ座標が移動するなら
			Point point = gameLib.touchInfo.getTouchPoint(TouchState.TOUCH_MOVING);

			if(point.x < backTouchPoint.x)
				vx = -speed;
			else if(point.x > backTouchPoint.x)
				vx = speed;

			if(point.y < backTouchPoint.y)
				vy = -speed;
			else if(point.y > backTouchPoint.y)
				vy = speed;

			backTouchPoint.set(point.x, point.y);
		}else if(touchState == TouchState.TOUCH_END){
			//タッチ終了なら
			moving = false;
		}

	}

	@Override
	public void draw() {
		image.draw(x, y);
	}

	@Override
	public void release() {

	}
}
