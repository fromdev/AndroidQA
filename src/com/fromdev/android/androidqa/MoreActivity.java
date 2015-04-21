package com.fromdev.android.androidqa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
		mFeedbackButton = (Button) findViewById(R.id.btn_feedback);
		mAboutButton = (Button) findViewById(R.id.btn_about);
		mUpdatequestionButton = (Button) findViewById(R.id.btn_update_question);
		mHomeButton = (Button) findViewById(R.id.actmore_btnhome);
		mHomeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mFeedbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(MoreActivity.this,
						FeedbackActivity.class));
			}
		});

		mUpdatequestionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MoreActivity.this,
						UpdateQuestionActivity.class));
			}
		});

		mAboutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MoreActivity.this, AboutActivity.class));
			}
		});
	}
	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
