package com.github.azegami.game.framework;

public enum ActivityState{
	RELEASE,	//リソースの開放
	INIT,			//ゲームの初期化/復元
	LOOP,		//ゲームのメインループ
	DUMMY,		//何もしない
};
