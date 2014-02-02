package com.github.azegami.game.framework;

import com.github.azegami.game.framework.android.AndroidResources;


/**
 * 読み込むもの
 * @author azegami
 *
 */
public abstract class Manager extends AndroidResources{
	public void init(){};			//初期化

	public abstract void reLoad();	//再開時に元の状態に復元
	public void goToBack(){};	//バックグランドに移行時に呼ばれる
}
