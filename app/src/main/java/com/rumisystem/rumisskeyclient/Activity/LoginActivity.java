package com.rumisystem.rumisskeyclient.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.rumisskeyclient.R;

public class LoginActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		//タクスバーを削除
		getSupportActionBar().hide();

		setContentView(R.layout.login_activity);
	}
}
