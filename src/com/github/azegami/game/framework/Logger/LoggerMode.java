package com.github.azegami.game.framework.Logger;

//ロガーの反応用インターフェイス
interface LoggerMode{
	public static enum Level{
		DEBUG(0),
		INFO(1),
		WARN(2),
		ERROR(3),
		FATAL(4),
		MAX(5),
		NO_LOG(-1), //表示しない
		ALL_LOG(0);	//全部表示

		private int val;

		Level(int val){
			this.val = val;
		}

		public int getVal() {
			return val;
		}
	};

	public abstract void Log(String tag, String msg);
	public abstract void setLevel(Level level);

	public abstract void debug(String message);
	public abstract void info(String message);
	public abstract void warn(String message);
	public abstract void fatal(String message);
	public abstract void error(String message);
}