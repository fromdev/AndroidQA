package com.fromdev.android.androidqa;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;
import com.fromdev.android.configuration.Global;
import com.fromdev.android.entity.Question;
import com.fromdev.android.fragment.PageFragment;
import com.fromdev.android.interfaces.FragmentLifeCycle;
import fromdev.interview.java.R;
/**
 * @author kamran
 *
 */
public class QAScreenActivity extends FragmentActivity implements OnTouchListener,Handler.Callback {
	// ===========================================================
	// Constants
	// ===========================================================
	public static final String TAG = QAScreenActivity.class.getSimpleName();
	// ===========================================================
	// Fields
	// ===========================================================
	private List<PageFragment> mFragments;
	private ViewPager mPager;
	private Button mHomeButton;
	private PagerAdapter mAdapter;
	private final Handler handler = new Handler(this);

	private static final int CLICK_ON_WEBVIEW = 1;
	private static final int CLICK_ON_URL = 2;
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
		setContentView(R.layout.activity_qadisplay);
		Intent mIntent = getIntent();
		String intention = mIntent.getStringExtra("intention");
		if (intention.equalsIgnoreCase("questionofday")) {
			mFragments = new ArrayList<PageFragment>();
			mFragments.add(PageFragment.newInstance(Global.getInstance()
					.getQuestionofday()));
		} else if (intention.equalsIgnoreCase("browse")) {
			mFragments = getFragments();

		}
		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mHomeButton = (Button) findViewById(R.id.actqascreen_btnhome);

		mAdapter = new PagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);

		PageListener pl = new PageListener();
		mPager.setOnPageChangeListener(pl);
	}

	@Override
	public boolean handleMessage(Message msg) {
	    if (msg.what == CLICK_ON_URL){
	        handler.removeMessages(CLICK_ON_WEBVIEW);
	        return true;
	    }
	    if (msg.what == CLICK_ON_WEBVIEW){
	        Toast.makeText(this, "WebView clicked", Toast.LENGTH_SHORT).show();
	        return true;
	    }
	    return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
	    if (v.getId() == R.id.pager && event.getAction() == MotionEvent.ACTION_DOWN){
	        handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500);
	    }
	    return false;
	}
	// ===========================================================
	// Methods
	// ===========================================================

	private List<PageFragment> getFragments() {

		List<PageFragment> fList = new ArrayList<PageFragment>();
		ArrayList<Question> questions = Global.getInstance().getQuestionslist();
		for (int i = 0; i < questions.size(); i++) {

			// make a current category question list
			if (questions
					.get(i)
					.getCategory()
					.equalsIgnoreCase(Global.getInstance().getCurrentCategory()))
				fList.add(PageFragment.newInstance(questions.get(i)));
		}

		return fList;

	}

	// ===========================================================
	// Inner Classes
	// ===========================================================

	private class PagerAdapter extends FragmentPagerAdapter {
		public PagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
			// return ScreenSlidePageFragment.create(position);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}
	}
	
	private class PageListener implements OnPageChangeListener  {
	int currentPosition = 0;

	@Override
	public void onPageSelected(int newPosition) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onpageselected=" + newPosition);
		FragmentLifeCycle fragmentToShow = (FragmentLifeCycle) mAdapter
				.getItem(newPosition);
		fragmentToShow.onResumeFragment();

		FragmentLifeCycle fragmentToHide = (FragmentLifeCycle) mAdapter
				.getItem(currentPosition);
		fragmentToHide.onPauseFragment();

		currentPosition = newPosition;
	}

	@Override
	public void onPageScrolled(int position, float arg1, int arg2) {
		// TODO Auto-generated method stub
		// Log.e(TAG, "onpageselected"+position+"positoinoffset"+arg1);

		if (position == mFragments.size()) {

		}
		// Toast.makeText(getApplicationContext(),
		// "End of Categories"+position, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPageScrollStateChanged(int position) {
		// TODO Auto-generated method stub
		Log.e(TAG, "onPageScrollStateChanged=" + position);
	}
	
	}

}
