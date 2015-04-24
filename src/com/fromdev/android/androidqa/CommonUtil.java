package com.fromdev.android.androidqa;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.SharedPreferences.Editor;
import android.util.Log;
/**
 * 
 * @author sjoshi
 *
 */
public class CommonUtil {
	private final static String TAG = CommonUtil.class.getSimpleName();


	public static void updatePreferences(Editor mEditor, String status) {
		SimpleDateFormat mDateFormat = new SimpleDateFormat(
				"dd/MM/yyy");
		Calendar mCalendar = Calendar.getInstance();
		String date = mDateFormat.format(mCalendar.getTime());
		mEditor.putString("updateDate", date);
		mEditor.putString("updateStatus", status);
		mEditor.commit();
		Log.e(TAG, "date=" + date + "status =" + status);
	}
	
	public static String defaultOnEmpty(String value,String defaultVal) {
		if(value==null || "".equals(value)) {
			return defaultVal;
		}
		return value;
	}
}
