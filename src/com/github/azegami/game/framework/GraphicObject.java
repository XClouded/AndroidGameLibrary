package com.github.azegami.game.framework;

import com.github.azegami.game.framework.Logger.Logger;
import com.github.azegami.game.framework.Logger.LoggerManager;

public class GraphicObject {
	protected int hashId;			//ファイル管理用ハッシュID
	protected String filepath;	//ファイルパス

	protected LoadMode loadMode = LoadMode.NOLOAD;	//読み込んだ方法
	protected static Logger logger = LoggerManager.getFrameworkLogger(FrameworkLogConfig.graphicLog, "GraphicObject");

	public GraphicObject(int hashId, String filepath) {
		this.hashId = hashId;
		this.filepath = filepath;
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

	public String getFilepath() {
		return filepath;
	}

	public int getId() {
		return hashId;
	}
}
