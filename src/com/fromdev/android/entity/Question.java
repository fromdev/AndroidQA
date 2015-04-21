/**
 * 
 */
package com.fromdev.android.entity;

/**
 * @author kamran
 *
 */
public class Question {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private String question;
	private String answer;
	private String category;
	// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
}
