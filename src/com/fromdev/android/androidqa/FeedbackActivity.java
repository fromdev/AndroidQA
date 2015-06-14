package com.fromdev.android.androidqa;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fromdev.android.configuration.Global;
import com.fromdev.android.mail.GMailSender;

/**
 * @author kamran
 *
 */
public class FeedbackActivity extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================

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
				message = appendAppInfo(message);
				if (isOnline()) {
					if (isEmailValid(sender)) {
						showCustomDialog();
						Send_Email();
					} else {

						emailEditText.setError(Html
								.fromHtml("<font color='black'>"
										+ getResources().getString(
												R.string.enter_valid_email)
										+ "</font>"));
					}
				} else {

					Toast mToast = Toast.makeText(getApplicationContext(),
							"Feedback Requires Internet", Toast.LENGTH_SHORT);

					mToast.setGravity(Gravity.CENTER, 0, 0);
					mToast.show();
				}

			}
		});

	}
	private String appendAppInfo(String message) {
		if(message ==null) message = "";
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			message+="\n App Version: " + pInfo.versionName + "-" + Global.getInstance().getVerion();
			message+="\n AppName: " + Global.getInstance().getAppName();
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		 emailEditText.requestFocus();

	        emailEditText.postDelayed(new Runnable() {

	            @Override
	            public void run() {
	                // TODO Auto-generated method stub
	                InputMethodManager keyboard = (InputMethodManager)
	                getSystemService(Context.INPUT_METHOD_SERVICE);
	                keyboard.showSoftInput(emailEditText, 0);
	            }
	        },200);
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
			} else if (msg.what == 200) {

				dialog.dismiss();
				Toast mToast = Toast.makeText(getApplicationContext(),
						"Feeback Sending Failed", Toast.LENGTH_SHORT);

				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.show();
				reset();
			}

		}

	};

	// ===========================================================
	// Methods
	// ===========================================================

	private void Send_Email() {

		subject = "Feeback";

		Thread mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					submitFeedback("test@a.com",message);
					GMailSender sender = new GMailSender(Global.getInstance().getEmailidString(),
							Global.getInstance().getPasswordString());
					if (sender.sendMail(FeedbackActivity.this.sender, message,
							Global.getInstance().getEmailidString(),Global.getInstance().getReceiverEmailString())) {
						mHandler.sendEmptyMessage(100);
					}

					else {
						mHandler.sendEmptyMessage(200);
					}

				} catch (Exception e) {
					Log.e("SendMail", e.getMessage(), e);
				}

			}
		});
		mThread.start();

	}
	
	private void submitFeedback(String fromEmail,String message) {
	    HttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost("https://docs.google.com/spreadsheets/d/17k4k0YjZZ8eitqGvTkiKcax1Xb0kH7oEBycAYZnKIwc");

	    List<BasicNameValuePair> results = new ArrayList<BasicNameValuePair>();
	    results.add(new BasicNameValuePair("entry.1015067069", fromEmail));
	    results.add(new BasicNameValuePair("entry.726134038", message));

	    try {
	        post.setEntity(new UrlEncodedFormEntity(results));
	    } catch (UnsupportedEncodingException e) {
	        // Auto-generated catch block
	        Log.e("YOUR_TAG", "An error has occurred", e);
	    }
	    try {
	        client.execute(post);
	    } catch (ClientProtocolException e) {
	        // Auto-generated catch block
	        Log.e("YOUR_TAG", "client protocol exception", e);
	    } catch (IOException e) {
	        // Auto-generated catch block
	        Log.e("YOUR_TAG", "io exception", e);
	    }
	}

	private boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	private void reset() {

		emailEditText.setText("");
		messageEditText.setText("");
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
}// ===========================================================
// Inner and Anonymous Classes
// ===========================================================
