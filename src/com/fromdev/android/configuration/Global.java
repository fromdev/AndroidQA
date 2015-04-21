package com.fromdev.android.configuration;

import java.util.ArrayList;

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
	private String verion;
	private String emailIdString;
	private String receiverEmailString;

	public String getReceiverEmailString() {
		return receiverEmailString;
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

	public String getVerion() {
		return verion;
	}

	public void setVerion(String verion) {
		this.verion = verion;
	}

	public String getEmailidString() {
		return emailIdString;
	}

	public void setEmailidString(String emailidString) {
		this.emailIdString = emailidString;
	}

	public String getPasswordString() {
		return passwordString;
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
		this.questionslist = questionslist;
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
