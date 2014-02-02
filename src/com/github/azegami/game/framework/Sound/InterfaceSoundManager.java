package com.github.azegami.game.framework.Sound;

public interface InterfaceSoundManager {
	void init(int soundNum);//初期化

	int load(int resId);		//読み込み
	int loadFromAssets(String path);
	
	boolean isPlaying(int soundId);//再生しているか

	void play(int soundId);
	void play(int soundId, boolean startpos);
	void stop(int soundId);	//音声ストップ
	void stopAll();				//すべての音声をストップ
	
	void releaseMap();	//ゲーム開始時に一度だけ呼ばれる	
	void release();		//ゲームがバックグラウンドに移行するときに読み込んだリソースを解放
}
