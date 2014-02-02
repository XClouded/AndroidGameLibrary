package com.github.azegami.game.framework.Logger;

import java.util.ArrayList;
import java.util.List;

import com.github.azegami.game.framework.Logger.LoggerMode.Level;

public class LoggerManager {
	private static boolean debug;	//全体として
	private static boolean debugFramework;	//全体として
	private static List<Logger> frameworkLoggerList;	//フレームワーク用のログ

	static{
		frameworkLoggerList = new ArrayList<Logger>();
		setDebug(true);
		setFrameworkDebug(true);
	}

	public static void setDebug(boolean debug) {
		LoggerManager.debug = debug;
	}

	public static void setFrameworkDebug(boolean debug) {
		LoggerManager.debugFramework = debug;
	}

	/**
	 *
	 * デバッグモードかリリースモードを指定
	 * @param debug デバッグモードか
	 * @param className
	 * @return
	 */
	public static Logger getLogger(boolean debug, String className) {
		Logger logger = new Logger(className);
		logger.setLogMode(debug);
		logger.setLevel(Level.ALL_LOG);

		return logger;
	}

	public static Logger getLogger(String className){
		return getLogger(debug, className);
	}

	/**
	 * @return
	 */
	public static Logger getLogger(){
		return getLogger(debug, "Logger");
	}

	/**
	 * フレームワーク用のデバックするかのフラグを指定したロガーを取得
	 * @param debug
	 * @param className
	 * @return
	 */
	public static Logger getFrameworkLogger(boolean debug, String className) {
		Logger logger = getLogger(debugFramework && debug, className);
		frameworkLoggerList.add(logger);

		return logger;
	}

	/**
	 * フレームワーク用のロガーを取得
	 * @param className
	 * @return
	 */
	public static Logger getFrameworkLogger(String className){
		return getFrameworkLogger(debugFramework, className);
	}
}
