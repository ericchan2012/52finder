package com.ds.finder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRoutePlan;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.TransitOverlay;

public class TransiteActivity extends MapActivity {
	private BMapManager mapManager; 
	private MKSearch mMKSearch;
	private MapView mapView;
	private MapController mapController;
	ImageView mBusImage;
	ImageView mDriveImage;
	ImageView mWalkImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transite);

		mapManager = new BMapManager(getApplication());
		mapManager.init("C69ADA9C85D9466B27E847377816A8ED8284CC10", null);
		super.initMapActivity(mapManager);

		mapView = (MapView) findViewById(R.id.map_View);
		mapView.setTraffic(true);
		mapView.setBuiltInZoomControls(true);
		
		View view = getLayoutInflater().inflate(R.layout.map_search_route_layout, null);
		mapView.addView(view);
		// mapView.removeView(view);
		GeoPoint geoPoint = new GeoPoint((int) (26.449446 * 1E6),
				(int) (106.682949 * 1E6));
		mapController = mapView.getController();
		mapController.setCenter(geoPoint);
		mapController.setZoom(12);
		mMKSearch = new MKSearch();
		mMKSearch.init(mapManager, new MySearchListener());
		
		mBusImage = (ImageView)findViewById(R.id.bus_route);
		mDriveImage = (ImageView)findViewById(R.id.drive_route);
		mWalkImage = (ImageView)findViewById(R.id.walk_route);
		mBusImage.setSelected(true);
	}

	public void onLocationStartClick(View view) {
		
	}

	public void onLocationEndClick(View view) {

	}

	public void onSwitchLocationClick(View view) {

	}

	//bus
	public void onBusRouteClick(View view) {
		if(!mBusImage.isSelected()){
			mBusImage.setSelected(true);
			mDriveImage.setSelected(false);
			mWalkImage.setSelected(false);
		}
		MKPlanNode startPlanNode = new MKPlanNode();
		startPlanNode.pt = new GeoPoint((int) (26.449446 * 1E6),
				(int) (106.682949 * 1E6));
		MKPlanNode endPlanNode = new MKPlanNode();
		endPlanNode.pt = new GeoPoint((int) (26.601771 * 1E6),
				(int) (106.71968 * 1E6));
		/**
		 * ���ù�������·���������ԣ�������4�ֲ��Կ�ѡ�� 1����������MKSearch.EBUS_NO_SUBWAY
		 * 2��ʱ�����ȣ�MKSearch.EBUS_TIME_FIRST 3�����ٻ��ˣ�MKSearch.EBUS_TRANSFER_FIRST
		 * 4�����ٲ��о��룺MKSearch.EBUS_WALK_FIRST
		 * 
		 * ��������ѡ����������������ٻ��ˣ�����;ת����������
		 */
		mMKSearch.setTransitPolicy(MKSearch.EBUS_TRANSFER_FIRST);
		// Ҫ�������ĸ���������
		mMKSearch.transitSearch("����", startPlanNode, endPlanNode);
	}
	//drive
	public void onDriveRouteClick(View view) {
		if(!mDriveImage.isSelected()){
			mBusImage.setSelected(false);
			mDriveImage.setSelected(true);
			mWalkImage.setSelected(false);
		}
		MKPlanNode startPlanNode = new MKPlanNode();
		startPlanNode.pt = new GeoPoint((int) (26.449446 * 1E6),
				(int) (106.682949 * 1E6));
		MKPlanNode endPlanNode = new MKPlanNode();
		endPlanNode.pt = new GeoPoint((int) (26.601771 * 1E6),
				(int) (106.71968 * 1E6));
		/**
		 * ���ù�������·���������ԣ�������4�ֲ��Կ�ѡ�� 1����������MKSearch.EBUS_NO_SUBWAY
		 * 2��ʱ�����ȣ�MKSearch.EBUS_TIME_FIRST 3�����ٻ��ˣ�MKSearch.EBUS_TRANSFER_FIRST
		 * 4�����ٲ��о��룺MKSearch.EBUS_WALK_FIRST
		 * 
		 * ��������ѡ����������������ٻ��ˣ�����;ת����������
		 */
		mMKSearch.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
		// Ҫ�������ĸ���������
		mMKSearch.drivingSearch("����", startPlanNode, "����", endPlanNode);
	}
	//walk
	public void onWalkRouteClick(View view) {
		if(!mWalkImage.isSelected()){
			mBusImage.setSelected(false);
			mDriveImage.setSelected(false);
			mWalkImage.setSelected(true);
		}
		MKPlanNode startPlanNode = new MKPlanNode();
		startPlanNode.pt = new GeoPoint((int) (26.449446 * 1E6),
				(int) (106.682949 * 1E6));
		MKPlanNode endPlanNode = new MKPlanNode();
		endPlanNode.pt = new GeoPoint((int) (26.601771 * 1E6),
				(int) (106.71968 * 1E6));
		mMKSearch.setTransitPolicy(MKSearch.EBUS_WALK_FIRST);
		mMKSearch.walkingSearch("����", startPlanNode, "����", endPlanNode);
	}
	
	public void onRouteSearchClick(View view){
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onDestroy() {
		if (mapManager != null) {
			// �����˳�ǰ����ô˷���
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mapManager != null) {
			// ��ֹ�ٶȵ�ͼAPI
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mapManager != null) {
			// �����ٶȵ�ͼAPI
			mapManager.start();
		}
		super.onResume();
	}

	/**
	 * ʵ��MKSearchListener�ӿ�,����ʵ���첽��������
	 * 
	 * @author liufeng
	 */
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
			Log.i("Transit","drive result:" + result.getNumPlan());
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
			if (result == null) {
				return;
			}
			Log.d("liuyq", "�������˷�����" + result.getNumPlan());

			// TransitOverlay��baidu map api�ṩ�������ڵ�ͼ����ʾ��������·�ߵ�Overlay
			TransitOverlay transitOverlay = new TransitOverlay(
					TransiteActivity.this, mapView);
			// չʾ����һ�����˷���
			transitOverlay.setData(result.getPlan(0));
			// �ڵ�ͼ����ʾ
			mapView.getOverlays().add(transitOverlay);

			/**
			 * �����Ҫ�ڵ�ͼ��չʾ���й������˷������뽫165-170��ע�ͣ�����ע����176-186
			 * ������ͨ�����صĹ������˷�����϶࣬ȫ��ʾ�ڵ�ͼ�ϻ���ң����ֱܷ治����,����ֻ��ʾ������һ�ַ���
			 */
			/**
			 * TransitOverlay transitOverlay = null; // �����������õ����л��˷��� for(int
			 * i=0; i<result.getNumPlan(); i++) { transitOverlay = new
			 * TransitOverlay(TransitPolicyActivity.this, mapView); // ����չʾ���
			 * transitOverlay.setData(result.getPlan(i)); // �ڵ�ͼ����ʾ
			 * mapView.getOverlays().add(transitOverlay); }
			 */

			/**
			 * ˵���� 1����������·���������ͨ�����ж��ֻ��˷���; 2��ͨ��result.getNumPlan()���Եõ����˷�����
			 * �����ڵ�ͼ�ϱ�ע����һ�ֹ������˷����⣬�������Ҫ�õ����л��˷�������Ϣ������Ҫ��������������������
			 */

			// �����������õ����л��˷���
			for (int i = 0; i < result.getNumPlan(); i++) {
				// �������˷���������
				MKTransitRoutePlan routePlan = result.getPlan(i);
				// �ٸ��MKTransitRoutePlan���ṩ�ķ���ȥ��ȡ����Ļ�����Ϣ��ʡ�ԣ�
				routePlan.getContent();
			}
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
			Log.i("Transit","walk result:" + result.getNumPlan());
		}

		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	}
}