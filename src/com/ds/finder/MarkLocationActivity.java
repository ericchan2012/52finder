package com.ds.finder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKRoute;
import com.baidu.mapapi.MKRouteAddrResult;
import com.baidu.mapapi.MKRoutePlan;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRoutePlan;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;
import com.baidu.mapapi.RouteOverlay;
import com.baidu.mapapi.TransitOverlay;
import com.ds.util.Constants;
import com.ds.util.Util;

public class MarkLocationActivity extends MapActivity {
	private BMapManager mapManager;
	private MapView mMapView;
	private MapController mMapController;
	Util mUtil;
	public View popView;
	int mRange;
	String mKeyword;
	String mAddr;
	String mCity;
	String mName;
	int mEpoiType;
	String mPhoneNum;
	String mPostCode;
	int mLatitude;
	int mLongitude;
	View mSearchLineView;
	ImageView mBusImage;
	ImageView mDriveImage;
	ImageView mWalkImage;
//	MKSearch mMKSearch;
	
	int mMyLatitude;
	int mMyLongitude;
//	ArrayList<String> busList = new ArrayList<String>();
//	ArrayList<String> driveList = new ArrayList<String>();
//	ArrayList<String> walkList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUtil = Util.getInstance();
		mUtil.requestNoTitle(this);
		setContentView(R.layout.transite);
		getInitData();
		findView();
		initNear();
	}

	private void getInitData() {
		Bundle extras = getIntent().getExtras();
		mAddr = extras.getString(Constants.ADDRESS);
		mCity = extras.getString(Constants.CITY);
		mName = extras.getString(Constants.NAME);
		mEpoiType = extras.getInt(Constants.EPOITYPE);
		mPhoneNum = extras.getString(Constants.PHONE_NUM);
		mPostCode = extras.getString(Constants.POST_CODE);
		mLatitude = extras.getInt(Constants.PT_LATITUDE);
		mLongitude = extras.getInt(Constants.PT_LONGITUDE);
		mMyLatitude = extras.getInt(Constants.MY_LATITUDE);
		mMyLongitude = extras.getInt(Constants.MY_LONGITUDE);
		mSearchLineView = getLayoutInflater().inflate(
				R.layout.map_search_route_layout, null);
	}

	private void findView() {
		mMapView = (MapView) findViewById(R.id.map_View);
	}

	private void initPopview() {

		popView = super.getLayoutInflater().inflate(R.layout.map_location_popview, null);
		mMapView.addView(popView, new MapView.LayoutParams(
				MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.TOP_LEFT));
	}

	private void initNear() {
		mapManager = new BMapManager(getApplication());
		mapManager.init(Constants.BAIDU_KEY, null);
		super.initMapActivity(mapManager);

		mMapView = (MapView) findViewById(R.id.map_View);
		mMapView.setTraffic(true);
		mMapView.setBuiltInZoomControls(true);

		Drawable marker = getResources().getDrawable(R.drawable.shopmark); // 得到需要标在地图上的资源
		marker.setBounds(0, 0,58,58); // 为maker定义位置和边界
		initPopview();
		GeoPoint point = new GeoPoint(mLatitude, mLongitude);
		OverItemT item = new OverItemT(marker, MarkLocationActivity.this,point, "");
		mMapView.getOverlays().add(item);
		mMapController = mMapView.getController();
		mMapController.setCenter(point);
		mMapController.setZoom(18);
		
//		mMKSearch = new MKSearch();
//		mMKSearch.init(mapManager, new MySearchListener());
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
	
//	private class MySearchListener implements MKSearchListener {
//		@Override
//		public void onGetAddrResult(MKAddrInfo result, int iError) {
//		}
//
//		@Override
//		public void onGetDrivingRouteResult(MKDrivingRouteResult result,
//				int iError) {
//			
////			driveList.clear();
////			if(result == null){
////				Toast.makeText(MarkLocationActivity.this, "No route", Toast.LENGTH_SHORT).show();
////				return;
////			}else{
////				Log.i("Transit","drive result:" + result.getNumPlan());
////				int numplan = result.getNumPlan();
////				for (int i = 0; i < numplan; i++) {
////					MKRouteAddrResult routeaddr = result.getAddrResult();
////					MKRoutePlan routePlan = result.getPlan(i);
////					MKRoute route = routePlan.getRoute(i);
////				}
////			}
//		}
//		@Override
//		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
//		}
//		@Override
//		public void onGetTransitRouteResult(MKTransitRouteResult result,
//				int iError) {
//			busList.clear();
////			if (result == null) {
////				Toast.makeText(MarkLocationActivity.this, "No route", Toast.LENGTH_SHORT).show();
////				return;
////			}else{
////				Log.d("liuyq", "公交换乘方案数：" + result.getNumPlan());
////				int numplan = result.getNumPlan();
////				for (int i = 0; i < numplan; i++) {
////					MKTransitRoutePlan routePlan = result.getPlan(i);
////					String route = routePlan.getContent();
////					busList.add(route);
////				}
////				sendIntentToSearchLine("buslist",busList);
////			}
//			
////
////			// TransitOverlay是baidu map api提供的用于在地图上显示公交换乘路线的Overlay
////			TransitOverlay transitOverlay = new TransitOverlay(
////					MarkLocationActivity.this, mMapView);
////			// 展示其中一个换乘方案
////			transitOverlay.setData(result.getPlan(0));
////			// 在地图上显示
//////			mapView.getOverlays().add(transitOverlay);
////
////			/**
////			 * 如果需要在地图上展示所有公交换乘方案，请将165-170行注释，并打开注释行176-186
////			 * 但由于通常返回的公交换乘方案数较多，全显示在地图上会很乱，可能分辨不出来,所以只显示了其中一种方案
////			 */
////			/**
////			 * TransitOverlay transitOverlay = null; // 遍历搜索结果，得到所有换乘方案 for(int
////			 * i=0; i<result.getNumPlan(); i++) { transitOverlay = new
////			 * TransitOverlay(TransitPolicyActivity.this, mapView); // 设置展示数据
////			 * transitOverlay.setData(result.getPlan(i)); // 在地图上显示
////			 * mapView.getOverlays().add(transitOverlay); }
////			 */
////
////			/**
////			 * 说明： 1）公交换乘路线搜索结果通常都有多种换乘方案; 2）通过result.getNumPlan()可以得到换乘方案数。
////			 * 除了在地图上标注其中一种公交换乘方案外，如果我们要得到所有换乘方案的信息，就需要像下面这样遍历搜索结果
////			 */
//
//			
//		}
//		@Override
//		public void onGetWalkingRouteResult(MKWalkingRouteResult result,
//				int iError) {
//			
////			if (result == null) {
////				Toast.makeText(MarkLocationActivity.this, "No route", Toast.LENGTH_SHORT).show();
////				return;
////			}else{
////				Log.i("Transit","walk result:" + result.getNumPlan());
////				int numplan = result.getNumPlan();
////				for (int i = 0; i < numplan; i++) {
////					int dis = result.getPlan(i).getDistance();
////					Toast.makeText(MarkLocationActivity.this, String.valueOf(dis), Toast.LENGTH_SHORT).show();
////				}
////				
////				RouteOverlay routeOverlay = new RouteOverlay(
////						MarkLocationActivity.this, mMapView);
////				routeOverlay.setData(result.getPlan(0).getRoute(0));
////				mMapView.getOverlays().add(routeOverlay);
////			}
//			
//		}
//		
//	};

	private class OverItemT extends ItemizedOverlay<OverlayItem> {

		private List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
		private Drawable marker;
		private MarkLocationActivity mContext;
		private TextView textView1;
		private ImageView switchLocation;
//		private ImageView locationStart;
//		private ImageView locationEnd;
		private TextView startAddr;
		private TextView endAddr;
		private TextView routeSearch;
		
		private String name;
		private int type = 0;
		
		public OverItemT(Drawable marker, Context context, final GeoPoint pt,
				String mer_name) {
			super(boundCenterBottom(marker));

			this.marker = marker;
			this.mContext = (MarkLocationActivity) context;

			GeoPoint p1 = pt;
			mGeoList.add(new OverlayItem(p1, "", mer_name));
			populate();
			mContext.mMapView.updateViewLayout(mContext.popView,
					new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, pt,0,0,
							MapView.LayoutParams.BOTTOM_CENTER));
			mContext.popView.setVisibility(View.VISIBLE);
			textView1 = (TextView) mContext.findViewById(R.id.title);
			textView1.setText(mName);
			ImageView imageView = (ImageView) mContext
					.findViewById(R.id.search_route);
			imageView.setClickable(true);
			imageView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					initshowSearchView(pt);
					v.setClickable(false);
				}
			});
		}
		
		private void sendIntentToSearchLine(int type,int startLatitude,int endLatitude,int startLongitude,int endLongitude,String start,String end){
			Intent intent = new Intent(MarkLocationActivity.this, SearchLineActivity.class);
			Bundle extras = new Bundle();
			extras.putInt(Constants.TYPE, type);
			extras.putInt(Constants.START_LATITUDE, startLatitude);
			extras.putInt(Constants.END_LATITUDE, endLatitude);
			extras.putInt(Constants.START_LONGITUDE, startLongitude);
			extras.putInt(Constants.END_LONGITUDE, endLongitude);
			extras.putString(Constants.START, start);
			extras.putString(Constants.END, end);
			extras.putString(Constants.CITY, mCity);
			intent.putExtras(extras);
			startActivity(intent);
		}
		
		public void initshowSearchView(final GeoPoint mpt){
			mContext.mMapView.addView(mContext.mSearchLineView);
			mBusImage = (ImageView)findViewById(R.id.bus_route);
			mDriveImage = (ImageView)findViewById(R.id.drive_route);
			mWalkImage = (ImageView)findViewById(R.id.walk_route);
			mBusImage.setSelected(true);
			
			routeSearch = (TextView)findViewById(R.id.route_search);
			endAddr = (TextView)findViewById(R.id.end_addr);
			startAddr = (TextView)findViewById(R.id.start_addr);
			endAddr.setText(mName);
			startAddr.setText(R.string.my_location);
			
			routeSearch.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
//					MKPlanNode startPlanNode = new MKPlanNode();
//					Log.i("Mark", "mLatitude:" + mLatitude);
//					Log.i("Mark", "mLongitude:" + mLongitude);
//					startPlanNode.pt = new GeoPoint(mLatitude,mLongitude);
//					MKPlanNode endPlanNode = new MKPlanNode();
//					endPlanNode.pt = mpt;
					/**
					 * 设置公交换乘路线搜索策略，有以下4种策略可选择： 1）不含地铁：MKSearch.EBUS_NO_SUBWAY
					 * 2）时间优先：MKSearch.EBUS_TIME_FIRST 3）最少换乘：MKSearch.EBUS_TRANSFER_FIRST
					 * 4）最少步行距离：MKSearch.EBUS_WALK_FIRST
					 * 
					 * 我们这里选择的搜索策略是最少换乘，即中途转车次数最少
					 */
//					
//					switch(type){
//					case 0:
//						mMKSearch.setTransitPolicy(MKSearch.EBUS_TRANSFER_FIRST);
//						mMKSearch.transitSearch(mCity, startPlanNode, endPlanNode);
//						break;
//					case 1:
//						mMKSearch.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);
//						mMKSearch.drivingSearch(mCity, startPlanNode, mCity, endPlanNode);
//						break;
//					case 2:
//						mMKSearch.setTransitPolicy(MKSearch.EBUS_WALK_FIRST);
//						mMKSearch.walkingSearch(mCity, startPlanNode, mCity, endPlanNode);
//						break;
//					}
					int startLatitude = mMyLatitude;
					int endLatitude = mLatitude;
					int startLongitude =mMyLongitude;
					int endLongitude = mLongitude;
					String startTxt = startAddr.getText().toString();
					String endTxt = endAddr.getText().toString();
					sendIntentToSearchLine(type,startLatitude,endLatitude,startLongitude,endLongitude,startTxt,endTxt);		
				}				
			});
			
			switchLocation = (ImageView)findViewById(R.id.switch_location);
//			locationStart = (ImageView)findViewById(R.id.location_start);
//			locationEnd = (ImageView)findViewById(R.id.location_end);
//			
//			locationStart.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					startAddr.setText(R.string.my_location);
//					endAddr.setText("");
//				}
//			});
//			
//			locationEnd.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					endAddr.setText(R.string.my_location);
//					startAddr.setText("");
//				}
//			});
			
			switchLocation.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					String endaddr = endAddr.getText().toString();
					String startaddr = startAddr.getText().toString();
					endAddr.setText(startaddr);
					startAddr.setText(endaddr);
				}
			});
			
			mBusImage.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					if(!mBusImage.isSelected()){
						mBusImage.setSelected(true);
						mDriveImage.setSelected(false);
						mWalkImage.setSelected(false);
					}
					type = 0;
					
				}
				
			});
			mDriveImage.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					if(!mDriveImage.isSelected()){
						mBusImage.setSelected(false);
						mDriveImage.setSelected(true);
						mWalkImage.setSelected(false);
					}
					
					type = 1;
				}
				
			});
			mWalkImage.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					if(!mWalkImage.isSelected()){
						mBusImage.setSelected(false);
						mDriveImage.setSelected(false);
						mWalkImage.setSelected(true);
					}
					
					type = 2;
				}
			});
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {

			Projection projection = mapView.getProjection();
			for (int index = size() - 1; index >= 0; index--) {
				OverlayItem overLayItem = getItem(index);

				String title = overLayItem.getTitle();
				Point point = projection.toPixels(overLayItem.getPoint(), null);

				Paint paintText = new Paint();
				paintText.setColor(Color.BLUE);
				paintText.setTextSize(15);
				canvas.drawText(title, point.x - 30, point.y, paintText); // ªÊ÷∆Œƒ±æ
			}

			super.draw(canvas, mapView, shadow);
			boundCenterBottom(marker);
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mGeoList.get(i);
		}

		@Override
		public int size() {
			return mGeoList.size();
		}

		protected boolean onTap(int i) {
			Log.i("Mark","onTap:" + i);
			// setFocus(mGeoList.get(i));
			// MapView.LayoutParams geoLP = (MapView.LayoutParams)
			// mContext.popView
			// .getLayoutParams();
			// GeoPoint pt = mGeoList.get(i).getPoint();
			// mContext.mMapView.updateViewLayout(mContext.popView,
			// new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT, pt,
			// MapView.LayoutParams.BOTTOM_CENTER));
			// mContext.popView.setVisibility(View.VISIBLE);
			// textView1 = (TextView)
			// mContext.findViewById(R.id.map_bubbleTitle);
			// textView2 = (TextView)
			// mContext.findViewById(R.id.map_bubbleText);
			// textView3 = (TextView)
			// mContext.findViewById(R.id.map_bubbleTele);
			// Log.i("NearActivity","name:" + getName());
			// Log.i("NearActivity","address:" + getAddress());
			// Log.i("NearActivity","telephone number:" + getTelephone());
			// textView1.setText(getName());
			// textView2.setText(getAddress());
			// textView3.setText(getTelephone());
			// ImageView imageView = (ImageView) mContext
			// .findViewById(R.id.map_bubbleImage);
			// imageView.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
			// mContext.popView.setVisibility(View.GONE);
			// }
			//
			// });
			return true;
		}
		
		
//		@Override
//		public boolean onKeyDown(int arg0, KeyEvent arg1, MapView arg2) {
//			Log.i("Mark","keycode:" + arg1.getKeyCode());
//			Log.i("Mark","arg0:" + arg0);
//			if(arg1.getKeyCode() == KeyEvent.KEYCODE_BACK){
//				arg2.removeView(mContext.mSearchLineView);
//			}
//			return super.onKeyDown(arg0, arg1, arg2);
//		}
		
		@Override
		public boolean onKeyUp(int arg0, KeyEvent arg1, MapView arg2) {
			// TODO Auto-generated method stub
			Log.i("Mark","keycode:" + arg1.getKeyCode());
			Log.i("Mark","arg0:" + arg0);
			return super.onKeyUp(arg0, arg1, arg2);
		}

		public void removeSearchView(){
			mContext.mMapView.removeView(mContext.mSearchLineView);
		}

		@Override
		public boolean onTap(GeoPoint arg0, MapView arg1) {
			return super.onTap(arg0, arg1);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
