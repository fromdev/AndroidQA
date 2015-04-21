package com.fromdev.android.androidqa;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.fromdev.android.configuration.Global;
import com.fromdev.android.entity.Question;
import com.fromdev.android.fragment.PageFragment;
import com.fromdev.android.interfaces.FragmentLifeCycle;
/**
 * @author kamran
 *
 */
public class QAScreenActivity extends FragmentActivity {
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

		mPager.setOnPageChangeListener(new OnPageChangeListener() {
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
		});

		mHomeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent(QAScreenActivity.this,
						MainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(mIntent);
			}
		});
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
}
