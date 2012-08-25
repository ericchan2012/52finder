package com.ds.todolist;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ds.finder.R;

public class ToDoCursorAdapter extends SimpleCursorAdapter implements Constants {
	private Cursor c;
	private Context context;
	public static ArrayList<Boolean> checkItemList = new ArrayList<Boolean>();
	private boolean mIsMulti;

	public ToDoCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, boolean isMulti) {
		super(context, layout, c, from, to);
		this.c = c;
		this.context = context;
		this.mIsMulti = isMulti;
		init();
	}

	private void init() {
		checkItemList.clear();
		for (int i = 0; i < this.c.getCount(); i++) {
			checkItemList.add(i, false);
		}
	}

	public void update(int position, boolean flag) {
		checkItemList.set(position, flag);
	}

	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.item, null);
		}
		this.c.moveToPosition(pos);
		TextView text1 = (TextView) v.findViewById(R.id.text1);
		CheckBox cb = (CheckBox) v.findViewById(R.id.cb);
		String text = this.c.getString(this.c.getColumnIndex(NOTE_CONTENT));
		int isFinish = this.c.getInt(this.c.getColumnIndex(IS_FINISH));
		if(isFinish == 1){
			text1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}else{
			text1.getPaint().setStrikeThruText(false);
		}
		text1.setText(text);
		if (mIsMulti) {
			cb.setVisibility(View.VISIBLE);
		}
		cb.setChecked(checkItemList.get(pos));
		return v;
	}

}
