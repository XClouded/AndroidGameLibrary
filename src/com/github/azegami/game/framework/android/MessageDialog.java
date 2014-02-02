package com.github.azegami.game.framework.android;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;

import com.github.azegami.game.framework.GameLibrary;

public class MessageDialog extends AndroidResources{
	private Builder dlg;
	private boolean start;

	private Handler handler;
	Callback callback;

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public abstract static class Callback{
		public void yes(){}
		public void no(){}
		public void ok(){}
		public void end(){}
	}

	public MessageDialog() {
		reset();
	}

	public void messageViewLayout(final String title, final int layoutId){
		if(isDialogStarted()) return;
		setSelectInit();

		handler.post(new Runnable() {
			@Override
			public void run() {
				Dialog dialog = new Dialog(activity);
				dialog.setContentView(layoutId);

				dialog.setCancelable(false);

				if(title != null){
					dialog.setTitle(title);
				}

				dialog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						dialog = null;
						reset();
					}
				});

				dialog.show();
			}
		});
	}

	/**
	 * ダイアログの終了処理
	 */
	private void dialogFinally(){
		Callback tmpCallback = callback;

		reset();

		if(tmpCallback != null)
			tmpCallback.end();
	}

	//YES, NOの選択ダイアログを作成
	private void messageBoxYesNo(String title, String message){
		if(isDialogStarted()) return;

		dlg = new AlertDialog.Builder(activity)
		.setTitle(title)				//タイトル
		.setMessage(message)	//メッセージ
		.setCancelable(false)		//戻るボタンでキャンセルできないように
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(callback != null){
					callback.yes();
				}

				dialogFinally();
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				if(callback != null){
					callback.no();
				}

				dialogFinally();
			}
		});

		//選択するための初期化
		setSelectInit();

		show();
	}

	public boolean isDialogStarted(){
		return start;
	}

	private void setSelectInit(){
		//モードを選択中に
		//mode = Mode.SELECTING;
		start = true;
	}

	public void reset() {
		dlg = null;
		callback = null;
		start = false;
	}

	public void messageBoxYesNo(String title,
			String message, Callback callback) {
		if(isDialogStarted()) return;
		this.callback = callback;
		messageBoxYesNo(title, message);
	}

	private void alert(String title, String message){
		dlg = new AlertDialog.Builder(activity)
		.setTitle(title)				//タイトル
		.setMessage(message)	//メッセージ
		.setCancelable(false)		//戻るボタンでキャンセルできないように
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(callback != null){
					callback.ok();
				}

				dialogFinally();
				GameLibrary.frameworkLog("MessageDialog", "OK");
			}
		});

		setSelectInit();
		show();
	}

	public void alert(String title,
			String message, Callback callback) {
		if(isDialogStarted()) return;
		this.callback = callback;
		alert(title, message);
	}

	private void show(){
		handler.post(new Runnable() {
			@Override
			public void run() {
				if(dlg != null)
					dlg.show();
			}
		});
	}
}
