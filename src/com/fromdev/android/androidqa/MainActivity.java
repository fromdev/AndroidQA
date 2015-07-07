package com.fromdev.android.androidqa;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import com.fromdev.android.configuration.Constant;
import com.fromdev.android.configuration.Global;
import com.fromdev.android.entity.Question;
import com.fromdev.android.parser.Decompress;
import com.fromdev.android.parser.Parser;
import fromdev.interview.java.R;

/**
 * @author kamran
 *
 */
public class MainActivity extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final String TAG = MainActivity.class.getSimpleName();
	private Button mBrowseQuestionButton;
	private Button mMoreButton;
	private Dialog mDialog;
	private Boolean isFirstTime=false;

	// ===========================================================
	// Fields
	// ===========================================================
	private SharedPreferences mPreferences;
	private Button mQuestionOfDayButton;
	private InputStream inputStream;

	private Handler mHandler;

	public String fileLocation;

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mQuestionOfDayButton = (Button) findViewById(R.id.btn_question_of_day);
		mBrowseQuestionButton = (Button) findViewById(R.id.btn_browse_question);
		mMoreButton = (Button) findViewById(R.id.btn_more);
		mPreferences = getSharedPreferences(Constant.TAG_PREF_FILE_NAME, MODE_PRIVATE);
		fileLocation = getApplicationContext().getFilesDir()
				+ "/appdata/qaconfig.json";
		File mFile = new File(fileLocation);

		showCustomDialog();
		if (!mFile.exists()) {
			Thread mThread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					isFirstTime=true;
					inputStream = getApplicationContext().getResources()
							.openRawResource(R.raw.qaconfig);
					Decompress mDecompress = new Decompress(inputStream,
							MainActivity.this, "appdata");

					
					mDecompress.unzip();

					Parser mParser = new Parser(getApplicationContext(),
							fileLocation);

					Global.getInstance().setQuestionslist(
							mParser.parsingJsonString(mParser.readFile()));

					mHandler.sendEmptyMessage(100);
				}
			});
			mThread.start();

		} 
		mQuestionOfDayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String date = mPreferences.getString("date", "");

				if (date.equalsIgnoreCase(getCurrentDate())) {

					getQuestionOfDaty();
				} else {

					Editor mEditor = mPreferences.edit();
					Question mQuestion = getRandomQuestion();
					if(mQuestion !=null) {
						mEditor.putString("date", getCurrentDate());
						mEditor.putString("id", mQuestion.getId());
						mEditor.putString("category", mQuestion.getCategory());
						mEditor.putString("question", mQuestion.getQuestion());
						mEditor.putString("answer", mQuestion.getAnswer());
						mEditor.commit();
						Global.getInstance().setQuestionofday(mQuestion);
					}
				}
				Intent mIntent = new Intent(MainActivity.this,
						QAScreenActivity.class);
				mIntent.putExtra("intention", "questionofday");
				startActivity(mIntent);
			}
		});

		mBrowseQuestionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(new Intent(MainActivity.this,
						BrowseQuestionAcitvity.class));
			}
		});
		mMoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getApplicationContext(),
						MoreActivity.class));
			}
		});

		mHandler = new Handler(new Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub

				mDialog.dismiss();
				return true;
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (!isFirstTime) {
			Thread mThread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Parser mParser = new Parser(getApplicationContext(),
							fileLocation);
					Global.getInstance().setQuestionslist(
							mParser.parsingJsonString(mParser.readFile()));

					mHandler.sendEmptyMessage(100);
				}
			});
			mThread.start();
		}

	}

	// ===========================================================
	// Methods
	// ===========================================================
	public String getCurrentDate() {

		SimpleDateFormat mDateFormat = new SimpleDateFormat("dd/MM/yyy");
		Calendar mCalendar = Calendar.getInstance();
		return mDateFormat.format(mCalendar.getTime());
	}

	public void getQuestionOfDaty() {

		Question mQuestion = new Question();
		Question randomQuestion = getRandomQuestion();
		if(randomQuestion !=null) {
		mQuestion.setId(mPreferences.getString("id", randomQuestion.getId()));
		mQuestion.setCategory(mPreferences.getString("category",
				randomQuestion.getCategory()));
		mQuestion.setQuestion(mPreferences.getString("question",
				randomQuestion.getQuestion()));
		mQuestion.setAnswer(mPreferences.getString("answer",
				randomQuestion.getAnswer()));
		}
		Global.getInstance().setQuestionofday(mQuestion);
	}

	public Question getRandomQuestion() {
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int questionsSize = Global.getInstance().getQuestionslist()
				.size();
		Question mQuestion = null;
		if(questionsSize > 0) {
			int randomNum = rand.nextInt(questionsSize);
			mQuestion = Global.getInstance().getQuestionslist().get(randomNum);
		}
		return mQuestion;

	}

	// Showing a custom styled dialog and adding actions to the buttons
	protected void showCustomDialog() {

		mDialog = new Dialog(MainActivity.this,
				android.R.style.Theme_Translucent);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.setCancelable(true);
		mDialog.setContentView(R.layout.layout_dialog);

		final ImageView myImage = (ImageView) mDialog.findViewById(R.id.loader);
		myImage.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,
				R.anim.rotate));

		mDialog.getWindow()
				.setBackgroundDrawable(new ColorDrawable(0x7f000000));

		mDialog.show();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
