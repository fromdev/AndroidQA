package com.fromdev.android.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
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
	private ArrayList<Question> questionslist = new ArrayList<Question>();
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

	private Throwable lastException = null;
	
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
	
	public String getScreenInfo(Context context) {
		int measuredWidth = 0;
		int measuredHeight = 0;
		WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
		    Point size = new Point();
		    w.getDefaultDisplay().getSize(size);
		    measuredWidth = size.x;
		    measuredHeight = size.y;
		} else {
		    Display d = w.getDefaultDisplay();
		    measuredWidth = d.getWidth();
		    measuredHeight = d.getHeight();
		}
		return "Screen Width: " + measuredWidth + " Screen Height:" + measuredHeight;
	}
	public void showToast(Context c,String msg, int length) {
		Toast mToast = Toast.makeText(c,
				msg, 
				length);

		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}	
	
	public void setLastException(Throwable t) {
		this.lastException = t;
	}

	public String getLastExceptionAsString() {
		String trace = "";
		if(this.lastException != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			this.lastException.printStackTrace(pw);
			trace = sw.toString();
		}
		return trace;
	}

	public static Long getRemoteFileSize(String url) {
		Long contentLength = -1l;
		HttpURLConnection ucon = null;
		try {
			final URL uri = new URL(url);
			ucon = (HttpURLConnection) uri.openConnection();
			ucon.connect();
			String contentLengthStr = ucon.getHeaderField("content-length");
			contentLength = Long.parseLong(contentLengthStr);
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} finally {
			if (ucon != null) {
				try {
					InputStream in = ucon.getInputStream();
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ucon.disconnect();
			}
		}
		return contentLength;
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
