package com.ds.finder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ds.inspiration.NotesList;
import com.ds.todolist.ToDoListActivity;
import com.ds.util.Util;

public class FinderActivity extends Activity {

	// static {
	// System.loadLibrary("locSDK_2.2");
	// }
	private static final String TAG = "FinderActivity";
	TextView mShowLocText;
	ProgressBar mShowLocBar;

	Util mUtil;
	private LocationClient mLocClient = null;

	private boolean mTagOn = true;
	private boolean mHasLoc = false;
	ImageView homeImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUtil = Util.getInstance();
		mUtil.requestNoTitle(this);
		setContentView(R.layout.main);

		homeImage = (ImageView) findViewById(R.id.homeimg);

		AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
		alphaAnimation.setDuration(3000);
		alphaAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				homeImage.setVisibility(View.GONE);
			}
		});

		homeImage.setAnimation(alphaAnimation);
		homeImage.setVisibility(View.VISIBLE);

		findView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initLoc();
	}

	private void initLoc() {
		MyLocationListener myListener = new MyLocationListener();
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("detail");
		option.setCoorType("gcj02");
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		if (mLocClient != null && mLocClient.isStarted())
			mLocClient.requestLocation();
	}

	private void findView() {
		mShowLocText = (TextView) findViewById(R.id.title_show_loc);
		mShowLocBar = (ProgressBar) findViewById(R.id.title_show_loc_bar);
	}

	public void onActionBarRefreshButtonClick(View view) {
		if (mShowLocBar != null && mShowLocText != null) {
			mHasLoc = false;
			mShowLocBar.setVisibility(View.VISIBLE);
			mShowLocText.setText("Loading...");
			initLoc();
		}
	}

	public void onActionOneClick(View view) {
		if (mHasLoc) {
			Intent intent = new Intent();
			intent.setClass(this, POIActivity.class);
			startActivity(intent);
		} else {
			Toast.makeText(this, "No addr", Toast.LENGTH_SHORT).show();
		}
	}

	public void onActionTwoClick(View view) {
		Intent intent = new Intent(this, ToDoListActivity.class);
		startActivity(intent);

	}

	public void onActionThreeClick(View view) {

	}

	public void onActionFourClick(View view) {
		Intent intent = new Intent(this, NotesList.class);
		startActivity(intent);
	}

	public void onActionFiveClick(View view) {

	}

	public void onActionSixClick(View view) {

	}

	@Override
	public void onDestroy() {
		stopLocClient();
		super.onDestroy();
	}

	public void stopLocClient() {
		mLocClient.stop();
	}

	// private String parseJson(String jsondata) {
	// try {
	// JSONObject jsonObj = new JSONObject(jsondata);
	// JSONObject jsonContent = jsonObj
	// .getJSONObject(Constants.TAG_CONTENT);
	// JSONObject jsonAddr = jsonContent.getJSONObject(Constants.TAG_ADDR);
	// String detail = jsonAddr.getString(Constants.TAG_DETAIL);
	// return detail;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }
	class MyLocationListener implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			StringBuffer sb = new StringBuffer(256);
			if (location == null) {
				mLocClient.requestLocation();
			}
			// else if (location.getLocType().equals("InternetException")) {
			// mShowLocText.setText("InternetException");
			// mShowLocBar.setVisibility(View.GONE);
			// }
			else {
				// logMsg("ReceiveListener: " + strData);
				String address = location.getAddrStr();
				// sb.append("time : ");
				// sb.append(location.getTime());
				// sb.append("\nerror code : ");
				// sb.append(location.getLocType());
				// sb.append("\nlatitude : ");
				// sb.append(location.getLatitude());
				// sb.append("\nlontitude : ");
				// sb.append(location.getLongitude());
				// sb.append("\nradius : ");
				// sb.append(location.getRadius());
				// if (location.getLocType() == BDLocation.TypeGpsLocation){
				// sb.append("\nspeed : ");
				// sb.append(location.getSpeed());
				// sb.append("\nsatellite : ");
				// sb.append(location.getSatelliteNumber());
				// } else if (location.getLocType() ==
				// BDLocation.TypeNetWorkLocation){
				// sb.append("\naddr : ");
				// sb.append(location.getAddrStr());
				if (address != null) {
					if (mShowLocBar != null && mShowLocText != null) {
						mShowLocBar.setVisibility(View.GONE);
						mShowLocText.setText(address);
						mHasLoc = true;
						stopLocClient();
					}
				}
			}
			if (location.getLocType() == BDLocation.TypeGpsLocation) {
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			}
		}
	};
}
