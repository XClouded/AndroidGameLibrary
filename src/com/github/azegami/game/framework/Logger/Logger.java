package com.github.azegami.game.framework.Logger;

public class Logger implements LoggerMode{
	private LoggerMode mode;
	private DebugMode debugMode;
	private ReleaseMode releaseMode;
	private String className;

	public Logger(String className) {
		this.className = className;
	}

	public void setLogMode(boolean isDebug){
		if(isDebug){
			if(debugMode == null)
				debugMode = new DebugMode(className);

			mode = debugMode;
		}else{
			if(releaseMode == null)
				releaseMode = new ReleaseMode();

			mode = releaseMode;
		}
	}

	@Override
	public void Log(String tag, String msg) {
		mode.Log(tag, msg);
	}

	@Override
	public void setLevel(Level level) {
		mode.setLevel(level);
	}

	@Override
	public void debug(String message) {
		mode.debug(message);
	}

	@Override
	public void info(String message) {
		mode.info(message);
	}

	@Override
	public void warn(String message) {
		mode.warn(message);
	}

	@Override
	public void fatal(String message) {
		mode.fatal(message);
	}

	@Override
	public void error(String message) {
		mode.error(message);
	}
}
