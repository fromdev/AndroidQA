package com.fromdev.android.androidqa;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fromdev.android.configuration.Constant;

/**
 * @author kamran
 *
 */
public class UpdateQuestionActivity extends Activity {

	// ===========================================================
	// Constants
	// ===========================================================
	public static final String TAG = UpdateQuestionActivity.class
			.getSimpleName();

	// ===========================================================
	// Fields
	// ===========================================================
	private TextView mUpdateOnTextView;
	private TextView mUpdateStatusTextView;
	private Button mUpdateButton;
	private Button mHomButton;

	private long downloadReference;
	private DownloadManager mDownloadManager;

	private SharedPreferences mPreferences;
	private Editor mEditor;

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
		setContentView(R.layout.activity_updatequestion);

		mHomButton = (Button) findViewById(R.id.actudateques_btnhome);
		mUpdateOnTextView = (TextView) findViewById(R.id.actudateques_txtlastudateon);
		mUpdateStatusTextView = (TextView) findViewById(R.id.actudateques_txtudatestatusvalue);
		mUpdateButton = (Button) findViewById(R.id.actudateques_btnudate);

		mPreferences = getSharedPreferences("pref", MODE_PRIVATE);
		mEditor = mPreferences.edit();
		mUpdateOnTextView.setText(mPreferences.getString("updateDate",
				"-------"));
		mUpdateStatusTextView.setText(mPreferences.getString("updateStatus",
				"-------"));
		updateButtonStatus();
		// Register to receive messages.
		// We are registering an observer (mMessageReceiver) to receive Intents
		// with actions named "custom-event-name".
		LocalBroadcastManager.getInstance(this).registerReceiver(
				mMessageReceiver, new IntentFilter("ui-update"));
		mHomButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(UpdateQuestionActivity.this,
						MainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);

			}
		});

		mUpdateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isOnline()) {
					// TODO Auto-generated method stub
					mUpdateStatusTextView.setText(R.string.in_progress);
					CommonUtil.updatePreferences(mEditor,getResources().getString(
							R.string.in_progress));
					updateButtonStatus();
					startDownload();
					startservcie();

				} else {

					Toast mToast = Toast.makeText(getApplicationContext(),
							"Requires Network", Toast.LENGTH_SHORT);

					mToast.setGravity(Gravity.CENTER, 0, 0);
					mToast.show();
				}
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Unregister since the activity is about to be closed.
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mMessageReceiver);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void updateButtonStatus() {
		String status = mUpdateStatusTextView.getText().toString();
		if (status.equalsIgnoreCase(getResources().getString(R.string.failed))
				|| status.equalsIgnoreCase(getResources().getString(
						R.string.success))) {
			mUpdateButton.setEnabled(true);
		}
		if (status.equalsIgnoreCase(getResources().getString(
				R.string.in_progress))) {

			mUpdateButton.setEnabled(false);
		}

	}

	/*
	 * Register Download Broadcast Receiver
	 */

	public void startservcie() {

		Intent mIntent = new Intent(this, UpdateService.class);
		mIntent.putExtra("downloadreference", downloadReference);
		startService(mIntent);
	}

	public void startDownload() {

		mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		Uri Download_Uri = Uri.parse(CommonUtil.defaultOnEmpty(getResources().getString(R.string.updateUrl), Constant.URL));
		
		DownloadManager.Request request = new DownloadManager.Request(
				Download_Uri);

		// Restrict the types of networks over which this download may
		// proceed.
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
				| DownloadManager.Request.NETWORK_MOBILE);
		// Set whether this download may proceed over a roaming
		// connection.
		request.setAllowedOverRoaming(false);
		// Set the title of this download, to be displayed in
		// notifications (if enabled).
		request.setTitle("Question Answer Updates");
		// Set a description of this download, to be displayed in
		// notifications (if enabled)
		request.setDescription("");
		// Set the local destination for the downloaded file to a path
		// within the application's external files directory
		// request.setDestinationInExternalFilesDir(this,Environment.DIRECTORY_DOWNLOADS,"CountryList.json");

		// Enqueue a new download and same the referenceId
		downloadReference = mDownloadManager.enqueue(request);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	// Our handler for received Intents. This will be called whenever an Intent
	// with an action named "custom-event-name" is broadcasted.
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Get extra data included in the Intent
			String message = intent.getStringExtra("state");
			mUpdateStatusTextView.setText(message);
			updateButtonStatus();
			
			Log.e("receiver", "Got message: " + message);
		}
	};
}
