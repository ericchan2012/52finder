package com.ds.finder;

import java.util.ArrayList;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.ds.entity.NearListViewAdapter;
import com.ds.ui.BounceListView;
import com.ds.util.Constants;
import com.ds.util.Util;

public class ListPoiActivity extends MapActivity implements
		OnItemClickListener, LocationListener {

	Util mUtil;
	private BMapManager mapManager;

	private MKSearch mMKSearch;
	BounceListView mListView;
	private ProgressBar mProgressBar;

	NearSearchListener mNsl;
	ArrayList<MKPoiInfo> mPoiInfoList = new ArrayList<MKPoiInfo>();
	NearListViewAdapter poiInfoAdapter;
	private MKLocationManager mLocationManager = null;

	public View popView;
	private TextView mEmptyTextView;
	int mRange;
	String mKeyword;
	private int mMyLatitude;
	private int mMyLongitude;

	private TextView mShowNumTv;
	private MapView mMapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUtil = Util.getInstance();
		mUtil.requestNoTitle(this);
		setContentView(R.layout.list_poi);
		Bundle bundle = getIntent().getExtras();
		mRange = bundle.getInt(Constants.POI_RANGE);
		mKeyword = bundle.getString(Constants.POI_KEYWORD);
		mPoiInfoList.clear();
		findView();
		initNear();
	}

	private void findView() {
		mListView = (BounceListView) findViewById(R.id.listview);
		mProgressBar = (ProgressBar) findViewById(R.id.poi_bar);
		mEmptyTextView = (TextView) findViewById(R.id.empty);
		mListView.setOnItemClickListener(this);
		mShowNumTv = (TextView) findViewById(R.id.show_total);
		mMapView = (MapView) findViewById(R.id.map_View);
	}

	private void initNear() {
		mapManager = new BMapManager(getApplication());
		mapManager.init(Constants.BAIDU_KEY, null);

		mMKSearch = new MKSearch();

		mMKSearch.init(mapManager, new NearSearchListener());
		super.initMapActivity(mapManager);
		mLocationManager = mapManager.getLocationManager();
		mLocationManager.requestLocationUpdates(this);
		mLocationManager
				.enableProvider((int) MKLocationManager.MK_GPS_PROVIDER);
		mLocationManager
				.enableProvider((int) MKLocationManager.MK_NETWORK_PROVIDER);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ListView listView = (ListView) parent;
		MKPoiInfo info = (MKPoiInfo) listView.getItemAtPosition(position);
		String name = info.name;
		String address = info.address;
		String city = info.city;
		String phonenum = info.phoneNum;
		String postcode = info.postCode;
		int epoitype = info.ePoiType;
		int latitude = info.pt.getLatitudeE6();
		int longitude = info.pt.getLongitudeE6();
		Intent intent = new Intent();
		intent.setClass(this, MarkLocationActivity.class);
		Bundle extras = new Bundle();
		extras.putString(Constants.ADDRESS, address);
		extras.putString(Constants.NAME, name);
		extras.putString(Constants.CITY, city);
		extras.putString(Constants.PHONE_NUM, phonenum);
		extras.putString(Constants.POST_CODE, postcode);
		extras.putInt(Constants.EPOITYPE, epoitype);
		extras.putInt(Constants.PT_LATITUDE, latitude);
		extras.putInt(Constants.PT_LONGITUDE, longitude);
		extras.putInt(Constants.MY_LATITUDE, mMyLatitude);
		extras.putInt(Constants.MY_LONGITUDE, mMyLongitude);
		intent.putExtras(extras);
		startActivity(intent);
		// Toast.makeText(getApplicationContext(),
		// "name��"+name+" address��ֵ��:"+address,
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onDestroy() {
		if (mapManager != null) {
			mapManager.destroy();
			mapManager = null;
		}
		mLocationManager = null;
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mapManager != null) {
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mapManager != null) {
			mapManager.start();
		}
		super.onResume();
	}

	/**
	 * ��λ�÷���仯ʱ�����˷���
	 * 
	 * @param location
	 *            ��ǰλ��
	 */
	public void onLocationChanged(Location location) {
		if (location != null) {
			// ����ǰλ��ת���ɵ�������
			int latitude = (int) (location.getLatitude() * 1000000);
			int longitude = (int) (location.getLongitude() * 1000000);
			GeoPoint pt = new GeoPoint(latitude, longitude);
			mMKSearch.reverseGeocode(pt);
			mMKSearch.poiSearchNearBy(mKeyword, pt, mRange);
			mLocationManager.removeUpdates(this);
			Log.i("Near", "latitude:" + latitude);
			Log.i("Near", "longitude:" + longitude);
			mMyLatitude = latitude;
			mMyLongitude = longitude;
		}
	}

	private static final int FIND_POI = 0;
	private static final int NO_POI = 1;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FIND_POI:
				poiInfoAdapter = new NearListViewAdapter(getLayoutInflater(),
						mPoiInfoList, mMyLatitude, mMyLongitude);
				mListView.setAdapter(poiInfoAdapter);
				mProgressBar.setVisibility(View.GONE);
				mShowNumTv.setText(String.valueOf(mPoiInfoList.size()));
				break;
			case NO_POI:
				mListView.setVisibility(View.GONE);
				mProgressBar.setVisibility(View.GONE);
				mEmptyTextView.setVisibility(View.VISIBLE);
				mEmptyTextView.setText("No Poi");
				break;
			}
		}
	};

	private class NearSearchListener implements MKSearchListener {
		/**
		 * ��ݾ�γ��������ַ��Ϣ���
		 * 
		 * @param result
		 *            �������
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		public void onGetAddrResult(MKAddrInfo result, int iError) {
			Log.i("Near", "iError:" + iError);
			if (result == null) {
				return;
			}
		}

		/**
		 * �ݳ�·���������
		 * 
		 * @param result
		 *            �������
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
				int iError) {
		}

		/**
		 * POI�������Χ����������POI�������ܱ߼�����
		 * 
		 * @param result
		 *            �������
		 * @param type
		 *            ���ؽ�����ͣ�11,12,21:poi�б� 7:�����б?
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			Log.i("NearActivity", "result:" + result);
			if (result == null) {
				mHandler.sendEmptyMessage(NO_POI);
				return;
			} else {
				// for (MKPoiInfo poiInfo : result.getAllPoi()) {
				mPoiInfoList.addAll(result.getAllPoi());
				// }

				if (result.getPageIndex() < result.getNumPages() - 1) {
					mMKSearch.goToPoiPage(result.getPageIndex() + 1);
				} else if (result.getPageIndex() == result.getNumPages() - 1) {
					mHandler.sendEmptyMessage(FIND_POI);
				}
			}
		}

		/**
		 * ��������·���������
		 * 
		 * @param result
		 *            �������
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		public void onGetTransitRouteResult(MKTransitRouteResult result,
				int iError) {
		}

		/**
		 * ����·���������
		 * 
		 * @param result
		 *            �������
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
				int iError) {
		}

		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}
	}
}
