package com.github.azegami.game.framework.Sound;

import android.content.Context;
import android.media.AudioManager;

import com.github.azegami.game.framework.Manager;

public class MusicManager extends Manager{
	public final BgmPlayerManager bgm;
	public final SePlayerManager se;
	private final SoundData soundData;

	enum MusicMode{
		SE, BGM;
	}

	//解放
	public void release(){
		bgm.release();
		se.release();
	}

	public void reset(){
		release();
		bgm.releaseMap();
		se.releaseMap();
	}

	public MusicManager() {
		bgm = new BgmPlayerManager();
		se = new SePlayerManager();
		soundData = new SoundData();
	}

	public void init(int bgmMax, int seMax){
		bgm.init(bgmMax);
		se.init(seMax);
	}

	@Override
	//音声ファイルを再読み込み
	public void reLoad() {
		bgm.reLoad();
		se.reLoad();
	}

	@Override
	public void goToBack() {
		/**
		 * * 音声をすべて止める
		 * */
		bgm.stopAll();
		se.stopAll();
	}

	public class SoundData {
		private int currentVolume;
		private int maxVolume;
		private float seVolume;
		private boolean mannerMode;

		void update(Context context){
			AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

			// 着信音モードを取得
			int ringerMode = audioManager.getRingerMode();

			currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			maxVolume  = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

			//currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
			//maxVolume  = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

			seVolume = (float)currentVolume / maxVolume;

			//GameLibrary.Log("", "" + currentVolume + " " + maxVolume);

			switch (ringerMode) {
			case AudioManager.RINGER_MODE_NORMAL: // 通常（音あり）
				mannerMode = false;
				break;
			case AudioManager.RINGER_MODE_SILENT: // サイレントモード（音なし）
			case AudioManager.RINGER_MODE_VIBRATE: // バイブレートモード（マナーモード？）
				mannerMode = true;
			default:
				break;
			}
		}

		/**
		 * SEの
		 * @return
		 */
		public float getSEVolume(){
			return seVolume;
		}

		/**
		 * マナーモードか
		 * @return
		 */
		public boolean isMannerMode() {
			return mannerMode;
		}
	}

	/**
	 * 音再生しないか
	 * @return
	 * 再生しないなら true<br>
	 * 再生するなら false
	 */
	public boolean isMute(){
		return soundData.isMannerMode() || !gameLib.isActive();
	}

	private void phoneUpdate(){
		soundData.update(context);
		se.volumeRate = soundData.getSEVolume();
	}

	public void update(){
		phoneUpdate();
	}
}

//package com.test.framework.Sound;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.media.AudioManager;
//
//import com.test.framework.Manager;
//import com.test.framework.android.GameActivity;
//
//public class MusicManager extends Manager{
//	public final BgmPlayerManager bgm;
//	public final SePlayerManager se;
//	private final SoundData soundData;
//	private EventReceiver eventReceiver;
//	private MannerState phoneMannerState;
//
//	/**
//	 * マナーモードの状態
//	 */
//	public static enum MannerState{
//		MANNER("マナー"),		//マナーモード
//		SILENT("サイレント"),	//サイレントモード
//		OFF("通常"),				//マナーモードOFF
//		ERROR("エラー");		//予想外な状態
//
//		private String name;
//
//		private MannerState(String name) {
//			this.name = name;
//		}
//
//		public String getName() {
//			return name;
//		}
//	};
//
//	public MannerState getPhoneMannerState() {
//		return phoneMannerState;
//	}
//
//	public class EventReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
//				switch(intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1)){
//				case AudioManager.RINGER_MODE_VIBRATE:
//					phoneMannerState = MannerState.MANNER;
//					break;
//				case AudioManager.RINGER_MODE_SILENT:
//					phoneMannerState = MannerState.SILENT;
//					break;
//				case AudioManager.RINGER_MODE_NORMAL:
//					phoneMannerState = MannerState.OFF;
//					break;
//				}
//			}
//		}
//	}
//
//	/**
//	 * アクティブになったときに呼ばれる
//	 */
//	public void active(){
//		final IntentFilter filter = new IntentFilter();
//		filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
//		filter.addAction(Intent.ACTION_HEADSET_PLUG);
//		filter.addAction(Intent.ACTION_UMS_CONNECTED);
//		filter.addAction(Intent.ACTION_UMS_DISCONNECTED);
//		eventReceiver = new EventReceiver();
//gameLib.logger.debug("active" + activity.getTaskId());
//				activity.registerReceiver(eventReceiver, filter);
//		gameLib.uiChange(new Runnable() {
//
//			@Override
//			public void run() {
//
//			}
//		});
//	}
//
//	/**
//	 * 非アクティブになるときに呼ばれる
//	 */
//	public void inactive(GameActivity activity){
//		activity.unregisterReceiver(eventReceiver);
//
//		gameLib.uiChange(new Runnable() {
//
//			@Override
//			public void run() {
//
//			}
//		});
//	}
//
//	//解放
//	public void release(){
//		bgm.release();
//		se.release();
//	}
//
//	public void reset(){
//		release();
//		bgm.releaseMap();
//		se.releaseMap();
//	}
//
//	public MusicManager() {
//		bgm = new BgmPlayerManager();
//		se = new SePlayerManager();
//		soundData = new SoundData();
//	}
//
//	public void init(int bgmMax, int seMax){
//		bgm.init(bgmMax);
//		se.init(seMax);
//		soundData.init();
//	}
//
//	@Override
//	//音声ファイルを再読み込み
//	public void reLoad() {
//		bgm.reLoad();
//		se.reLoad();
//	}
//
//	@Override
//	public void goToBack() {
//		/**
//		 * * 音声をすべて止める
//		 * */
//		bgm.stopAll();
//		se.stopAll();
//	}
//
//	private class SoundData {
//		private int currentVolume;
//		private int maxVolume;
//		private float seVolume;
//		private boolean mannerMode;
//
//		public void init(){
//			AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
//
//			currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//			maxVolume  = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//		}
//
//		public void update(Context context){
//			seVolume = (float)currentVolume / maxVolume;
//
//			if(phoneMannerState == MannerState.MANNER ||
//					phoneMannerState == MannerState.SILENT){
//				mannerMode = true;
//			}else{
//				mannerMode = false;
//			}
//		}
//
//		/**
//		 * SEの
//		 * @return
//		 */
//		public float getSEVolume(){
//			return seVolume;
//		}
//
//		/**
//		 * マナーモードか
//		 * @return
//		 */
//		public boolean isMannerMode() {
//			return mannerMode;
//		}
//	}
//
//	/**
//	 * 音再生しないか
//	 * @return
//	 * 再生しないなら true<br>
//	 * 再生するなら false
//	 */
//	public boolean isMute(){
//		return soundData.isMannerMode() || !gameLib.isActive();
//	}
//
//	private void phoneUpdate(){
//		soundData.update(context);
//		se.volumeRate = soundData.getSEVolume();
//	}
//
//	public void update(){
//		phoneUpdate();
//	}
//}
