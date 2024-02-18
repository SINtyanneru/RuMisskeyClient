package com.rumisystem.rumisskeyclient.Activity;

import static com.rumisystem.rumisskeyclient.Main.appContext;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rumisystem.rumisskeyclient.Main;
import com.rumisystem.rumisskeyclient.R;

public class LaunchActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		//タクスバーを削除
		getSupportActionBar().hide();

		setContentView(R.layout.launch_activity);

		appContext = this;

		//アプリのメイン関数を実行
		Main.main();

		//トークンがNull = ログインしてない
		if(Main.TOKEN == null){
			Intent INTENT = new Intent(LaunchActivity.this, LoginActivity.class);
			startActivity(INTENT);
		}
	}
}
