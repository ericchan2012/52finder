package com.ds.todolist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ds.finder.R;

public class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.base);
	}

	public void baseSetContentView(int layoutResId) {
		LinearLayout llContent = (LinearLayout) findViewById(R.id.content);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutResId, null);
		llContent.addView(v);
	}

	public void hideCancel(){
		Button btn = getCancel();
		if(btn != null){
			btn.setVisibility(View.GONE);
		}
	}
	
	public void showCancel(){
		Button btn = getCancel();
		if(btn != null){
			btn.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideConfirm(){
		Button btn = getConfirm();
		if(btn != null){
			btn.setVisibility(View.GONE);
		}
	}
	
	public void showConfirm(){
		Button btn = getConfirm();
		if(btn != null){
			btn.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideAdd(){
		Button btn = getAdd();
		if(btn != null){
			btn.setVisibility(View.GONE);
		}
	}
	
	public void showAdd(){
		Button btn = getAdd();
		if(btn != null){
			btn.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideDelete(){
		Button btn = getDelete();
		if(btn != null){
			btn.setVisibility(View.GONE);
		}
	}
	
	public void showDelete(){
		Button btn = getDelete();
		if(btn != null){
			btn.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideBack(){
		Button btn = getBack();
		if(btn != null){
			btn.setVisibility(View.GONE);
		}
	}
	
	public void showBack(){
		Button btn = getBack();
		if(btn != null){
			btn.setVisibility(View.VISIBLE);
		}
	}
	
	public Button getCancel(){
		return (Button)findViewById(R.id.cancel);
	}
	
	public Button getConfirm(){
		return (Button)findViewById(R.id.confirm);
	}
	public Button getDelete(){
		return (Button)findViewById(R.id.delete);
	}
	public Button getAdd(){
		return (Button)findViewById(R.id.add);
	}
	public Button getBack(){
		return (Button)findViewById(R.id.titlbarback);
	}
	
}
