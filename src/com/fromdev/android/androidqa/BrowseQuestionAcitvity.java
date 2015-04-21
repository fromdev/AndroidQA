package com.fromdev.android.androidqa;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.fromdev.adapters.TransitionListAdapter;
import com.fromdev.android.configuration.Global;
import com.fromdev.android.entity.Category;
import com.fromdev.android.entity.Question;
/**
 * @author kamran
 *
 */
public class BrowseQuestionAcitvity extends Activity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private ListView mCategoryListView;
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
		setContentView(R.layout.activity_browsequestion);

		mCategoryListView = (ListView) findViewById(R.id.listView1);
		mHomeButton = (Button) findViewById(R.id.actbrowse_btnhome);
		final ArrayList<Category> mCategories = questionbyCategory(Global
				.getInstance().getQuestionslist());
		TransitionListAdapter mAdapter = new TransitionListAdapter(
				getApplicationContext(), mCategories);

		mCategoryListView.setAdapter(mAdapter);

		mCategoryListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Global.getInstance().setCurrentCategory(
								mCategories.get(position).getName().toString());
						Intent mIntent = new Intent(
								BrowseQuestionAcitvity.this,
								QAScreenActivity.class);
						mIntent.putExtra("intention", "browse");
						startActivity(mIntent);
					}
				});

		mHomeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	// ===========================================================
	// Methods
	// ===========================================================

	public ArrayList<String> getCategories(ArrayList<Question> questionList) {

		ArrayList<String> mCNameList = new ArrayList<>();
		for (int i = 0; i < questionList.size(); i++) {

			if (!mCNameList.contains(questionList.get(i).getCategory()))
				mCNameList.add(questionList.get(i).getCategory());
		}

		return mCNameList;

	}

	public ArrayList<Category> questionbyCategory(
			ArrayList<Question> questionsList) {

		ArrayList<String> mCNameList = getCategories(questionsList);
		ArrayList<Category> mCategories = new ArrayList<>();
		int count = 0;

		for (int j = 0; j < mCNameList.size(); j++) {

			Category mCategory = new Category();
			mCategory.setName(mCNameList.get(j));
			for (int i = 0; i < questionsList.size(); i++) {
				if (questionsList.get(i).getCategory()
						.equalsIgnoreCase(mCNameList.get(j))) {

					count++;
				}

			}
			mCategory.size = count;
			mCategories.add(mCategory);

		}
		return mCategories;

	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
