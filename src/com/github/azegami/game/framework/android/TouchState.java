package com.github.azegami.game.framework.android;

public enum TouchState{
	TOUCH_NO_STATE,	//どれの状態にも当てはまらない場合（スタート時 タッチ終了時から1フレーム過ぎたところ）
	TOUCH_START,		//タッチが始まったと時
	TOUCH_MOVING,	//タッチが始まって移動している時
	TOUCH_END			//タッチ終了時
};