package com.ds.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ds.finder.R;

public class Titlebar{
	
	private Button back;
	private TextView title;
	private Button confirm;
	private Button cancel;
	
	public Titlebar(Context context) {
		initTitleBar(context);
	}
	
	private void initTitleBar(Context context){
		LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.titlebar, null);
		back = (Button)view.findViewById(R.id.titlbarback);
		confirm = (Button)view.findViewById(R.id.confirm);
		cancel = (Button)view.findViewById(R.id.cancel);
		title = (Button)view.findViewById(R.id.title);
	}
	public void showBack(boolean isShow){
		if(isShow){
			back.setVisibility(View.VISIBLE);
		}else{
			back.setVisibility(View.GONE);
		}
	}
	
	public void showTitle(boolean isShow){
		if(isShow){
			title.setVisibility(View.VISIBLE);
		}else{
			title.setVisibility(View.GONE);
		}
	}
	public void showConfirm(boolean isShow){
		if(isShow){
			confirm.setVisibility(View.VISIBLE);
		}else{
			confirm.setVisibility(View.GONE);
		}
	}
	public void showCancel(boolean isShow){
		if(isShow){
			cancel.setVisibility(View.VISIBLE);
		}else{
			cancel.setVisibility(View.GONE);
		}
	}
	
}
