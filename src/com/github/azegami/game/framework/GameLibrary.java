package com.github.azegami.game.framework;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;

import com.github.azegami.game.framework.Logger.Logger;
import com.github.azegami.game.framework.Logger.LoggerManager;
import com.github.azegami.game.framework.Sound.MusicManager;
import com.github.azegami.game.framework.android.AndroidResources;
import com.github.azegami.game.framework.android.GameActivity;
import com.github.azegami.game.framework.android.GameApplication;
import com.github.azegami.game.framework.android.MessageDialog;
import com.github.azegami.game.framework.android.TouchInfo;
import com.github.azegami.game.framework.graphics.canvas.GraphicManagerCanvas;

public class GameLibrary extends AndroidResources{
	private static final GameLibrary instance = new GameLibrary();

	private boolean backKey;
	private boolean resetFlag = false;

	private final int TOUCH_INVALD = -1;

	public GraphicManager gManager;

	public final MusicManager mediaManager = new MusicManager();
	public final MessageDialog messageDialog = new MessageDialog();

	public Handler handler;
	String packcageName;

	public enum GraphicMode{
		CANVAS;

		@SuppressLint("DefaultLocale")
		public static GraphicMode getMode(String s, GraphicMode defaultMode){
			GraphicMode mode = defaultMode;

			s = s.toUpperCase();

			for(GraphicMode graphicMode : values()){
				if(graphicMode.name().equals(s)){
					mode = graphicMode;
					break;
				}
			}

			return mode;
		}
	};

	GraphicMode graphicMode;

	public void setGraphicMode(GraphicMode graphicMode) {
		this.graphicMode = graphicMode;

		if(graphicMode == GraphicMode.CANVAS){
			gManager = new GraphicManagerCanvas();
		}
	}

	public GraphicMode getGraphicMode() {
		return graphicMode;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
		messageDialog.setHandler(handler);
	}

	void setPackcageName(String packcageName) {
		this.packcageName = packcageName;
	}

	public static GameLibrary getLibrary() {
		return instance;
	}

	private GameLibrary(){}
	private Random random = new Random(System.currentTimeMillis());

	public Random getRandom() {
		return random;
	}

	/**
	 * 始まりの状態に戻す
	 */
	public void reset(){
		reset(true);
	}

	/**
	 * 始まりの状態に戻す
	 */
	public void reset(boolean media){
		touchInfo.reset();

		if(media){
			gManager.reset();
			mediaManager.reset();
		}
	}

	public void init(Activity gameActivity){
		AndroidResources.initResources(gameActivity);
		AndroidResources.setGameLib(this);

		gManager.init();
		mediaManager.init(10, 5);

		gameLib.touchInfo.setTouchPoint(TOUCH_INVALD, TOUCH_INVALD);
		backKey = false;
	}

	public void init(GameActivity gameActivity){
		init((Activity)gameActivity);
	}

	public Context getContext(){ return context; }
	public Resources getResources() { return resources; }

	public Activity getActivity() {
		return activity;
	}

	public void next(final Class<?> classIns){
		handler.post(new Runnable() {
			@Override
			public void run() {
				Intent intent=new Intent();
				intent.setClass(activity, classIns);

				activity.startActivity(intent);
				activity.finish();
			}
		});
	}

	public void endGame(){
		activity.moveTaskToBack(true);
	}

	public void finish(){
		handler.post(new Runnable() {

			@Override
			public void run() {
				activity.finish();
			}
		});
	}

	/**
	 * Activityのコンテキストを返す
	 * @return
	 */
	//	private Context getActivityContext(){
	//		return gameActivity;
	//	}
	/**
	 * タッチ情報
	 */
	public final TouchInfo touchInfo = new TouchInfo();
	public final static Logger logger = LoggerManager.getFrameworkLogger("GameLibrary");

	private static void logDebug(String tag, String message){
		logger.Log(tag, message);
	}

	public static void Log(String mode, String mes){
		logDebug("デバッグ" + mode, mes);
	}

	public static void frameworkLog(String mode, String mes){
		logDebug("フレームワーク " + mode, mes);
	}

	public static void frameworkLog(String mode, String mes, String className){
		frameworkLog(mode, String.format("%s %s", mes, className));
	}

	/*
	 *
	 * 以下 画面情報について
	 *
	 */

	public final ScreenInfo screenInfo = new ScreenInfo();

	public void setBackKey(boolean flag) {
		backKey = flag;
	}

	public boolean isBackKey() {
		return backKey;
	}

	public void setResetFlag(boolean flag){
		resetFlag = flag;
	}

	public boolean isReset() {
		return resetFlag;
	}

	public final ScreenInfo virtualScreenInfo = new ScreenInfo();

	public int getVirtualWidth() {
		return virtualScreenInfo.getWidth();
	}

	public int getVirtualHeight() {
		return virtualScreenInfo.getHeight();
	}

	private GameApplication gameApplication;

	public void attachGameApplication(GameApplication gameApplication){
		this.gameApplication = gameApplication;
	}

	public GameApplication getGameApplication(){
		return gameApplication;
	}

	/**
	 * 毎フレーム時に処理する
	 */
	public void loop() {
		//タッチ関係のループ処理
		touchInfo.loop();

		//音声関係の更新
		mediaManager.update();
	}

	/**
	 * アプリがバックグランドから復帰したとき
	 */
	public void resume() {
		//使っている画像をロード
		gManager.reLoad();

		//BGMの復元やSEの読み込み
		mediaManager.reLoad();
	}

	private boolean active;

	public void setActive(boolean active) {
		logger.debug("active = " + "" + active);
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	@SuppressWarnings("unused")
	/**
	 * アプリが停止するときに呼ばれる予定(未実装)
	 */
	private void appStop(){
		//音声を止める
		//mediaManager.
	}

	public String getResourcesString(int id){
		return resources.getString(id);
	}

	public void uiChange(Runnable runnable){
		handler.post(runnable);
	}
}
