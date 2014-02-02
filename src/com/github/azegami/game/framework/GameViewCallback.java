package com.github.azegami.game.framework;

import android.view.View;

public interface GameViewCallback {
	void setInitFlag(boolean flag);
	void setState(ActivityState state);
	View getView();

	void onResume();
	void onPause();
}
