package com.rumisystem.rumisskeyclient;

import android.app.AlertDialog;
import android.content.Context;

public class LIB {
	public static void MESSAGE_BOX_SHOW(Context appContext, String TITLE, String TEXT){
		new AlertDialog.Builder(appContext)
				.setTitle(TITLE)
				.setMessage(TEXT)
				.setPositiveButton("おｋ", null)
				.show();
	}
}
