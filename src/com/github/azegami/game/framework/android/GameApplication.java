package com.github.azegami.game.framework.android;

import java.util.HashMap;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.github.azegami.game.framework.FrameworkLogConfig;
import com.github.azegami.game.framework.GameLibrary;
import com.github.azegami.game.framework.GameLibrary.GraphicMode;
import com.github.azegami.game.framework.Logger.LoggerManager;

public class GameApplication extends Application {
	HashMap<String, String> appData;

	String getXmlData(String key, String defaultValue){
		String mode = defaultValue;

		try {
		    ApplicationInfo info = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);

		    if(info.metaData.containsKey(key)){
		    	mode = info.metaData.getString(key);
		    }
		} catch (NameNotFoundException e) {
		}

		return mode;
	}

	//アプリケーション起動時に描画モードを切り替える
	@Override
	public void onCreate() {
		super.onCreate();

		GraphicMode graphicMode = GraphicMode.getMode(getXmlData("Mode", ""), GraphicMode.CANVAS);

		//アプリケーション全体のログを設定
		LoggerManager.setFrameworkDebug(FrameworkLogConfig.debug);

		GameLibrary gameLib = GameLibrary.getLibrary();
		gameLib.setGraphicMode(graphicMode);
		gameLib.attachGameApplication(this);

		appData = new HashMap<String, String>();
	}

	public boolean isDataContainsKey(String key){
		return appData.containsKey(key);
	}

	public String getData(String key){
		return appData.get(key);
	}

	public void setData(String key, String value){
		appData.put(key, value);
	}

}
