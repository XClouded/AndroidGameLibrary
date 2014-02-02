package com.github.azegami.game.framework.Logger;

class DebugMode implements LoggerMode{
	Level level;

	private String[] tag;

	private void setTag(String className){
		tag = new String[Level.MAX.getVal()];

		for(int i = 0; i < Level.MAX.getVal();i++){
			Level level = Level.values()[i];

			StringBuilder sb = new StringBuilder();
			sb.append(level.toString());

			sb.append(":");

			if(className != null)
				sb.append(className);

			tag[i] = sb.toString();
		}

	}

	public DebugMode(String className) {
		setTag(className);
		level = Level.ALL_LOG;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	private void log(Level checkLevel, String message){
		if(checkLevel.getVal() >= level.getVal()){
			Log(tag[checkLevel.getVal()], message);
		}
	}

	public void debug(String message){
		Level checkLevel = Level.DEBUG;
		log(checkLevel, message);
	}

	public void info(String message){
		Level checkLevel = Level.INFO;
		log(checkLevel, message);
	}

	public void warn(String message){
		Level checkLevel = Level.WARN;
		log(checkLevel, message);
	}

	public void fatal(String message){
		Level checkLevel = Level.FATAL;
		log(checkLevel, message);
	}

	public void error(String message){
		Level checkLevel = Level.ERROR;
		log(checkLevel, message);
	}

	public void Log(String tag, String msg){
		android.util.Log.d(tag, msg);
	}
}