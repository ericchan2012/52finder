package com.ds.entity;

import java.util.List;

import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.MKPoiInfo;
import com.ds.finder.R;

public class NearListViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<MKPoiInfo> list;
	private int myLatitude;
	private int myLongitude;

	public NearListViewAdapter(LayoutInflater inflater, List<MKPoiInfo> list,int latitude,int longitude) {
		super();
		this.inflater = inflater;
		this.list = list;
		this.myLatitude = latitude;
		this.myLongitude = longitude;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		if (position < list.size()) {
			return list.get(position);
		} else
			return null;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		MKPoiInfo info = list.get(position);
//		float results[] = new float[2];
//		Location.distanceBetween(myLatitude, myLongitude, info.pt.getLatitudeE6(), info.pt.getLongitudeE6(), results);
//		if(results!=null){
//		Log.i("Near","results.size:" + results[0]);
//		Log.i("Near","results.size2:" + results[1]);
//		}
		// ��ȡ��ǰItem
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.mkinfo_item, parent, false);
		}

		// ���浱ǰItem�пؼ�������, ÿ��View�����������ͨ��setTag��������һ������
		InfoViewHolder holder = (InfoViewHolder) view.getTag();
		if (holder == null) {
			holder = new InfoViewHolder();
			view.setTag(holder);
		}
		
		holder.name =(TextView)view.findViewById(R.id.name);
		holder.addr =(TextView)view.findViewById(R.id.addr);
		

		// ��ǰItem�еĿؼ���ֵ
		if (info != null) {
			holder.name.setText(info.name);
			holder.addr.setText(info.address);
		} else {
//			holder.myFolderName.setText(R.string.no_myfolder_info);
		}
		return view;
	}

	public long getItemId(int position) {
		return position;
	}

}

class InfoViewHolder {
	public TextView name;
	public TextView addr;
}
