package com.rumisystem.rumisskeyclient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CONFIG{
	private static String  APP_DIR = Main.appContext.getExternalFilesDir(null).getPath();

	public static void main() {
		LOAD_TOKEN();
	}

	private static void LOAD_TOKEN(){
		try{
			File TOKEN_FILE = new File(APP_DIR, "TOKEN");

			if(TOKEN_FILE.exists()){
				FileReader FR = new FileReader(TOKEN_FILE);
				BufferedReader BR = new BufferedReader(FR);

				StringBuilder TOKEN_FILE_CONTENTS = new StringBuilder();

				String CONTENTS_TEMP;
				while ((CONTENTS_TEMP = BR.readLine()) != null) {
					TOKEN_FILE_CONTENTS.append(CONTENTS_TEMP);
				}
				BR.close();

				Main.TOKEN = TOKEN_FILE_CONTENTS.toString();
			}
		} catch (Exception EX){
			EX.printStackTrace();
		}
	}
}