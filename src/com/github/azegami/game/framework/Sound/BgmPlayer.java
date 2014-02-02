package com.github.azegami.game.framework.Sound;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.github.azegami.game.framework.FrameworkLogConfig;
import com.github.azegami.game.framework.GameLibrary;
import com.github.azegami.game.framework.LoadMode;
import com.github.azegami.game.framework.Logger.Logger;
import com.github.azegami.game.framework.Logger.LoggerManager;

public class BgmPlayer implements OnCompletionListener {
	private MediaPlayer player;

	private boolean playing;		//再生しているか
	private int playPostion;		//再生している位置
	private int resId;				//
	private String path;				//
	private LoadMode loadMode = LoadMode.NOLOAD;
	private Logger logger = LoggerManager.getFrameworkLogger(FrameworkLogConfig.bgmLog, "BgmPlayer");

	public BgmPlayer(int hashId, String path) {
		player = null;

		resId = hashId;
		this.path = path;
	}

	//再生しているか
	public boolean isPlaying(){
		return playing;
	}

	//再生していたら止めて再生
	//flagは最初から再生するか
	public void play(boolean restartFlag){
		if(restartFlag)
			player.seekTo(0);

		playing = true;

		//マナーモードでないなら再生
		if(!GameLibrary.getLibrary().mediaManager.isMute()){
			player.start();
			player.setLooping(true);
		}

		logger.debug("play");
	}

	public void stop(){
		player.pause();
		playing = false;

		logger.debug("stop");
	}

	/**
	 * StopAllで呼ばれた時、reloadで再生されなくならないように
	 */
	void stopForAll(){
		player.pause();

		logger.debug("stopAll");
	}

	@Override
	public void onCompletion(MediaPlayer mediaplayer) {
		playing = false;
		logger.debug("stop Event");
	}

	void attach(MediaPlayer mediaPlayer){
		player = mediaPlayer;
	}

	public void reset(){
		if(playPostion != -1){
			player.seekTo(playPostion);

			if(playing)
				play(false);
		}
	}

	public void save(){
		if(player != null)
			playPostion = player.getCurrentPosition();
		else
			playPostion = -1;
	}

	public void release(){
		if(player != null)
			player.release();

		player = null;
	}

	public int getId() {
		return resId;
	}

	public String getPath() {
		return path;
	}

	/**
	 * 読み込みモードを設定する
	 * @param loadMode
	 */
	public void setLoadMode(LoadMode loadMode) {
		this.loadMode = loadMode;
	}

	public LoadMode getLoadMode() {
		return loadMode;
	}
}
