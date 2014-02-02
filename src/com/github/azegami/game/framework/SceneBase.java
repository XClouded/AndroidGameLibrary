package com.github.azegami.game.framework;

public abstract class SceneBase {
	/**
	 * シーン名
	 */
	public final String sceneName;

	protected static SceneManager sceneManager;
	protected GameLibrary gameLib = GameLibrary.getLibrary();

	public SceneBase() {
		this("");
	}

	public SceneBase(String sceneName) {
		this.sceneName = sceneName;
	}

	/**
	 * ステージが開始前の初期化
	 */
	public abstract void init();

	/**
	 * 1フレーム毎の計算
	 */
	public abstract void calc();

	/**
	 * １フレーム毎の描画
	 */
	public abstract void draw();

	/**
	 * シーン終了時の開放処理
	 */
	public abstract void release();

	/**
	 * 次のシーンへ切り替え
	 * @param id シーンの配列のインデックス
	 */
	public void nextScene(int id){
		SceneBase scene = sceneManager.getScene(id);
		sceneManager.setNext(scene);
	}

	/**
	 * シーン名を指定して次のシーンへ切り替え
	 * @param sceneName シーン名
	 */
	public void nextScene(String sceneName){
		SceneBase scene = sceneManager.getScene(sceneName);
		sceneManager.setNext(scene);
	}

	public void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
}
