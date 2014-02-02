package com.github.azegami.game.framework;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

import com.github.azegami.game.BuildConfig;
import com.github.azegami.game.framework.Logger.LoggerManager;
import com.github.azegami.game.framework.android.GameActivity;

public class GameLoop extends GameActivity{
	SceneManager sceneManager;
	SceneBase scene;
	ConfigBase config;

	public GameLoop() {

	}

	String getResourcesString(String name){
		int id = getResources().getIdentifier(name, "string", getPackageName());

		String value;
		if(id != 0){
			value = getResources().getString(id);
		}else{
			value = "";
		}

		return value;
	}
	/**
	 * Activityのデータを読み込む
	 */
	@Override
	public boolean loadConfig(){
		ActivityInfo info = null;

		try {
			info = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA );
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}

		if(info.metaData.containsKey("config")){
			String className = info.metaData.getString("config");

			try {
				Class<?> clazz = Class.forName(className);

				if (clazz != null) {
					config = ConfigBase.class.cast(clazz.newInstance());
				}
			}
			catch(Exception e){}
			finally{
				//設定ファイルが見つからないなら表示して終了
				if(config == null){
					gameLib.handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(GameLoop.this,
							getResourcesString("CONFIG_CLASS_NOT_FOUND"),
							Toast.LENGTH_SHORT).show();
							GameLibrary.logger.debug(getResourcesString("CONFIG_CLASS_NOT_FOUND"));
							GameLoop.this.finish();
						}
					});

					return false;
				}else{
					LoggerManager.setDebug(BuildConfig.DEBUG && config.isDebug());
				}
			}
		}else{
			gameLib.handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(GameLoop.this,
							getResourcesString("CONFIG_NOT_FOUND"),
							Toast.LENGTH_SHORT).show();
							GameLibrary.logger.debug(getResourcesString("CONFIG_NOT_FOUND"));
							GameLoop.this.finish();
				}
			});
		}

		int width = info.metaData.getInt("screen-width", 480);
		int height = info.metaData.getInt("screen-height", 800);

		gameLib.virtualScreenInfo.setScreenInfo(-1, width, height);

		return true;
	}

	@Override
	public boolean init() {
		sceneManager = new SceneManager();
		sceneManager.init(config);
		config = null;
		scene = sceneManager.getCurrentScene();

		if(scene == null) return false;
		scene.init();

		SceneBase.sceneManager = sceneManager;

		return true;
	}

	@Override
	public void calc() {
		scene.calc();
	}

	@Override
	public void draw() {
		scene.draw();
	}

	@Override
	public void endDraw() {
		SceneBase nextScene = sceneManager.getNextScene();

		if(nextScene != null){
			sceneManager.changeScene();
			scene = sceneManager.getCurrentScene();
		}
	}
}
