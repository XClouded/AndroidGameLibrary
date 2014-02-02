package com.github.azegami.game.framework;

public class SceneManager{
	SceneBase[] scenes;
	SceneBase scene, nextScene;

	void init(ConfigBase config) {
		scenes = config.getScenes();
		scene = config.getFirstScene();
		nextScene = null;
	}

	void changeScene(){
		if(nextScene == null) return;

		scene.release();
		scene = nextScene;
		nextScene = null;
		scene.init();
	}

	SceneBase getNextScene() {
		return nextScene;
	}

	SceneBase getCurrentScene() {
		return scene;
	}

	public SceneBase getScene(int id) {
		if(id < 0 || id >= scenes.length) return null;

		return scenes[id];
	}

	public SceneBase getScene(String sceneName) {
		if(sceneName == null) return null;

		for(SceneBase s : scenes){
			if(s.sceneName.equals(sceneName))
				return s;
		}

		return null;
	}

	public void setNext(SceneBase scene) {
		if(scene == null) return;
		this.nextScene = scene;
	}
}
