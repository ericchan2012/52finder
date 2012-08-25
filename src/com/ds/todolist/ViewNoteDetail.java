package com.ds.todolist;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ds.db.DatabaseAdapter;
import com.ds.finder.R;

public class ViewNoteDetail extends Activity implements Constants{
	private LinearLayout views;
	private DatabaseAdapter dbAdapter;
	private Cursor mCursor;
	private String mDate;
	private LayoutInflater mInflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_note_detail);
		mDate = getIntent().getStringExtra(NOTE_DATE);
		setTitle(mDate);
		findViews();
		openDb();
		mInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
	}

	private void openDb() {
		// TODO Auto-generated method stub
		dbAdapter = new DatabaseAdapter(this);
		dbAdapter.open();
	}
	
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mCursor = dbAdapter.queryWhereDate(mDate);
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					String content = mCursor.getString(mCursor.getColumnIndex(NOTE_CONTENT));
					int isFinish = mCursor.getInt(mCursor.getColumnIndex(IS_FINISH));
					addViews(content,isFinish);
				} while (mCursor.moveToNext());
			}
		}
	}

	private void addViews(String content, int isFinish) {
		// TODO Auto-generated method stub
		View itemView = mInflater.inflate(R.layout.detail_item, null);
		TextView text1 =(TextView) itemView.findViewById(R.id.text1);
		TextView text2 = (TextView) itemView.findViewById(R.id.text2);
		
		if(isFinish == 1){
			text2.setText(getResources().getString(R.string.yes));
			text1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}else{
			text2.setText(getResources().getString(R.string.no));
		}
		
		text1.setText(content);
		
		views.addView(itemView);
	}

	private void findViews() {
		// TODO Auto-generated method stub
		views = (LinearLayout)findViewById(R.id.list);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbAdapter.close();
	}
	
	

}
