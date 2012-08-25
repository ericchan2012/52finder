package com.ds.todolist;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.ds.db.DatabaseAdapter;
import com.ds.finder.R;

public class ViewAllNotes extends Activity implements Constants, OnItemClickListener {
	private Set<HashMap<String, Object>> items = new HashSet<HashMap<String, Object>>();
	private List<HashMap<String, Object>> listMonthItems = new ArrayList<HashMap<String, Object>>();
	private DatabaseAdapter adapter;
	private Cursor mCursor;
	private GridView mGridView;
	private SimpleAdapter saImageItems;
	private View emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_all_notes);

		findviews();
		openDb();
		initData();

	}

	private void openDb() {
		// TODO Auto-generated method stub
		adapter = new DatabaseAdapter(this);
		adapter.open();
	}

	private void initData() {
		// TODO Auto-generated method stub
		mCursor = adapter.queryAll();
		if (mCursor != null && mCursor.getCount() > 0) {
			if (mCursor.moveToFirst()) {
				do {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("ItemImage", R.drawable.ic_launcher);
					map.put("ItemText", mCursor.getString(mCursor.getColumnIndex(NOTE_DATE)).substring(0, 7));//get the month
					items.add(map);
				} while (mCursor.moveToNext());
			}
		}
		Iterator<HashMap<String, Object>> it = items.iterator();
		while(it.hasNext()){
			listMonthItems.add(it.next());
		}
		
	}

	private void findviews() {
		// TODO Auto-generated method stub
		mGridView = (GridView) findViewById(R.id.gridview);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		saImageItems = new SimpleAdapter(this,
				listMonthItems,
				R.layout.gridview_item,

				new String[] { "ItemImage", "ItemText" },

				new int[] { R.id.ItemImage, R.id.ItemText });
		mGridView.setAdapter(saImageItems);
		mGridView.setOnItemClickListener(this);
		
		emptyView = findViewById(R.id.empty);
		mGridView.setEmptyView(emptyView);
	}
	

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		adapter.close();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		//�ㄦ�渚�腑arg2=arg3  
	    @SuppressWarnings("unchecked")
		HashMap<String, Object> item=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);  
	    //�剧ず���Item��temText  
	    String month = (String)item.get("ItemText");
//	    setTitle((String)item.get("ItemText"));  
	    Intent intent = new Intent(this,ViewMonthNotes.class);
	    intent.putExtra(NOTE_MONTH, month);
	    startActivity(intent);
	}

}
