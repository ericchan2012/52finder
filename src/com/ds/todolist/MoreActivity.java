package com.ds.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ds.finder.R;

public class MoreActivity extends Activity implements OnItemClickListener {

	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moreitems);
		String[] texts = { getResources().getString(R.string.menu_settings),
				getResources().getString(R.string.weibo_account_manage),
				getResources().getString(R.string.weibo_readmode),
				getResources().getString(R.string.skin_list),
				getResources().getString(R.string.weibo_officialweibo),
				getResources().getString(R.string.weibo_feedback),
				getResources().getString(R.string.weibo_check_update),
				getResources().getString(R.string.weibo_about),
				getResources().getString(R.string.back) };
		int[] resIds = { R.drawable.moreitems_setting_icon,
				R.drawable.moreitems_accountmanage_icon,
				R.drawable.readmode_icon, R.drawable.skin_def_icon,
				R.drawable.moreitems_officialweibo_icon,
				R.drawable.moreitems_feedback_icon,
				R.drawable.moreitems_version, R.drawable.aboutweibo,
				R.drawable.aboutweibo };
		listView = (ListView) findViewById(R.id.moreItemsListView);
		listView.setAdapter(new ListViewAdapter(texts, resIds));
		listView.setDivider(getResources().getDrawable(
				R.drawable.moreitem_bg_line));
		listView.setOnItemClickListener(this);
	}

	public class ListViewAdapter extends BaseAdapter {
		View[] itemViews;

		public ListViewAdapter(String[] itemTexts, int[] itemImageRes) {
			itemViews = new View[itemTexts.length];

			for (int i = 0; i < itemViews.length; i++) {
				itemViews[i] = makeItemView(itemTexts[i], itemImageRes[i]);
			}
		}

		public int getCount() {
			return itemViews.length;
		}

		public View getItem(int position) {
			return itemViews[position];
		}

		public long getItemId(int position) {
			return position;
		}

		private View makeItemView(String strText, int resId) {
			LayoutInflater inflater = (LayoutInflater) MoreActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View itemView = inflater.inflate(R.layout.moreitemsview, null);
			TextView text = (TextView) itemView.findViewById(R.id.TextView01);
			text.setText(strText);
			ImageView image = (ImageView) itemView
					.findViewById(R.id.ImageView01);
			image.setImageResource(resId);

			ImageView image2 = (ImageView) itemView
					.findViewById(R.id.ImageView02);
			image2.setImageResource(R.drawable.triangle);
			return itemView;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (position == 0) {
				itemViews[position]
						.setBackgroundResource(R.drawable.list_above_background);
			} else if (position == (itemViews.length - 1)) {
				itemViews[position]
						.setBackgroundResource(R.drawable.list_below_background);
			} else {
				itemViews[position]
						.setBackgroundResource(R.drawable.list_mid_background);
			}
			if (convertView == null)
				return itemViews[position];
			return convertView;
		}
		
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
		switch(position){
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			Intent intent = new Intent(this,AboutActivity.class);
			startActivity(intent);
			break;
		case 8:
			finish();
			break;
		}
	}

}
