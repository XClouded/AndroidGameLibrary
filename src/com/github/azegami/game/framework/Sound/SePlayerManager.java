package com.github.azegami.game.framework.Sound;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import com.github.azegami.game.framework.FrameworkLogConfig;
import com.github.azegami.game.framework.GameLibrary;
import com.github.azegami.game.framework.LoadMode;
import com.github.azegami.game.framework.Manager;
import com.github.azegami.game.framework.Logger.Logger;
import com.github.azegami.game.framework.Logger.LoggerManager;

public class SePlayerManager extends Manager implements InterfaceSoundManager, OnLoadCompleteListener {
	private class SeObject{
		private String path;					//ファイルパス
		private LoadMode loadMode;	//読み込んだ形式
		private int soundId;				//SoundPoolのサウンドID
		private boolean ready;				//再生できるか？
	}

	private SoundPool soundPool;
	float volumeRate;	//SE再生の音量

	@SuppressLint("UseSparseArrays")
	private final Map<Integer, Integer> map = new HashMap<Integer, Integer>();//楽曲のＩＤをリソースIDに結んだもの

	SeObject[] seObjects;
	private int soundNum;
	Logger logger = LoggerManager.getFrameworkLogger(FrameworkLogConfig.seLog, "SePlayerManager");

	@Override
	public void releaseMap(){
		map.clear();
	}

	@Override
	public void release() {
		if(soundPool != null)
			soundPool.release();
	}

	@Override
	public void init(int soundNum) {
		release();
		this.soundNum = soundNum;
		seObjects = new SeObject[soundNum];
		soundPool = new SoundPool(soundNum, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(this);
	}

	int getSoundId(LoadMode loadMode, String path){
		int key = -1;
		try{
			switch (loadMode) {
			case ASSET:
				key = soundPool.load(assetManager.openFd(path), 1);
				break;
			case FILE:
				break;
			case RESOURCE:
				key = soundPool.load(context, Integer.valueOf(path), 1);
				break;
			default:
				break;
			}
		}catch(FileNotFoundException e){
		} catch (IOException e) {
		}

		return key;
	}

	/**
	 * まだ使用してないインデックスを探す
	 * @return
	 */
	private int getNextIndex(){
		for(int i = 0; i < seObjects.length;i++){
			if(seObjects[i] == null)
				return i;
		}

		return -1;
	}

	private int load(LoadMode loadMode, String path) {
		int hashId = path.hashCode();

		if(map.containsKey(hashId)){
			return map.get(hashId);
		}

		int soundId = getSoundId(loadMode, path);
		int index = getNextIndex();

		if(index != -1 && soundId != -1){
			seObjects[index] = new SeObject();
			seObjects[index].loadMode = loadMode;
			seObjects[index].path = path;
			seObjects[index].soundId = soundId;
			seObjects[index].ready = false;
			map.put(hashId, index);
		}

		return index;
	}

	@Override
	public int load(int resId) {
		return load(LoadMode.RESOURCE, Integer.valueOf(resId).toString());
	}

	@Override
	public int loadFromAssets(String path) {
		return load(LoadMode.ASSET, path);
	}

	@Override
	public void reLoad() {
		soundPool = new SoundPool(soundNum, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(this);

		Iterator<Integer> iterator = map.keySet().iterator();

		while(iterator.hasNext()){
			int id = iterator.next();
			int key = map.get(id);
			seObjects[key].ready = false;
			seObjects[key].soundId = getSoundId(seObjects[key].loadMode, seObjects[key].path);
		}
	}

	/**
	 * SEはいつも再生してないことにする。
	 */
	@Override
	public boolean isPlaying(int key) { return false; }

	@Override
	public void play(int key) {
		play(key, true);
	}

	@Override
	public void play(int key, boolean start) {
		//マナーモードでないなら再生
		if(seObjects[key].ready && !GameLibrary.getLibrary().mediaManager.isMute()){
			soundPool.play(seObjects[key].soundId, volumeRate, volumeRate, 0, 0, 1.0F);
		}
	}

	@Override
	public void stop(int key) {
		soundPool.stop(seObjects[key].soundId);
	}

	@Override
	public void stopAll() {
		Iterator<Integer> iterator = map.keySet().iterator();

		while(iterator.hasNext()){
			int id = iterator.next();
			int soundId = seObjects[map.get(id)].soundId;
			soundPool.stop(soundId);
		}

		release();
	}

	@Override
	public void onLoadComplete(SoundPool pool, int sampleId, int status) {
		for(SeObject obj : seObjects){
			if(obj != null && !obj.ready && obj.soundId == sampleId){
				if(status == 0){
					obj.ready = true;
					logger.debug("サウンドロード完了");
					break;
				}
			}
		}
	}

}
