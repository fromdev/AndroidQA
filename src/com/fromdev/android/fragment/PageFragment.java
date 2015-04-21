package com.fromdev.android.fragment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fromdev.android.androidqa.R;
import com.fromdev.android.entity.Question;
import com.fromdev.android.interfaces.FragmentLifeCycle;
/**
 * @author kamran
 *
 */
public class PageFragment extends Fragment implements FragmentLifeCycle {

	// ===========================================================
	// Constants
	// ===========================================================
	public static final String QUESTION = "Q";
	public static final String ANSWER = "A";
	private float MOVE_THRESHOLD_DP;
	public Boolean isQuestion = true;
	// ===========================================================
	// Fields
	// ===========================================================
	private AnimatorSet leftIn, rightOut, leftOut, rightIn, frontA, backA;

	private WebView mQAWebView;
	private TextView mTapTextView;
	private TextView mHeaderTextview;

	private Boolean onMoveOccur;
	private float mDownPosX;
	private float mDownPosY;
	private String questionString;
	private String answerString;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_qa, container, false);

		LinearLayout mLinearLayout = (LinearLayout) v
				.findViewById(R.id.pagefragement_viewgroup);

		RelativeLayout mLayout = (RelativeLayout) v
				.findViewById(R.id.webviewcontainer);
		mQAWebView = (WebView) v.findViewById(R.id.fragmentqa_txtquestion);

		mTapTextView = (TextView) v.findViewById(R.id.fragmentqa_txttap);
		mHeaderTextview = (TextView) getActivity().findViewById(
				R.id.actqascreen_txtheader);
		// mHeaderTextview.setText(R.string.question);

		initializeAnimation();

		MOVE_THRESHOLD_DP = 20 * getActivity().getResources()
				.getDisplayMetrics().density;
		questionString = getArguments().getString(QUESTION);
		answerString = getArguments().getString(ANSWER);

		rightIn.setTarget(mLayout);
		rightOut.setTarget(mLayout);
		leftIn.setTarget(mLayout);
		leftOut.setTarget(mLayout);
		frontA.playTogether(rightOut, leftIn);
		backA.playTogether(leftOut, rightIn);
		mQAWebView.loadDataWithBaseURL(null, questionString, "text/html",
				"utf-8", null);

		mLinearLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateScreen(questionString, answerString);
			}
		});

		mTapTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateScreen(questionString, answerString);
			}
		});

		mLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateScreen(questionString, answerString);
			}
		});

		mQAWebView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				final int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					Log.d("TAG", "ON Down" + onMoveOccur);
					onMoveOccur = false;
					mDownPosX = event.getX();
					mDownPosY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					Log.d("TAG", "ON UP==" + onMoveOccur);

					if (!onMoveOccur) {
						updateScreen(questionString, answerString);
					}
					break;

				case MotionEvent.ACTION_MOVE:
					if (Math.abs(event.getX() - mDownPosX) > MOVE_THRESHOLD_DP
							|| Math.abs(event.getY() - mDownPosY) > MOVE_THRESHOLD_DP) {
						onMoveOccur = true;
					}
					break;

				default:
					break;
				}

				return false;
			}
		});
		return v;
	}

	@Override
	public void onPauseFragment() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResumeFragment() {
		// TODO Auto-generated method stub
		if (isQuestion) {
			mHeaderTextview.setText(R.string.question);
			mQAWebView.loadDataWithBaseURL(null, questionString, "text/html",
					"utf-8", null);
			mTapTextView.setText(getResources().getString(
					R.string.tap_view_answer));
		} else {
			mQAWebView.loadDataWithBaseURL(null, answerString, "text/html",
					"utf-8", null);
			mTapTextView.setText(getResources().getString(
					R.string.tap_view_question));

		mHeaderTextview.setText(R.string.answer);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public static final PageFragment newInstance(Question aQuestion)

	{
		PageFragment mPageFragment = new PageFragment();

		Bundle mBundle = new Bundle(2);

		mBundle.putString(QUESTION, aQuestion.getQuestion());
		mBundle.putString(ANSWER, aQuestion.getAnswer());

		mPageFragment.setArguments(mBundle);

		return mPageFragment;

	}

	public void initializeAnimation() {

		frontA = new AnimatorSet();
		backA = new AnimatorSet();
		leftOut = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
				R.anim.card_flip_left_out);
		leftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
				R.anim.card_flip_left_in);
		rightIn = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
				R.anim.card_flip_right_in);
		rightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(),
				R.anim.card_flip_right_out);
	}

	public void updateScreen(String questionString, String answerString) {
		if (isQuestion) {

			backA.start();
			mQAWebView.loadDataWithBaseURL(null, answerString, "text/html",
					"utf-8", null);
			mTapTextView.setText(getResources().getString(
					R.string.tap_view_question));

			mHeaderTextview.setText(getResources().getString(R.string.answer));
			isQuestion = false;

		} else {
			frontA.start();
			mQAWebView.loadDataWithBaseURL(null, questionString, "text/html",
					"utf-8", null);
			mTapTextView.setText(getResources().getString(
					R.string.tap_view_answer));
			mHeaderTextview.setText(R.string.question);
			isQuestion = true;
		}
	}
	// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
}
