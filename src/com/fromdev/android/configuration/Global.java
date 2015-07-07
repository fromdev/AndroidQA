package com.fromdev.android.configuration;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.fromdev.android.androidqa.CommonUtil;
import com.fromdev.android.entity.Question;

/**
 * @author kamran
 *
 */
public class Global {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private ArrayList<Question> questionslist;
	private String currentCategory;
	private String aboutTitle;
	private Question questionofday;
	private String aboutDesc;
	private String appName;
	private String version;
	private String emailIdString;
	private String receiverEmailString;
	private String deviceDebugInfo = "Debug-infos:"
     + "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")"
     + "\n OS API Level: "+android.os.Build.VERSION.RELEASE + "("+android.os.Build.VERSION.SDK_INT+")"
     + "\n Device: " + android.os.Build.DEVICE
     + "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";


	public String getReceiverEmailString() {
		return CommonUtil.defaultOnEmpty(receiverEmailString,"pima.support@gmail.com");
	}

	public void setReceiverEmailString(String receiverEmailString) {
		this.receiverEmailString = receiverEmailString;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public Question getQuestionofday() {
		return questionofday;
	}

	public void setQuestionofday(Question questionofday) {
		this.questionofday = questionofday;
	}

	public String getAboutTitle() {
		return aboutTitle;
	}

	public void setAboutTitle(String aboutTitle) {
		this.aboutTitle = aboutTitle;
	}

	public String getAboutDesc() {
		return aboutDesc;
	}

	public void setAboutDesc(String aboutDesc) {
		this.aboutDesc = aboutDesc;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String verion) {
		this.version = verion;
	}

	public String getEmailidString() {
		return CommonUtil.defaultOnEmpty(emailIdString,"pima.support@gmail.com");
	}

	public void setEmailidString(String emailidString) {
		this.emailIdString = emailidString;
	}

	public String getPasswordString() {
		return CommonUtil.defaultOnEmpty(passwordString,"test1234");
	}

	public void setPasswordString(String passwordString) {
		this.passwordString = passwordString;
	}

	private String passwordString;

	public String getCurrentCategory() {
		return currentCategory;
	}

	public void setCurrentCategory(String currentCategory) {
		this.currentCategory = currentCategory;
	}

	public ArrayList<Question> getQuestionslist() {

		return questionslist;
	}

	public void setQuestionslist(ArrayList<Question> questionslist) {
		if(questionslist != null && questionslist.size() > 0) {
			this.questionslist = questionslist;
		} else {
			Log.e("setQuestionslist", "Can not use new questions list. File may be corrupted. Ignoring.");
		}
	}
	
	public String getDeviceDebugInfo() {
		return deviceDebugInfo;
	}
	
	public void showToast(Context c,String msg, int length) {
		Toast mToast = Toast.makeText(c,
				msg, 
				length);

		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	private static Global instance;
	static {

		instance = new Global();
	}

	// ===========================================================
	// Constructors
	// ===========================================================
	private Global() {
	}

	// ===========================================================
	// Methods
	// ===========================================================
	public static Global getInstance() {

		return instance;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
