/**
 *
 */
package com.github.azegami.game.framework;


/**
 * フレームワークのログ設定
 * @author azegami
 *
 */
public class FrameworkLogConfig {
	/**
	 * フレームワークのログを出力するか
	 */
	public static final boolean debug = false;

	/**
	 * 各機能がログを出力するか
	 */
	public static final boolean
		graphicLog = false,		//グラフィック関係のログを出すか
		seLog = false,			//音声関係のログを出すか
		bgmLog = false,			//音声関係のログを出すか
		viewLog = false,			//SurfaceViewのログを出すか
		actityLog = true;		//アクティビティのログを出すか

}
