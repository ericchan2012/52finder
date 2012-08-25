package com.ds.todolist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ds.db.DatabaseAdapter;
import com.ds.finder.R;

public class ViewMonthNotes extends Activity implements Constants,
		OnItemClickListener {
	private String sMonth;
	private DatabaseAdapter adapter;
	private Cursor mCursor;
	private Set<String> items = new HashSet<String>();
	private List<String> listDayItems = new ArrayList<String>();
	private ListView listview;
	private View emptyView;
	@SuppressWarnings("rawtypes")
	private ArrayAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_month_notes);
		sMonth = getIntent().getStringExtra(NOTE_MONTH);
		setTitle(sMonth);
		findViews();
		openDb();
		init();
	}

	private void init() {
		mCursor = adapter.queryWhere(sMonth);
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					items.add(mCursor.getString(mCursor
							.getColumnIndex(NOTE_DATE)));
				} while (mCursor.moveToNext());
			}
		}
		Iterator<String> it = items.iterator();
		while (it.hasNext()) {
			listDayItems.add(it.next());
		}
	}

	private void openDb() {
		adapter = new DatabaseAdapter(this);
		adapter.open();
	}

	private void findViews() {
		listview = (ListView) findViewById(R.id.list);
		emptyView = findViewById(R.id.empty);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		listAdapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, listDayItems);
		listview.setAdapter(listAdapter);
		listview.setOnItemClickListener(this);

		listview.setEmptyView(emptyView);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		adapter.close();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		String date =  (String)parent.getItemAtPosition(position);
		Intent intent = new Intent(this,ViewNoteDetail.class);
		intent.putExtra(NOTE_DATE, date);
		startActivity(intent);
	}

}
