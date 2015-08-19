package com.fromdev.android.androidqa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import fromdev.interview.java.R;

/**
 * @author kamran
 *
 */
public class MoreActivity extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private Button mFeedbackButton;
	private Button mAboutButton;
	private Button mUpdatequestionButton;
	private Button mHomeButton;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		registerHomeActions();
		registerFeedbackActions();
		registerUpdateActions();
		registerAboutActions();
	}
	// ===========================================================
	// Methods
	// ===========================================================

	private void registerButtonForAction(Button button, final Context context, final Class clazz) {
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, clazz));
			}
		});
	}
	private void registerHomeActions() {
		mHomeButton = (Button) findViewById(R.id.actmore_btnhome);
		mHomeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void registerAboutActions() {
		mAboutButton = (Button) findViewById(R.id.btn_about);
		registerButtonForAction(mAboutButton,MoreActivity.this, AboutActivity.class);
	}

	private void registerUpdateActions() {
		mUpdatequestionButton = (Button) findViewById(R.id.btn_update_question);
		registerButtonForAction(mUpdatequestionButton,MoreActivity.this, UpdateQuestionActivity.class);
	}

	private void registerFeedbackActions() {
//		mFeedbackButton = (Button) findViewById(R.id.btn_feedback);
//		registerButtonForAction(mAboutButton,MoreActivity.this, FeedbackActivity.class);
	}
}
