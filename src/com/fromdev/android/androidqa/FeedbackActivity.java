package com.fromdev.android.androidqa;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fromdev.android.mail.GMailSender;
import com.fromdev.android.configuration.Global;

/**
 * @author kamran
 *
 */
public class FeedbackActivity extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final String USER_EMAIL = "userEmail";
	// ===========================================================
	// Fields
	// ===========================================================
	private EditText emailEditText;
	private EditText messageEditText;
	private Button feedBackButton;
	private Button homeButton;
	String sender;
	String message;
	String subject;
	Dialog dialog;
	private Editor mEditor;
	private SharedPreferences mPreferences;

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
		setContentView(R.layout.activity_feedbacck);

		emailEditText = (EditText) findViewById(R.id.actfback_txtemail);
		messageEditText = (EditText) findViewById(R.id.actfback_txtmessage);
		feedBackButton = (Button) findViewById(R.id.actfback_btnfeedback);
		homeButton = (Button) findViewById(R.id.actfeedback_btnhome);
		 mPreferences = getSharedPreferences("pref", MODE_PRIVATE);
		String defaultUserEmail = mPreferences.getString(USER_EMAIL, "");
		emailEditText.setText(defaultUserEmail);
		mEditor = mPreferences.edit();
		homeButton.setOnClickListener(new OnClickListener() {
		@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(FeedbackActivity.this,
						MainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);
			}
		});

		feedBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				sender = emailEditText.getText().toString();
				message = messageEditText.getText().toString();
				if (isOnline()) {
					if (isEmailValid(sender)) {
						mEditor.putString(USER_EMAIL, sender);
						mEditor.commit();
						showCustomDialog();
						Send_Email();
					} else {

						emailEditText.setError(Html
								.fromHtml("<font color='black'>"
										+ getResources().getString(
												R.string.enter_valid_email)
										+ "</font>"));
					}
				}else {
					
					Toast mToast = Toast.makeText(getApplicationContext(),
							"Feedback Requires Internet", Toast.LENGTH_SHORT);

					mToast.setGravity(Gravity.CENTER, 0, 0);
					mToast.show();
				}

			}
		});

	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if (msg.what == 100) {

				dialog.dismiss();
				Toast mToast = Toast.makeText(getApplicationContext(),
						R.string.feedback_successful, Toast.LENGTH_SHORT);

				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.show();
				reset();
			}
			else if (msg.what == 200) {

				dialog.dismiss();
				Toast mToast = Toast.makeText(getApplicationContext(),
						"Feeback Sending Failed", Toast.LENGTH_SHORT);

				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.show();
				Log.e("SendMail", msg.toString());
				reset();
			}
		
			
		}

	};

	// ===========================================================
	// Methods
	// ===========================================================

	private void Send_Email() {

		subject = "Feedback";

		Thread mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					message = appendAppInfo(message);
					GMailSender sender = new GMailSender
							//(Global.getInstance().getEmailidString(),Global.getInstance().getPasswordString());
							("qanswer30@gmail.com","123456@78");
					if (sender.sendMail("New Feedback On " + Global.getInstance().getAppName(), message,
							"qanswer30@gmail.com", Global.getInstance().getReceiverEmailString())) {
						mHandler.sendEmptyMessage(100);
					}

					else {
						mHandler.sendEmptyMessage(200);
					}

				} catch (Exception e) {
					Global.getInstance().setLastException(e);
					Log.e("SendMail", e.getMessage(), e);
				}

			}
		});
		mThread.start();

	}

	private boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	private void reset() {
		if(mPreferences!= null) {
			String defaultUserEmail = mPreferences.getString(USER_EMAIL, "");
			emailEditText.setText(defaultUserEmail);
		}
		messageEditText.setText("");
	}
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    return netInfo != null && netInfo.isConnectedOrConnecting();
	}
	protected void showCustomDialog() {

		dialog = new Dialog(FeedbackActivity.this,
				android.R.style.Theme_Translucent);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setCancelable(true);
		dialog.setContentView(R.layout.layout_dialog);

		final ImageView myImage = (ImageView) dialog.findViewById(R.id.loader);
		myImage.startAnimation(AnimationUtils.loadAnimation(
				FeedbackActivity.this, R.anim.rotate));

		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));

		dialog.show();
	}
	private String appendAppInfo(String message) {
		if(message ==null) message = "";
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			message+="\n Sender: " + sender;
			message+="\n App Version: " + pInfo.versionName + "-" + Global.getInstance().getVersion();
			message+="\n AppName: " + Global.getInstance().getAppName();
			message+="\n Debug Info: " + Global.getInstance().getDeviceDebugInfo();
			message+="\n Screen Info: " + Global.getInstance().getScreenInfo(getApplicationContext());
			message+="\n Last Exception: " + Global.getInstance().getLastExceptionAsString();
		} catch (NameNotFoundException e) {
			Global.getInstance().setLastException(e);
			e.printStackTrace();
		}
		return message;
	}
}// ===========================================================
// Inner and Anonymous Classes
// ===========================================================
