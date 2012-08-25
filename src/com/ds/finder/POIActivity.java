package com.ds.finder;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.ds.util.Constants;
import com.ds.util.Util;

public class POIActivity extends Activity {

	private Util mUtil;
	private int mRange;
	private String mKeyWord;
	private Resources mRes;
	public static final String SETTING = "setting";
	public static final String RANGE = "range";
	private final int SET_RANGE_DIALOG = 1;

	private ArrayList<HashMap<String, Object>> hotPoi = new ArrayList<HashMap<String, Object>>();
	private String[] hotPoiArray;
	private GridView mGridView;
	private SimpleAdapter gridviewadapter;
	private static final String[] PROJECT_FROM = new String[] { "hot_poi" };
	private static final int[] PROJECT_TO = new int[] { R.id.hot_poi };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUtil = Util.getInstance();
		mUtil.requestNoTitle(this);
		mRes = getResources();
		setContentView(R.layout.poi);		
		SharedPreferences mSettings = getSharedPreferences(SETTING, 0);
		mRange = mSettings.getInt(RANGE, 500);
		initHotCities();
		initView();
	}
	
	private void initView(){
		mGridView = (GridView)findViewById(R.id.gridview);

		gridviewadapter = new SimpleAdapter(this, hotPoi,
				R.layout.grid_item, PROJECT_FROM, PROJECT_TO);
		mGridView.setAdapter(gridviewadapter);
		mGridView.setOnItemClickListener(new ItemClickListener());
	}
	
	private class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long id) {
			HashMap<String, Object> item = (HashMap<String, Object>) adapterView.getItemAtPosition(position);
			setRange();
			mKeyWord = (String)item.get("hot_poi");
			sendIntent();
		}
	};
	
	private void initHotCities() {		hotPoi.clear();
		hotPoiArray = mRes.getStringArray(R.array.hotpoi);
		for (int i = 0; i < hotPoiArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("hot_poi", hotPoiArray[i]);
			hotPoi.add(map);
		}
	}

	public void onActionBarSettingButtonClick(View view) {
		showDialog(SET_RANGE_DIALOG);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case SET_RANGE_DIALOG:
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.set_range);
			final ChoiceOnClickListener choiceListener = new ChoiceOnClickListener();
			SharedPreferences setting = getSharedPreferences(SETTING, 500); 
			int range = setting.getInt(RANGE, 500);
			final String[] ranges = mRes.getStringArray(R.array.ranges);
			int checkedItem=0;
			for(int i=0; i< ranges.length; i++){
				if (ranges[i].equals(String.valueOf(range))){
					checkedItem = i;
				}
			}
			builder.setSingleChoiceItems(R.array.ranges, checkedItem, choiceListener);

			DialogInterface.OnClickListener btnListener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialogInterface, int which) {

					int choiceWhich = choiceListener.getWhich();
					String selectedRange = ranges[choiceWhich];
					mRange = Integer.parseInt(selectedRange);
					SharedPreferences mSettings = getSharedPreferences(SETTING, 500); 
					SharedPreferences.Editor editor = mSettings.edit();
					editor.putInt(RANGE, mRange);
					editor.commit();
				}
			};
			builder.setPositiveButton(R.string.confirm, btnListener);
			dialog = builder.create();
			break;
		}
		return dialog;
	}
	
	private class ChoiceOnClickListener implements DialogInterface.OnClickListener {  
		  
        private int which = 0;  
        public void onClick(DialogInterface dialogInterface, int which) {  
            this.which = which;  
        }  
          
        public int getWhich() {  
            return which;  
        }  
    } 

	public void onActionBarSearchButtonClick(View view) {

	}
	
	private void setRange(){
		SharedPreferences mSettings = getSharedPreferences(SETTING, 0);
		mRange = mSettings.getInt(RANGE, 500);
	}

	private void sendIntent() {
		Intent intent = new Intent();
		intent.setAction(Constants.ACTION_POI_ATM);
		intent.setClass(this, ListPoiActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.POI_RANGE, mRange);
		bundle.putString(Constants.POI_KEYWORD, mKeyWord);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
