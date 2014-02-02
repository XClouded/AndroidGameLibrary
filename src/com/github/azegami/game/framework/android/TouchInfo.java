package com.github.azegami.game.framework.android;

import android.graphics.Point;

/**
 * タッチに関するクラス
 */
public class TouchInfo extends AndroidResources{
	private final Point startPoint = new Point();		//タッチし始めた座標
	private final Point movingPoint = new Point(); 	//タッチが始まって移動している座標
	private final Point endPoint = new Point();			//タッチ終了時の座標
	private final Point nonePoint = new Point();		//タッチしてない時など(意味がない値)
	private final Point tmp = new Point();				//受け渡し用

	private TouchState touchState = TouchState.TOUCH_NO_STATE;
	private int touchCount;								//その状態になってからのカウント

	/**
	 * フレームワークで使う初期化関数
	 */
	public void reset(){
		touchCount = 0;
		touchState = TouchState.TOUCH_NO_STATE;
	}

	/**
	 * 指定された状態のポイントインスタンスを返す
	 * @param state 指定する状態
	 */
	private Point getTouchPointInstance(TouchState state){
		Point point = null;

		switch(state){
		case TOUCH_START:
			point = startPoint;
			break;
		case TOUCH_MOVING:
			point = movingPoint;
			break;
		case TOUCH_END:
			point = endPoint;
			break;
		case TOUCH_NO_STATE:
			point = nonePoint;
			break;
		}

		return point;
	}

	/**
	 * 切り替え先を指定しデータを入れる
	 * @param touchState 切り替える状態
	 * @param x タッチ座標 X
	 * @param y タッチ座標 Y
	 */
	public void setTouchData(TouchState touchState, int x, int y){
		//おなじイベントの場合以外はリセット
		if(this.touchState != touchState)
			touchCount = 0;

		this.touchState = touchState;

		Point point = getTouchPointInstance(touchState);

		point.x = (int)(x * (double)gameLib.getVirtualWidth() / gameLib.screenInfo.getWidth());
		point.y = (int)(y * (double)gameLib.getVirtualHeight() / gameLib.screenInfo.getHeight());

		//GameLibrary.logger.debug(String.format("%s x = %d y = %d", touchState.name(), x, y));
	}

	/**
	 * ゲームループで毎フレーム呼ばれる
	 * タッチの更新処理など
	 */
	public void loop(){
		//タッチ終了状態で1フレームたったら
		if(getTouchState() == TouchState.TOUCH_END
				&& getTouchCount() == 1){

			//タッチしていないに変更（値は意味無し）
			setTouchData(TouchState.TOUCH_NO_STATE, 0, 0);
		}else {
			//タッチ状態のカウントを増やす
			touchCount++;
		}
	}

	int getTouchCount() {
		return touchCount;
	};

	public TouchState getTouchState(){
		return touchState;
	}

	public Point getTouchPoint(TouchState state){
		Point point = getTouchPointInstance(state);
		tmp.set(point.x, point.y);
		return tmp;
	}

	/*
	 * 以下 タッチ座標について(タッチしてるかチェックするだけ用）
	 */
	private final Point touchPoint = new Point();

	public void setTouchPoint(int x, int y) {
		synchronized (touchPoint) {
			touchPoint.set(x, y);
		}
	}

	public int getTouchX(){
		synchronized (touchPoint) {
			return touchPoint.x;
		}
	}

	public int getTouchY(){
		synchronized (touchPoint) {
			return touchPoint.y;
		}
	}

	/**
	 * タッチされているか
	 * @return
	 */
	public boolean isTouch() {
		return touchPoint.x != -1;
	}

	/**
	 * タッチ情報をリセット
	 */
	public void resetKey() {
		touchPoint.x = -1;
	}
}