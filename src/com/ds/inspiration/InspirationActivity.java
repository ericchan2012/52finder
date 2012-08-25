package com.ds.inspiration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ds.finder.R;
import com.ds.todolist.BaseActivity;

public class InspirationActivity extends BaseActivity{

	Button add;
	Button back;
	private static final int ADD_REQUEST_CODE = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.ins_main);
		showAdd();
		showBack();
		add = getAdd();
		back = getBack();
		add.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InspirationActivity.this,EditInspiration.class);
				startActivityForResult(intent,ADD_REQUEST_CODE);
			}
			
		});
		back.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}

}
