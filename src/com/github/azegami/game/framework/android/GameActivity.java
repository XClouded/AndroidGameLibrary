package com.github.azegami.game.framework.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;

import com.github.azegami.game.framework.ActivityState;
import com.github.azegami.game.framework.FrameworkLogConfig;
import com.github.azegami.game.framework.GameLibrary;
import com.github.azegami.game.framework.GameLibrary.GraphicMode;
import com.github.azegami.game.framework.GameViewCallback;
import com.github.azegami.game.framework.StringUtil;
import com.github.azegami.game.framework.Logger.Logger;
import com.github.azegami.game.framework.Logger.LoggerManager;
import com.github.azegami.game.framework.graphics.canvas.GameSurfaceView;
import com.github.azegami.game.framework.graphics.canvas.GraphicManagerCanvas;

public abstract class GameActivity extends FragmentActivity{
	private GameViewCallback viewCallback;
	public GameApplication gameApplication;
	public GameActivity activity;

	private Logger logger;

	public GameActivity() {
		String className = StringUtil.getClassName(GameActivity.class.getName());
		logger = LoggerManager.getFrameworkLogger(FrameworkLogConfig.actityLog, className);
		activity = this;
	}

	//ゲームライブラリ
	protected final GameLibrary gameLib = GameLibrary.getLibrary();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		logger.debug("onCreate");

		//タイトルバーの非表示
		//setContentViewの前に呼ぶ必要がある
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		gameLib.setHandler(new Handler());

		if(!loadConfig()){
			return;
		}

		gameApplication = (GameApplication)getApplication();

		gameLib.reset();
		gameLib.init(this);

		if(gameLib.getGraphicMode() == GraphicMode.CANVAS){
			GameSurfaceView surfaceView = new GameSurfaceView(getApplicationContext(), this);
			surfaceView.setGraphicManeger((GraphicManagerCanvas)gameLib.gManager);
			viewCallback = surfaceView;
		}

		setContentView(viewCallback.getView());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//戻るのだったら
		if(keyCode == KeyEvent.KEYCODE_BACK){
			logger.debug("BackKey");
			gameLib.setBackKey(true);

			onKeyBack();
			return false;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		logger.debug("onStart");
	}

	@Override
	protected void onRestart(){
		super.onRestart();
		logger.debug("onRestart");
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		logger.debug("onResume");

		//リソースの読み込み
		gameLib.setActive(true);
		viewCallback.setState(ActivityState.INIT);
		viewCallback.onResume();
	}

	@Override
	protected void onPause(){
		super.onPause();

		//ゲームをリセットするなら
		if(gameLib.isReset()){
			//初期化させる
			viewCallback.setInitFlag(true);

			logger.debug("ゲームリセット");

			gameLib.setBackKey(false);
			gameLib.setResetFlag(false);
		}

		viewCallback.onPause();

		//this.finish();
		logger.debug("onPause");
	}

	@Override
	protected void onStop(){
		super.onStop();
		logger.debug("onStop");

		//リソースの解放
		gameLib.setActive(false);
		viewCallback.setState(ActivityState.RELEASE);
	}

	@Override
	//終了時に呼ばれる音を止めるなど
	protected void onDestroy(){
		super.onDestroy();
		logger.debug("onDestroy");
	}

	public void toActivity(Intent intent){
		startActivity( intent );
		finish();
	}

	public void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}

	public boolean loadConfig(){return true;}
	public boolean init(){return true;}
	public abstract void calc();
	public abstract void draw();
	public void endDraw(){}
	public void release(){}
	public void onKeyBack() {}
}
