package com.github.azegami.game.sample;

import com.github.azegami.game.framework.ConfigBase;
import com.github.azegami.game.framework.SceneBase;

public class Config extends ConfigBase{
	SceneBase[] sceneBases;

	public Config() {
		sceneBases = new SceneBase[]{
			new TestScene()
		};
	}

	@Override
	public SceneBase getFirstScene() {
		return sceneBases[0];
	}

	@Override
	public SceneBase[] getScenes() {
		return sceneBases;
	}

	@Override
	//ユーザーのログを出力するか
	public boolean isDebug() {
		return true;
	}
}
