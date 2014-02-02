package com.github.azegami.game.framework.Sound;

import android.content.Context;
import android.media.AudioManager;

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

		seVolume = (float)currentVolume / maxVolume;

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
