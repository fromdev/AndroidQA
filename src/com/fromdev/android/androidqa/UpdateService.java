package com.fromdev.android.androidqa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.fromdev.android.configuration.Global;
import com.fromdev.android.parser.Decompress;
import com.fromdev.android.parser.Parser;
import fromdev.interview.java.R;

/**
 * @author kamran
 *
 */
public class UpdateService extends Service {
	// ===========================================================
	// Constants
	// ===========================================================
	private final static String TAG = UpdateService.class.getSimpleName();

	// ===========================================================
	// Fields
	// ===========================================================
	private long downloadReference;
	private DownloadManager mDownloadManager;
	private String filepath;
	private Editor mEditor;
	private SharedPreferences mPreferences;

	public String temp_filepath;
	public String perm_filepath;

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
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		temp_filepath = getApplicationContext().getFilesDir()
				+ "/tempdata/qaconfig.json";
		perm_filepath = getApplicationContext().getFilesDir()
				+ "/appdata/qaconfig.json";

		mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		mPreferences = getSharedPreferences("pref", MODE_PRIVATE);
		mEditor = mPreferences.edit();
		regDownloadBR();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		if (intent != null) {
			downloadReference = intent.getLongExtra("downloadreference", 0);
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(downloadBroadcastReceiver);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private Boolean deleteFile() {
		try {
			// delete the original file
			File mFile = new File(temp_filepath);

			return mFile.delete();
		} catch (Exception e) {
			Global.getInstance().setLastException(e);
			Log.e("tag", e.getMessage(),e);
			return false;
		}
	}

	public void regDownloadBR() {

		IntentFilter mIntentFilter = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downloadBroadcastReceiver, mIntentFilter);
	}

	/**
	 * @param inputPath
	 * @param outputPath
	 */
	private boolean moveFile(String inputPath, String outputPath) {
		boolean status = false;
		InputStream mInputStream = null;
		OutputStream mOutputStream = null;
		try {

			 mInputStream = new FileInputStream(inputPath);
			 mOutputStream = new FileOutputStream(outputPath);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = mInputStream.read(buffer)) != -1) {
				mOutputStream.write(buffer, 0, read);
			}

			status = true;
		}

		catch (FileNotFoundException e) {
			Global.getInstance().setLastException(e);
			Log.e(TAG, e.getMessage(),e);
		} catch (IOException e) {
			Global.getInstance().setLastException(e);
			e.printStackTrace();
		} finally {
			try {
				mInputStream.close();
			} catch (Exception e) {
				Global.getInstance().setLastException(e);
				e.printStackTrace();
			}
			mInputStream = null;

			// closing streames
			try {
				mOutputStream.flush();
			} catch (Exception e) {
				Global.getInstance().setLastException(e);
				e.printStackTrace();
			}
			try {
				mOutputStream.close();
			} catch (IOException e) {
				Global.getInstance().setLastException(e);
				e.printStackTrace();
			}
			mOutputStream = null;
		}
		return status;
	}

	private BroadcastReceiver downloadBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// check if the broadcast message is for our Enqueued download

			Log.e(TAG, "Download Broadcast on Receive Method");
			long referenceId = intent.getLongExtra(
					DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			if (downloadReference == referenceId) {
				// do work after download here

				DownloadManager.Query query = new DownloadManager.Query();
				query.setFilterById(downloadReference);
				Cursor cursor = mDownloadManager.query(query);
				cursor.moveToFirst();

				int status = cursor.getInt(cursor
						.getColumnIndex(DownloadManager.COLUMN_STATUS));
				@SuppressWarnings("unused")
				int reason = cursor.getInt(cursor
						.getColumnIndex(DownloadManager.COLUMN_REASON));
				filepath = cursor.getString(cursor
						.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));

				Log.e(TAG, "Download Result:" + status);
				switch (status) {
				case DownloadManager.STATUS_SUCCESSFUL:
					Log.d(TAG, "Successful download Start Thread");
					mThread.start();
					break;
				case DownloadManager.STATUS_FAILED:
					Log.d(TAG, "FAILED DOWNLOAD");
					CommonUtil.updatePreferences(mEditor,getResources().getString(R.string.failed));
					break;

				default:
					break;
				}
			}
		}
	};

	Thread mThread = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			File mFile = new File(filepath);
			try {
				InputStream mInputStream = new FileInputStream(mFile);
				Decompress mDecompress = new Decompress(mInputStream,
						getBaseContext(), "tempdata");
				mDecompress.unzip();

				Parser mParser = new Parser(getApplicationContext(),
						temp_filepath);

				// if JSON file is valid
				if (mParser.isJsonStringValid(mParser.readFile())) {

					Log.e(TAG, "JSON VALID");
					boolean moveStatus = moveFile(temp_filepath, perm_filepath);
					// delete the old file
					if(moveStatus) {
						deleteFile();
					}
					udpateStatus(getResources().getString(R.string.success));
				} else {
					udpateStatus(getResources().getString(R.string.failed));
				}
			} catch (FileNotFoundException e) {
				udpateStatus(getResources().getString(R.string.failed));
				e.printStackTrace();
				Log.e(TAG, e.getMessage(), e);
			} finally {
				stopSelf();
			}
		}

		private void udpateStatus(String status) {
			// updating system preferences
			CommonUtil.updatePreferences(mEditor,
					status);
			// sending a local broadcast to update UI
			SendLocalBroadCast(status);
		}
	});

	/*
	 * sending Local Broadcast for updating Update Status
	 */
	public void SendLocalBroadCast(String Message) {

		Log.e(TAG, "Sending Broadcast=" + Message);
		Intent mIntent = new Intent("ui-update");
		mIntent.putExtra("state", Message);
		LocalBroadcastManager.getInstance(getApplicationContext())
				.sendBroadcast(mIntent);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
