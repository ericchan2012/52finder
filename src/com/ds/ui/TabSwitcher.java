package com.ds.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ds.finder.R;

public class TabSwitcher extends LinearLayout{

	private static final String tag="TabSwitcher";
	private Context context;
	private String[] texts;
	private int arrayId;
	private int selectedPosition=0;
	private int oldPosition=selectedPosition;
	private int[] background={R.drawable.tabswitcher_short,Color.TRANSPARENT};
	private TextView[] tvs;
	public TabSwitcher(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	public TabSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Log.i(tag, "--------------TabSwitcher2---------------------");
		init();
		TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.custom);  
		arrayId=a.getResourceId(R.styleable.custom_arrayId, 0);
		selectedPosition=a.getInt(R.styleable.custom_selectedPosition, 0);
        a.recycle();
	}
	private void init(){
		context=getContext();
		setOrientation(HORIZONTAL);
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		setLayoutParams(params);
//		texts=new String[]{"��Ϸ","Ӧ��","����"};
		setBackgroundResource(R.drawable.tabswitcher_long);
		
	}
	
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		Log.i(tag, "--------------onFinishInflate---------------------");
		if(arrayId!=0){
			texts=getResources().getStringArray(arrayId);
		}else{
			texts=new String[]{};
		}
		
		tvs=new TextView[texts.length];
		LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		params.weight=1;
		params.gravity=Gravity.CENTER_VERTICAL;
		for(int i=0;i<texts.length;i++){
			TextView child=new TextView(context);
			child.setTag(i);
//			child.setText(texts[i]);
			child.setTextSize(16);
			child.setTextColor(Color.BLACK);
			child.setGravity(Gravity.CENTER);
			
			tvs[i]=child;
			child.setOnClickListener(listener);
			this.addView(child, params);
		}
	}

	OnClickListener listener = new OnClickListener(){

		public void onClick(View v) {
			// TODO Auto-generated method stub
			selectedPosition=(Integer)v.getTag();
			if(selectedPosition!=oldPosition){
				tvs[oldPosition].setBackgroundColor(background[1]);
				oldPosition=selectedPosition;
				((TextView)v).setBackgroundResource(background[0]);
				if(onItemClickLisener!=null){
					onItemClickLisener.onItemClickLisener(v, selectedPosition);
				}
			}
		}
		
	};
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		Log.i("tag", "---------------onSizeChanged--------------------");
		if(selectedPosition>texts.length-1){
			throw new IllegalArgumentException("The selectedPosition can't be > texts.length.");
		}
		oldPosition=selectedPosition;
		for(int i=0;i<texts.length;i++){
			tvs[i].setText(texts[i]);
			if(selectedPosition==i){
				tvs[i].setBackgroundResource(background[0]);
			}else{
				tvs[i].setBackgroundColor(background[1]);
			}
		}
	}
	
	private OnItemClickLisener onItemClickLisener;
	public void setOnItemClickLisener(OnItemClickLisener onItemClickLisener) {
		this.onItemClickLisener = onItemClickLisener;
	}
	public interface OnItemClickLisener{
		void onItemClickLisener(View view,int position);
	}
	
	public void setTexts(String[] texts) {
		this.texts = texts;
	}
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}
//	private void animationStart(int left,int leftMargin){
//		TranslateAnimation trans = new TranslateAnimation(
//				Animation.RELATIVE_TO_SELF, 0, Animation.ABSOLUTE,
//				left/width, Animation.RELATIVE_TO_SELF, 0,
//				Animation.RELATIVE_TO_SELF, 0);
//		trans.setStartOffset(0);
//		trans.setDuration(1000);
//		trans.setFillBefore(false);
//		between.startAnimation(trans);
//		setLayoutParams(leftMargin);
//	}
	
}
