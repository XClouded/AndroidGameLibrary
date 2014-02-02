package com.github.azegami.game.framework;

//設定クラスの雛型
public abstract class ConfigBase {
	public abstract SceneBase getFirstScene();
	public abstract SceneBase[] getScenes();
	public abstract boolean isDebug();
}