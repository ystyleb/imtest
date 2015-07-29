package util;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DataHelper {
	public static String USER_ID = "";
	 // 是否打开扬声器
    public static boolean getSettingMsgSpeaker = true;

	public static void putSettings(Context context, String key,
			Serializable content) {
		try {
			String encrypt = ObjectSerializer.serialize(content);
			// encrypt = AES.encrypt("1234567891234567", encrypt);
			putString(context, key, encrypt);
		} catch (Exception e) {

		}
	}

	private static void putString(Context context, String key, String value) {
		try {
//			Settings.System.putString(context.getContentResolver(), key, value);
			 SharedPreferences shared = context.getSharedPreferences(
			 key, Activity.MODE_PRIVATE);
			 Editor editor = shared.edit();
			 editor.putString(key, value);
			 editor.commit();
		} catch (Exception e) {

		}
	}

	public static Object getSettings(Context context, String key) {
		try {
			String content = getString(context, key);
			// content = AES.decrypt("1234567891234567", content);
			return ObjectSerializer.deserialize(content);
		} catch (Exception e) {

		}
		return null;
	}

	private static String getString(Context context, String key) {
		try {
//			return Settings.System.getString(context.getContentResolver(), key);
			 SharedPreferences shared = context.getSharedPreferences(key,
			 Activity.MODE_PRIVATE);
			 String value = shared.getString(key, "");
			 return value;
		} catch (Exception e) {

		}
		return "";
	}
}
