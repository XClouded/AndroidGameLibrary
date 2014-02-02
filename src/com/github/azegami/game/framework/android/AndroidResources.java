package com.github.azegami.game.framework.android;

import com.github.azegami.game.framework.GameLibrary;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class AndroidResources {
	protected static Activity activity;
	protected static Context context;
	protected static Resources resources;
	protected static AssetManager assetManager;
	protected static GameLibrary gameLib;

	public static void setGameLib(GameLibrary gameLib) {
		AndroidResources.gameLib = gameLib;
	}

	public static void initResources(Activity gameActivity){
		AndroidResources.activity = gameActivity;
		context = gameActivity.getApplicationContext();
		resources = context.getResources();
		assetManager = resources.getAssets();
	}
}
