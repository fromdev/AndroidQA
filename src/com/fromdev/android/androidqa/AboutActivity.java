package com.fromdev.android.androidqa;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.fromdev.android.configuration.Global;

/**
 * @author kamran
 *
 */
public class AboutActivity extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private TextView mTitleTextView;
	private WebView mDescriptionWebView;
	private TextView mVersionTextView;
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about);

		mTitleTextView = (TextView) findViewById(R.id.activityabout_txttitle);
		mDescriptionWebView = (WebView) findViewById(R.id.activityabout_txtdescription);
		mHomeButton = (Button) findViewById(R.id.actabout_btnhome);
		mVersionTextView = (TextView) findViewById(R.id.activityabout_txtversion);
		mTitleTextView.setText(Global.getInstance().getAboutTitle());
		
		mDescriptionWebView.loadDataWithBaseURL(null, Global.getInstance().getAboutDesc(), "text/html",
				"utf-8", null);
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			mVersionTextView.setText("Version "+pInfo.versionName + "-" + Global.getInstance().getVerion());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mHomeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(AboutActivity.this,
						MainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);
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
