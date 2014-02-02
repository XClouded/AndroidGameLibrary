package com.github.azegami.game.framework;

public class StringUtil {
	/**
	 * s1がnullでないならs1をnullならs2を返す
	 * @param s1 nullかチェックする文字列
	 * @param s2 nullの場合返す文字列
	 * @return nullでない文字列
	 */
	public static String nullConvert(String s1, String s2){
		return s1 != null ? s1 : s2;
	}

	/**
	 *
	 * com.example.AのAの部分を取り出す
	 */
	public static String getClassName(String className){
		return className.substring(className.lastIndexOf(".") + 1);
	}
}
