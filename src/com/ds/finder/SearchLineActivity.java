package com.ds.finder;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKRoutePlan;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRoutePlan;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.RouteOverlay;
import com.baidu.mapapi.TransitOverlay;
import com.ds.util.Constants;
import com.ds.util.Util;

public class SearchLineActivity extends MapActivity {

	private Intent mIntent;
	private int mStartLatitude;
	private int mEndLatitude;
	private int mStartLongitude;
	private int mEndLongitude;
	private int mType;
	private BMapManager mapManager;
	private MKSearch mMKSearch;
	private MapView mMapView;
	private MapController mMapController;

	private RelativeLayout mMapRel;
//	private LinearLayout mConViewLayout;
	private ImageView mHeaderImage;
	private TextView mHeaderTitle;
	private TextView mHeaderHint;
	private LinearLayout mLoading;
	private LinearLayout mLoadingError;
	private ListView mListView;
	private LinearLayout mContentLoad;

	private ArrayList<HashMap<String, String>> busList = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter mAdapter;
	
	private String mStart;
	private String mEnd;
	private Util mUtil;
	private String mCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUtil = Util.getInstance();
		mUtil.requestNoTitle(this);
		setContentView(R.layout.search_line_layout);
		mIntent = getIntent();
		Bundle extras = mIntent.getExtras();
		mType = extras.getInt(Constants.TYPE);
		mStartLatitude = extras.getInt(Constants.START_LATITUDE);
		mEndLatitude = extras.getInt(Constants.END_LATITUDE);
		mStartLongitude = extras.getInt(Constants.START_LONGITUDE);
		mEndLongitude = extras.getInt(Constants.END_LONGITUDE);
		mStart = extras.getString(Constants.START);
		mEnd = extras.getString(Constants.END);
		mCity = extras.getString(Constants.CITY);
		findView();
		initView();
	}

	private void findView() {
		mMapRel = (RelativeLayout) findViewById(R.id.map_view);
//		mConViewLayout = (LinearLayout) findViewById(R.id.content_view);
		mHeaderImage = (ImageView) findViewById(R.id.header_change_img);
		mHeaderTitle = (TextView) findViewById(R.id.header_title_tv);
		mHeaderHint = (TextView) findViewById(R.id.header_hint_tv);
		mLoading = (LinearLayout) findViewById(R.id.base_loading);
		mLoadingError = (LinearLayout) findViewById(R.id.base_loading_err);
		mLoadingError.setVisibility(View.GONE);
		mListView = (ListView) findViewById(R.id.main);
		mHeaderTitle.setText(mStart + "To" + mEnd);
		mContentLoad = (LinearLayout)findViewById(R.id.content_load);
	}

	private void initView() {
		mapManager = new BMapManager(getApplication());
		mapManager.init("C69ADA9C85D9466B27E847377816A8ED8284CC10", null);
		super.initMapActivity(mapManager);

		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setTraffic(true);
		mMapView.setBuiltInZoomControls(true);
		mMapController = mMapView.getController();
		mMapController.setZoom(16);
		mMKSearch = new MKSearch();
		mMKSearch.init(mapManager, new MySearchListener());

		MKPlanNode startPlanNode = new MKPlanNode();
		startPlanNode.pt = new GeoPoint(mStartLatitude, mStartLongitude);
		MKPlanNode endPlanNode = new MKPlanNode();
		endPlanNode.pt = new GeoPoint(mEndLatitude, mEndLongitude);
		Log.i("Search","mEndLatitude:" + mEndLatitude);
		Log.i("Search","mEndLongitude:" + mEndLongitude);
		/**
		 * ���ù�������·���������ԣ�������4�ֲ��Կ�ѡ�� 1����������MKSearch.EBUS_NO_SUBWAY
		 * 2��ʱ�����ȣ�MKSearch.EBUS_TIME_FIRST 3�����ٻ��ˣ�MKSearch.EBUS_TRANSFER_FIRST
		 * 4�����ٲ��о��룺MKSearch.EBUS_WALK_FIRST
		 * 
		 * ��������ѡ����������������ٻ��ˣ�����;ת����������
		 */
		switch (mType) {
		case 0:
//			mMKSearch.setTransitPolicy(MKSearch.EBUS_TRANSFER_FIRST);
			mMKSearch.transitSearch(mCity, startPlanNode, endPlanNode);
			break;
		case 1:
			mMKSearch.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
			mMKSearch.drivingSearch(mCity, startPlanNode, mCity, endPlanNode);
			break;
		case 2:
			mMKSearch.setTransitPolicy(MKSearch.EBUS_WALK_FIRST);
			mMKSearch.walkingSearch(mCity, startPlanNode, mCity, endPlanNode);
			break;
		}

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

	public class MySearchListener implements MKSearchListener {
		/**
		 * ��ݾ�γ��������ַ��Ϣ���
		 * 
		 * @param result
		 *            �������
		 * @param iError
		 *            ����ţ�0��ʾ��ȷ���أ�
		 */
		public void onGetAddrResult(MKAddrInfo result, int iError) {
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
			Log.i("Transit", "drive result:" + result.getNumPlan());
			MKRoutePlan plan = result.getPlan(0);
			mHeaderHint.setText(String.valueOf(plan.getDistance()));
			mContentLoad.setVisibility(View.GONE);
			mMapRel.setVisibility(View.VISIBLE);
			RouteOverlay routeOverlay = new RouteOverlay(
					SearchLineActivity.this, mMapView);
			routeOverlay.setData(plan.getRoute(0));
			mMapView.getOverlays().add(routeOverlay);
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
			Log.i("Transit", "iError: " + iError);
			if (result == null) {
				Log.i("Transit", "result is null ");
				return;
			} else {
				busList.clear();
				Log.d("SearchLine", "�������˷�����" + result.getNumPlan());
				int dis =0;
				for (int i = 0; i < result.getNumPlan(); i++) {
					MKTransitRoutePlan routePlan = result.getPlan(i);
					String route = routePlan.getContent();
					dis = routePlan.getDistance();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(String.valueOf(i), route);
					busList.add(map);
				}
				mHeaderHint.setText(String.valueOf(dis));
				mLoading.setVisibility(View.GONE);
				mAdapter = new SimpleAdapter(SearchLineActivity.this, busList,
						android.R.layout.simple_list_item_2, new String[] {
								"id", "content" }, new int[] {
								android.R.id.text1, android.R.id.text2 });
				mListView.setAdapter(mAdapter);
			}

//			// TransitOverlay��baidu map api�ṩ�������ڵ�ͼ����ʾ��������·�ߵ�Overlay
//			TransitOverlay transitOverlay = new TransitOverlay(
//					SearchLineActivity.this, mMapView);
//			// չʾ����һ�����˷���
//			transitOverlay.setData(result.getPlan(0));
//			// �ڵ�ͼ����ʾ
//			mMapView.getOverlays().add(transitOverlay);

			/**
			 * �����Ҫ�ڵ�ͼ��չʾ���й������˷������뽫165-170��ע�ͣ�����ע����176-186
			 * ������ͨ�����صĹ������˷�����϶࣬ȫ��ʾ�ڵ�ͼ�ϻ���ң����ֱܷ治����,����ֻ��ʾ������һ�ַ���
			 */
			/**
			 * TransitOverlay transitOverlay = null; // �����������õ����л��˷��� for(int
			 * i=0; i<result.getNumPlan(); i++) { transitOverlay = new
			 * TransitOverlay(TransitPolicyActivity.this, mMapView); // ����չʾ���
			 * transitOverlay.setData(result.getPlan(i)); // �ڵ�ͼ����ʾ
			 * mMapView.getOverlays().add(transitOverlay); }
			 */

			/**
			 * ˵���� 1����������·���������ͨ�����ж��ֻ��˷���; 2��ͨ��result.getNumPlan()���Եõ����˷�����
			 * �����ڵ�ͼ�ϱ�ע����һ�ֹ������˷����⣬�������Ҫ�õ����л��˷�������Ϣ������Ҫ��������������������
			 */

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
			Log.i("Transit", "walk result:" + result.getNumPlan());
			MKRoutePlan plan = result.getPlan(0);
			mHeaderHint.setText(String.valueOf(plan.getDistance()));
			mContentLoad.setVisibility(View.GONE);
			mMapRel.setVisibility(View.VISIBLE);
			RouteOverlay routeOverlay = new RouteOverlay(
					SearchLineActivity.this, mMapView);
			routeOverlay.setData(plan.getRoute(0));
			mMapView.getOverlays().add(routeOverlay);
		}

		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	}
}
