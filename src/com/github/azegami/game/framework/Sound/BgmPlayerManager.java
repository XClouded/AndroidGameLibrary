package com.github.azegami.game.framework.Sound;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import com.github.azegami.game.framework.LoadMode;
import com.github.azegami.game.framework.Manager;

public class BgmPlayerManager extends Manager implements InterfaceSoundManager  {
	private BgmPlayer[] bgmPlayer;

	@SuppressLint("UseSparseArrays")
	private final Map<Integer, Integer> map = new HashMap<Integer, Integer>();//楽曲のＩＤをリソースIDに結んだもの

	@Override
	public void releaseMap(){
		map.clear();
	}

	@Override
	public void release() {
		if(bgmPlayer != null){
			for(int i = 0; i < bgmPlayer.length;i++)
				if(bgmPlayer[i] != null)
					bgmPlayer[i].release();
		}

		bgmPlayer = null;
	}

	@Override
	public void init(int soundNum) {
		release();
		bgmPlayer = new BgmPlayer[soundNum];
	}

	private int getNextId(){
		for(int i = 0; i < bgmPlayer.length;i++)
			if(bgmPlayer[i] == null)
				return i;

		return -1;
	}

	private int load(LoadMode loadMode, String path){
		int hashId = path.hashCode();

		//ハッシュが登録されてれば
		if(map.containsKey(hashId)){
			return map.get(hashId);
		}

		int id = getNextId();

		MediaPlayer mediaPlayer = getMediaPlayer(loadMode, path);

		if(id != -1 && mediaPlayer != null){
			bgmPlayer[id] = new BgmPlayer(hashId, path);
			bgmPlayer[id].setLoadMode(loadMode);
			bgmPlayer[id].attach(mediaPlayer);
			map.put(hashId, id);
		}

		return id;
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
	public void reLoad(){
		for(int i = 0; i < bgmPlayer.length;i++){
			if(bgmPlayer[i] != null){
				MediaPlayer mediaPlayer =
						getMediaPlayer(bgmPlayer[i].getLoadMode(), bgmPlayer[i].getPath());

				bgmPlayer[i].attach(mediaPlayer);
				bgmPlayer[i].reset();
			}
		}
	}

	@Override
	public boolean isPlaying(int soundId) {
		if(soundId != -1){
			return bgmPlayer[soundId].isPlaying();
		}

		return false;
	}

	public void play(int soundId) {
		play(soundId, true);
	}

	@Override
	public void play(int soundId, boolean start) {
		if(soundId != -1){
			bgmPlayer[soundId].play(start);
		}
	}

	@Override
	public void stop(int soundId) {
		if(soundId != -1){
			bgmPlayer[soundId].stop();
		}
	}

	public void delete(int soundId) {
		if(soundId != -1){
			bgmPlayer[soundId].release();
			bgmPlayer[soundId] = null;
		}
	}

	private MediaPlayer getMediaPlayer(LoadMode loadMode, String path){
		MediaPlayer mediaPlayer = null;

		try{
			switch (loadMode) {
			case ASSET:

				AssetFileDescriptor afd = assetManager.openFd(path);
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
				afd.close();
				mediaPlayer.prepare();

				break;
			case FILE:
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setDataSource(path);
				break;

			case RESOURCE:
				mediaPlayer = MediaPlayer.create(context, Integer.valueOf(path));
				break;
			default:
				break;
			}
		}catch(FileNotFoundException e){
		} catch (IOException e) {
		}

		return mediaPlayer;
	}

	/**
	 * ゲームがバックグラウンドに行くとき呼ばれる
	 */
	@Override
	public void stopAll() {
		for(int i = 0; i < bgmPlayer.length;i++){
			if(bgmPlayer[i] != null){
				//状態を保存
				bgmPlayer[i].save();

				//再生している状態を変えないで再生をストップ
				bgmPlayer[i].stopForAll();

				//MediaPlayerを解放
				bgmPlayer[i].release();
			}
		}
	}

	@Override
	public void goToBack() {}
}
