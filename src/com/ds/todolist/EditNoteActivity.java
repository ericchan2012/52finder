package com.ds.todolist;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ds.db.DatabaseAdapter;
import com.ds.finder.R;

public class EditNoteActivity extends Activity implements Constants,
		OnClickListener, TextWatcher {

	private int iMode = 0;// mode,0 is add,1 is edit,the default is 0
	private DatabaseAdapter adapter;
	private Button bSave;
	private Button bCancel;
	private EditText eContent;
	private boolean bIsChanged;
	private int iId;
	private String sContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit);
		findViews();
		dispatchIntent();
		openDb();
	}

	private void findViews() {
		// TODO Auto-generated method stub
		bSave = (Button) findViewById(R.id.save);
		bCancel = (Button) findViewById(R.id.cancel);
		bSave.setOnClickListener(this);
		bCancel.setOnClickListener(this);

		eContent = (EditText) findViewById(R.id.content);
		eContent.addTextChangedListener(this);
	}

	private void openDb() {
		adapter = new DatabaseAdapter(this);
		adapter.open();
	}

	/**
	 * Judge the intent is from add or edit
	 */
	private void dispatchIntent() {
		Intent intent = getIntent();
		String action = intent.getAction();
		if (action != null) {
			if (action.equals(ACTION_ADD_NOTE)) {
				iMode = 0;
			}
			if (action.equals(ACTION_EDIT_NOTE)) {
				iMode = 1;
			}
		}
		if (iMode == 1) {
			iId = intent.getIntExtra(NOTE_ID, INVALID_ID);
			sContent = intent.getStringExtra(NOTE_CONTENT);
			eContent.setText(sContent);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		adapter.close();
	}

	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch (id) {
		case R.id.save:
			if (iMode == 0) {
				String content = eContent.getText().toString();
				if (content.length() > 0) {
					saveNote(content);
					setResult(RESULT_OK);
					finish();
				}
			} else {
				if (bIsChanged) {
					String content = eContent.getText().toString();
					if (content.length() > 0) {
						editNote(content);
						setResult(RESULT_OK);
						finish();
					}
				}
			}
			break;
		case R.id.cancel:
			warning();
			break;
		}
	}

	private void editNote(String content) {

		if (iId != INVALID_ID) {
			ContentValues values = new ContentValues();
			values.put(NOTE_CONTENT, content);
			adapter.update(TODO_TABLE, values, NOTE_ID + " = " + iId);
		}
	}

	private void warning() {
		finish();
	}

	private void saveNote(String content) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date();
		String date = formatter.format(curDate);

		ContentValues values = new ContentValues();
		values.put(NOTE_CONTENT, content);
		values.put(NOTE_DATE, date);
		values.put(NOTE_MONTH, date.substring(0, 7));
		adapter.insert(TODO_TABLE, values);
	}

	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub

	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		bIsChanged = true;
		if (arg0.length() == 140) {
			showHint();
		}
	}

	private void showHint() {
		// TODO Auto-generated method stub
		Toast.makeText(this, R.string.max_length, Toast.LENGTH_LONG);
	}

}
