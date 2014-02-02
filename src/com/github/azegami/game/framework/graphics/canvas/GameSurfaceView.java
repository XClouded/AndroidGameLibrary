package com.github.azegami.game.framework.graphics.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.github.azegami.game.framework.ActivityState;
import com.github.azegami.game.framework.FrameworkLogConfig;
import com.github.azegami.game.framework.GameLibrary;
import com.github.azegami.game.framework.GameViewCallback;
import com.github.azegami.game.framework.Logger.Logger;
import com.github.azegami.game.framework.Logger.LoggerManager;
import com.github.azegami.game.framework.android.GameActivity;
import com.github.azegami.game.framework.android.TouchState;

public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable, GameViewCallback{
	//システム用
	private SurfaceHolder surfaceHolder;
	private Thread thread;
	private Logger logger = LoggerManager.getLogger(FrameworkLogConfig.viewLog, "GameSurfaceView");
	GameLibrary gameLib = GameLibrary.getLibrary();
	GameActivity gameActivity;
	ActivityState state;

	//初期化をするフラグ
	private boolean appInitFlag = true;

	public void setInitFlag(boolean flag){
		appInitFlag = flag;
	}

	//コンストラクタ（変更する必要はない）
	public GameSurfaceView(Context context, GameActivity activity) {
		super(context);
		setFocusable(true);

		//SurfaceViewのお約束
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);

		logger.debug("GameSurfaceView コンストラクタ");

		this.gameActivity = activity;
	}

	@Override
	// SurfaceViewが変化（画面の大きさ，ピクセルフォーマット）した時のイベントの処理を記述
	public void surfaceChanged (SurfaceHolder holder, int format, int width, int height) {
		gameLib.screenInfo.setScreenInfo(format, width, height); /** ライブラリに情報を設定*/

		logger.debug("surfaceChanged" + "幅 " + width + " 高さ " + height);

		//Threadを作成
		thread = new Thread(this);

		thread.setName("GameFramework Thread");


		//runへ飛ぶ
		thread.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// SurfaceViewが作られた時の処理を記述
		logger.debug("surfaceCreated");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// SurfaceViewが廃棄された時の処理を記述
		//ループを抜ける
		thread = null;

		logger.debug("surfaceDestroyed");
	}

	//タッチイベントを処理
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();

		//タッチされた座標
		int x = (int)event.getX();
		int y = (int)event.getY();

		if(action == MotionEvent.ACTION_DOWN){
			//タッチされた場所を受け取る。
			gameLib.touchInfo.setTouchPoint(x, y);
			gameLib.touchInfo.setTouchData(TouchState.TOUCH_START, x, y);
		}else if(action == MotionEvent.ACTION_MOVE){
			gameLib.touchInfo.setTouchData(TouchState.TOUCH_MOVING, x, y);
		}else if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
			gameLib.touchInfo.setTouchData(TouchState.TOUCH_END, x, y);
		}
		return true;
	}

	/**
	 * ゲームを進行するか返す
	 * @return
	 */
	private boolean isGameActive(){
		//ダイアログが出ていないで、ゲームがアクティブなら
		return !gameLib.messageDialog.isDialogStarted() && gameLib.isActive();
	}

	public void setState(ActivityState state) {
		this.state = state;
	}

	Paint paint = new Paint();
	Paint bgPaint = new Paint();


	//拡大レートを取得
	double xRate;
	double yRate;

	/**
	 * アクティブ時に呼ばれるループ
	 */
	private void activeLoop(){
		if(state == ActivityState.INIT){
			logger.debug("初期化");

			//ゲーム中に一回初期化
			if(appInitFlag){
				//初期化に失敗したら終了
				if(!gameActivity.init()) return;

				appInitFlag = false;
			}else {
				//復帰時なら
				gameLib.resume();
			}

			state = ActivityState.LOOP;
		}else if(state == ActivityState.LOOP){
			//選択中でないなら
			if(isGameActive()){
				gameActivity.calc();
			}

			//lockCanvasでCanvasを手に入れる（ロックする）
			Canvas canvas = surfaceHolder.lockCanvas();

			//canvasが取得できなかったら戻る
			if(canvas != null){
				graphicManager.setCanvas(canvas);
				ImageDrawable.setCanvas(canvas);

				//画面を塗りつぶす
				canvas.drawRect(
						0, 0,
						gameLib.screenInfo.getWidth(), gameLib.screenInfo.getHeight(),
						bgPaint);

				canvas.scale((float)xRate, (float)yRate);

				gameActivity.draw();

				//unlockCanvasで解放（アンロックする）
				surfaceHolder.unlockCanvasAndPost(canvas);

				gameActivity.endDraw();
				canvas = null;
			}
		}
	}

	/**
	 * リソースを開放
	 */
	private void resourcesRelease(){
		//画像をメモリから解放
		gameLib.gManager.goToBack();
		gameLib.mediaManager.goToBack();
	}

	public void run(){
		logger.debug("run");

		boolean exitException = false;

		//背景色
		bgPaint.setStyle(Style.FILL);
		bgPaint.setColor(Color.WHITE);

		//描画する時に渡すpaintの初期設定
		paint.setStyle(Style.FILL);
		paint.setColor(Color.BLUE);

		//拡大レートを取得
		xRate = (double)gameLib.screenInfo.getWidth() / gameLib.getVirtualWidth();
		yRate = (double)gameLib.screenInfo.getHeight() / gameLib.getVirtualHeight();

		try {
			state = ActivityState.INIT;

			//変更はしない部分
			while(thread != null){
				//フレーム毎の処理
				gameLib.loop();

				if(gameLib.isActive()){
					activeLoop();
				}else{
					if(state == ActivityState.RELEASE){
						logger.debug("画面OFF メモリ開放");
						resourcesRelease();
						state = ActivityState.DUMMY;
					}
				}
				gameActivity.sleep(3);
			}
		} catch (Exception e) {
			logger.error(e.toString());
			e.printStackTrace();

			exitException = true;
		}

		gameActivity.release();

		logger.debug("アプリバックグラウンド メモリ開放");
		resourcesRelease();

		if(exitException){
			gameLib.endGame();
		}
	}

	@Override
	public View getView() {
		return this;
	}

	GraphicManagerCanvas graphicManager;
	public void setGraphicManeger(GraphicManagerCanvas graphicManagerCanvas) {
		graphicManager = graphicManagerCanvas;
	}

	@Override
	public void onResume() {

	}

	@Override
	public void onPause() {

	}
}
