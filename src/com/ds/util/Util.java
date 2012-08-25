package com.ds.util;

import android.app.Activity;
import android.view.Window;

public class Util {
	
	private static Util mUtil;

	private Util(){
		
	}
	
	public static Util getInstance(){
		mUtil = new Util();
		return mUtil;
	}
	
	public void requestNoTitle(Activity ac){
		ac.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
}
