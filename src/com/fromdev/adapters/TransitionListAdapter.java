package com.fromdev.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import fromdev.interview.java.R;
import com.fromdev.android.entity.Category;
/**
 * @author kamran
 *
 */
public class TransitionListAdapter extends BaseAdapter {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
	private ViewHolder viewHolder;

	private ArrayList<Category> mCategories;
	private Context mContext;

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================
	public TransitionListAdapter(Context mContext,
			ArrayList<Category> mCategories) {
		this.mContext = mContext;
		this.mCategories = mCategories;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public int getCount() {
		return mCategories.size();
	}

	@Override
	public Object getItem(int position) {
		return mCategories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (convertView == null) {

			// inflate the layout
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.fragment_list_item_transition, null);

			// well set up the ViewHolder
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) v.findViewById(R.id.item_title);
			viewHolder.descr = (TextView) v.findViewById(R.id.item_description);

			// store the holder with the view.
			v.setTag(viewHolder);

		} else {
			// just use the viewHolder
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.title.setText(mCategories.get(position).getName());
		viewHolder.descr.setText("" + mCategories.get(position).getSize());

		return v;
	}

	static class ViewHolder {
		TextView title;
		TextView descr;

	}
	// ===========================================================
	// Methods
	// ===========================================================
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}