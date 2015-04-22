package com.fromdev.android.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.fromdev.android.configuration.Constant;
import com.fromdev.android.configuration.Global;
import com.fromdev.android.entity.Question;

/**
 * @author kamran
 *
 */
public class Parser {

	// ===========================================================
	// Constants
	// ===========================================================
	public static final String TAG = Parser.class.getSimpleName();
	// ===========================================================
	// Fields
	// ===========================================================
	private Context mContext;
	private ArrayList<Question> mQuestionList;

	// private String filepath;

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	public Parser(Context mContext, String filepath) {
		this.mContext = mContext;
		// this.filepath = filepath;

	}

	// ===========================================================
	// Methods
	// ===========================================================
	public String readFile() {
		StringBuilder jsonBuilder = new StringBuilder();
		try {
			// get the JSON File
			FileReader fileReader = new FileReader(mContext.getFilesDir()
					+ "/appdata/qaconfig.json");
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String lineString;
			while ((lineString = bufferedReader.readLine()) != null) {
				jsonBuilder.append(lineString);

			}

			bufferedReader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonBuilder.toString();

	}

	public ArrayList<Question> parsingJsonString(String jsonString) {
		mQuestionList = new ArrayList<Question>();

		if (jsonString != null) {
			Log.e(TAG, "" + jsonString);
			try {
				// get JSON object
				JSONObject mJsonObject = new JSONObject(jsonString);

				Global.getInstance().setAppName(
						getString(mJsonObject,Constant.TAG_APP_NAME));
				Global.getInstance().setVerion(
						getString(mJsonObject,Constant.TAG_VERSION));
				Global.getInstance().setAboutTitle(
						getString(mJsonObject,Constant.TAG_ABOUT_TITLE));
				Global.getInstance().setAboutDesc(
						getString(mJsonObject,Constant.TAG_ABOUT_DESC));
				Log.e(TAG, "" + Global.getInstance().getAboutDesc());

				Global.getInstance().setEmailidString(
						getString(mJsonObject,Constant.TAG_EMAIL_SENDER));

				Global.getInstance().setPasswordString(
						
								getString(mJsonObject,Constant.TAG_SENDER_EMAIL_PASSWORD));

				Global.getInstance().setReceiverEmailString(
						getString(mJsonObject,Constant.TAG_RECEIVER_EMAIL));
				Log.e(TAG, "" + Global.getInstance().getEmailidString());
				// get JSON array from JSON object
				JSONArray mJsonArray = mJsonObject.getJSONArray("qaList");

				for (int i = 0; i < mJsonArray.length(); i++) {

					JSONObject arrayJsonObject = mJsonArray.getJSONObject(i);
					// create a question object
					Question mQuestion = new Question();
					mQuestion.setId(
							getString(arrayJsonObject,Constant.TAG_JSON_ID));
					mQuestion.setQuestion(
							getString(arrayJsonObject,Constant.TAG_QUESTION));
					mQuestion.setAnswer(
							getString(arrayJsonObject,Constant.TAG_ANSWER));
					mQuestion.setCategory(
							getString(arrayJsonObject,Constant.TAG_CATEGORY));

					mQuestionList.add(mQuestion);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e(TAG, "Error parsing JSON data", e);
			}

		}
		return mQuestionList;

	}
	
	private String getString(JSONObject json, String key) {
		return getString(json,key,"Not Available");
	}
	private String getString(JSONObject json, String key, String defaultValue) {
		String value = defaultValue;
		try {
			value = json.getString(key);
		} catch(Exception e) {
			Log.e(TAG, "Can not find in json doc  key=" + key, e);
		}
		return value;
	}

	public Boolean isJsonStringValid(String jsonString) {

		if (jsonString == null) {
			return false;
		} else {

			// get JSON object
			try {
				JSONObject mJsonObject = new JSONObject(jsonString);
				mJsonObject.getJSONArray("qaList");

			} catch (JSONException e) {
				// TODO Auto-generated catch block

				return false;
			}
		}
		return true;

	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
