package com.rumisystem.rumisskeyclient.Activity;

import static com.rumisystem.rumisskeyclient.CONFIG.APP_DIR;
import static com.rumisystem.rumisskeyclient.LIB.MESSAGE_BOX_SHOW;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.rumisystem.rumisskeyclient.HTTP_REQUEST;
import com.rumisystem.rumisskeyclient.Main;
import com.rumisystem.rumisskeyclient.R;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		//タクスバーを削除
		getSupportActionBar().hide();

		setContentView(R.layout.login_activity);

		//ユーザーID入力欄
		EditText UID_INPUT = findViewById(R.id.UID_INPUT);
		//パスワード入力欄
		EditText PASS_INPUT = findViewById(R.id.PASS_INPUT);

		//ログインボタン
		Button LOGIN_BUTTON = findViewById(R.id.LOGIN_BUTTON);
		LOGIN_BUTTON.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Androidの仕様で、HTTPは別スレッドで実行する必要がある(ただしUIの変更は別スレッドから出来ないので頭おかしい)
				new Thread(new Runnable() {
					@Override
					public void run() {
						try{
							//ボタンを推せなくする
							LOGIN_BUTTON.setActivated(false);

							//ログイン情報を作る
							HashMap<String, String> LOGIN_DATA = new HashMap<>();
							LOGIN_DATA.put("username", UID_INPUT.getText().toString());
							//まだパスワード入れない

							//OMの準備
							ObjectMapper OM = new ObjectMapper();

							//ログイン処理を実行
							HTTP_REQUEST AJAX = new HTTP_REQUEST("https://" + Main.SERVER_DOMAIN + "/api/users/show");
							String RESULT = AJAX.POST(OM.writeValueAsString(LOGIN_DATA));

							if(RESULT != null){
								JsonNode USER_INFO = OM.readTree(RESULT);

								//パスワードをセット
								LOGIN_DATA.put("password", PASS_INPUT.getText().toString());

								//二段階革命論が有効か？
								if(USER_INFO.get("twoFactorEnabled").asBoolean()){
									//入力欄
									AppCompatEditText DUB_FACTOR_INPUT = new AppCompatEditText(LoginActivity.this);

									//ダイアログを表示
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											new AlertDialog.Builder(LoginActivity.this)
													.setTitle("二段階革命論コードをここに")
													.setMessage("書いてください")
													.setView(DUB_FACTOR_INPUT)
													.setPositiveButton("OK", new DialogInterface.OnClickListener() {
														@Override
														public void onClick(DialogInterface dialog, int which) {
															//二段階革命論コードをセット
															LOGIN_DATA.put("token", DUB_FACTOR_INPUT.getText().toString());

															//メインのスレッドに戻ったのでまたスレッドを作る
															new Thread(new Runnable() {

																@Override
																public void run() {
																	LOGIN(LOGIN_DATA);
																}
															}).start();
														}
													}).create().show();
										}
									});
								} else {
									//有効じゃない
									LOGIN(LOGIN_DATA);
								}
							} else {
								//エラー
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										MESSAGE_BOX_SHOW(LoginActivity.this, "応答", "内部エラー");
									}
								});
							}
						} catch (Exception EX){
							EX.printStackTrace();
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									MESSAGE_BOX_SHOW(LoginActivity.this, "エラー", EX.getMessage());
								}
							});
						}
					}
				}).start();
			}
		});
	}

	//ログイン処理を担当(これはスレッド作成後に実行する前提)
	private void LOGIN(HashMap<String, String> LOGIN_DATA){
		try{
			//OMの準備
			ObjectMapper OM = new ObjectMapper();

			//ログイン処理を実行
			HTTP_REQUEST AJAX = new HTTP_REQUEST("https://" + Main.SERVER_DOMAIN + "/api/signin");
			String RESULT = AJAX.POST(OM.writeValueAsString(LOGIN_DATA));

			if(RESULT != null){
				JsonNode LOGIN_INFO = OM.readTree(RESULT);

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try{
							//書き込む内容
							HashMap<String, String> TOKEN_DATA = new HashMap<>();
							TOKEN_DATA.put("TOKEN", LOGIN_INFO.get("i").textValue());
							TOKEN_DATA.put("DOMAIN", Main.SERVER_DOMAIN);

							File TOKEN_FILE = new File(APP_DIR, "TOKEN.json");
							if(!TOKEN_FILE.exists()){
								if(TOKEN_FILE.createNewFile()) {
									//書き込む
									BufferedWriter WRITER = new BufferedWriter(new FileWriter(TOKEN_FILE));

									//MAPをString化
									ObjectMapper OM = new ObjectMapper();
									String RES = OM.writeValueAsString(TOKEN_DATA);

									//書き込み
									WRITER.write(RES);

									//メモリ開放
									WRITER.close();

									//ログインが完了したので再起動
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											System.exit(0);
										}
									});
								} else {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											MESSAGE_BOX_SHOW(LoginActivity.this, "エラー", "トークンファイルが作れません");
										}
									});
								}
							} else {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										MESSAGE_BOX_SHOW(LoginActivity.this, "エラー", "すでにログインしてるっぽいぞ？");
									}
								});
							}
						} catch (Exception EX){
							EX.printStackTrace();
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									MESSAGE_BOX_SHOW(LoginActivity.this, "エラー", EX.getMessage());
								}
							});
						}
					}
				});
			} else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						MESSAGE_BOX_SHOW(LoginActivity.this, "エラー", "ログインできませんでした、死んでください");
					}
				});
			}
		} catch (Exception EX){
			EX.printStackTrace();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					MESSAGE_BOX_SHOW(LoginActivity.this, "エラー", EX.getMessage());
				}
			});
		}
	}
}