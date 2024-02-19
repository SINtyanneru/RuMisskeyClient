package com.rumisystem.rumisskeyclient;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;

public class HTTP_REQUEST {
	private URL REQIEST_URI = null;

	public HTTP_REQUEST(String INPUT_REQ_URL){
		try{
			REQIEST_URI = new URL(INPUT_REQ_URL);
		}catch (Exception EX) {
			System.err.println(EX);
			System.exit(1);
		}
	}

	//GET
	public String POST(String POST_BODY){
		try{
			System.out.println("[  ***  ]GET:" + REQIEST_URI.toString());
			HttpURLConnection HUC = (HttpURLConnection) REQIEST_URI.openConnection();

			//POSTリクエストだと主張する(MisskeyはPOSTしかない！！便利！！() )
			HUC.setRequestMethod("POST");

			//POST可能に
			HUC.setDoInput(true);
			HUC.setDoOutput(true);

			//ヘッダーを入れる
			HUC.setRequestProperty("Content-Type", "application/json; charset=utf-8");

			//つなぐ
			HUC.connect();

			//リクエストボディに送信したいデータを書き込む
			PrintStream PS = new PrintStream(HUC.getOutputStream());
			PS.print(POST_BODY);
			PS.close();

			//レスポンスコード
			int RES_CODE = HUC.getResponseCode();
			BufferedReader BR = new BufferedReader(new InputStreamReader(HUC.getInputStream(), StandardCharsets.UTF_8));
			StringBuilder RES_STRING = new StringBuilder();

			String INPUT_LINE;
			while ((INPUT_LINE = BR.readLine()) != null){
				RES_STRING.append(INPUT_LINE);
			}

			BR.close();
			System.out.println("[  OK   ]POST");
			return RES_STRING.toString();
		}catch (Exception EX){
			EX.printStackTrace();
			return null;
		}
	}

	//ダウンロード
	public void DOWNLOAD(String PATH){
		try{
			System.out.println("[  ***  ]DOWNLOADING FILE:" + REQIEST_URI.toString());
			HttpURLConnection HUC = (HttpURLConnection) REQIEST_URI.openConnection();

			//GETリクエストだと主張する
			HUC.setRequestMethod("GET");

			//ヘッダーを入れる
			HUC.setRequestProperty("Host", "i.pximg.net");
			HUC.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
			HUC.setRequestProperty("Referer", "https://www.pixiv.net/");

			//レスポンスコード
			int RES_CODE = HUC.getResponseCode();
			if(RES_CODE == HttpURLConnection.HTTP_OK){
				//ファイルを保存する機構
				InputStream IS = HUC.getInputStream();
				FileOutputStream OS = new FileOutputStream(PATH);
				byte[] BUFFER = new byte[4096];
				int BYTES_READ;
				while((BYTES_READ = IS.read(BUFFER)) != -1){
					OS.write(BUFFER, 0, BYTES_READ);
				}
			}

			System.out.println("[  OK   ]DOWNLOADED!");
		}catch (Exception EX){
			EX.printStackTrace();
		}
	}
}
