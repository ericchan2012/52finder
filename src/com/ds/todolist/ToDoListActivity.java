package com.ds.todolist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ds.db.DatabaseAdapter;
import com.ds.finder.R;

public class ToDoListActivity extends Activity implements OnClickListener,
		Constants, OnItemClickListener {
	public static final String TAG = "ToDoListActivity";
	private TextView title;
	private Button back;
	private View emptyView;
	private ListView noteList;
	private DatabaseAdapter adapter;
	private Cursor mCursor;
	private ToDoCursorAdapter sCursorAdapter;

	private int mListMode = LISTVIEW_NORMAL_MODE;

	private Button confirmBtn;
	private Button cancelBtn;
	private ArrayList<Integer> idList = new ArrayList<Integer>();
	private ArrayList<HashMap<String, Integer>> checkList = new ArrayList<HashMap<String, Integer>>();

	private ImageView homeImage;

	private GridView toolbarGrid;

	private final int TOOLBAR_ITEM_ADD = 0;
	private final int TOOLBAR_ITEM_EDIT = 1;
	private final int TOOLBAR_ITEM_DELETE = 2;
	private final int TOOLBAR_ITEM_ALL = 3;
	private final int TOOLBAR_ITEM_SHARE = 4;
	private final int TOOLBAR_ITEM_ABOUT = 5;

	private int[] menu_toolbar_image_array = { R.drawable.controlbar_homepage,
			R.drawable.controlbar_backward_enable,
			R.drawable.controlbar_forward_enable, R.drawable.controlbar_window,
			R.drawable.controlbar_showtype_list,
			R.drawable.controlbar_backward_enable };
	private String[] menu_toolbar_name_array = { "Add", "Edit", "Delete",
			"All", "Share", "Check" };

	AlertDialog menuDialog;
	GridView menuGrid;
	View menuView;

	String[] menu_name_array = { "Edit", "Delete", "All", "Share", "Check",
			"About" };

	int[] menu_image_array = { R.drawable.menu_search,
			R.drawable.menu_filemanager, R.drawable.menu_downmanager,
			R.drawable.menu_fullscreen, R.drawable.menu_inputurl,
			R.drawable.menu_bookmark, R.drawable.menu_bookmark_sync_import,
			R.drawable.menu_sharepage, R.drawable.menu_quit,
			R.drawable.menu_nightmode, R.drawable.menu_refresh,
			R.drawable.menu_more };

	Button addBtn;
	Button menuBtn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todolist_main);
		Log.i(TAG, "onCreate");
		findviews();

		initDate();

		openDb();

		idList.clear();
		checkList.clear();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onMenuOpened(int featureId, Menu menu) {

		showDialog();
		return false;
	}

	private void showDialog() {
		if (menuDialog == null) {
			menuDialog = new AlertDialog.Builder(this).setView(menuView).show();
		} else {
			menuDialog.show();
		}
		WindowManager.LayoutParams params = menuDialog.getWindow()
				.getAttributes();
		params.width = 200;
		params.height = 450;
		params.x = -120;
		params.y = 120;
		menuDialog.getWindow().setAttributes(params);
		menuDialog.setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Log.i(TAG, "onNewIntent");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i(TAG, "onRestart");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i(TAG, "onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i(TAG, "onStop");

	}

	private void openDb() {
		adapter = new DatabaseAdapter(this);
		adapter.open();
	}

	private void initAdapter() {
		Log.i(TAG, "mListMode:" + mListMode);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date today = new Date();
		String str = formatter.format(today);
		mCursor = adapter.queryWhereDate(str);
		boolean isCheck = false;
		switch (mListMode) {
		case LISTVIEW_NORMAL_MODE:
			title.setText(str);
			setBack(View.GONE);
			setConfirm(View.GONE);
			setCancel(View.GONE);
			isCheck = false;
			noteList.setChoiceMode(ListView.CHOICE_MODE_NONE);
			break;
		case LISTVIEW_SINGLE_MODE:
			title.setText(getResources().getString(R.string.edit));
			setBack(View.VISIBLE);
			setConfirm(View.GONE);
			setCancel(View.GONE);
			isCheck = false;
			noteList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			break;
		case LISTVIEW_MULTI_MODE:
			title.setText(getResources().getString(R.string.delete));
			setBack(View.GONE);
			setConfirm(View.VISIBLE);
			setCancel(View.VISIBLE);
			noteList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			isCheck = true;
			break;
		case LISTVIEW_CHECK_MODE:
			title.setText(getResources().getString(R.string.check_msg));
			setBack(View.GONE);
			setConfirm(View.VISIBLE);
			setCancel(View.VISIBLE);
			noteList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			isCheck = true;
			break;
		}
		String[] from = { NOTE_CONTENT };
		int[] to = { R.id.text1 };
		sCursorAdapter = new ToDoCursorAdapter(this, R.layout.item, mCursor,
				from, to, isCheck);
		noteList.setAdapter(sCursorAdapter);

		noteList.setOnItemClickListener(this);
		noteList.setTextFilterEnabled(true);

		emptyView = findViewById(R.id.empty);
		noteList.setEmptyView(emptyView);
	}

	private void setCancel(int gone) {
		// TODO Auto-generated method stub
		confirmBtn.setVisibility(gone);
	}

	private void setConfirm(int gone) {
		// TODO Auto-generated method stub
		cancelBtn.setVisibility(gone);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");

		initAdapter();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		adapter.close();
		Log.i(TAG, "onDestroy");
		idList.clear();
		checkList.clear();
	}

	/**
	 * Get current date,format is "yyyy骞�M��d��
	 */
	private void initDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date();
		String str = formatter.format(curDate);
		title.setText(str);
	}

	/**
	 * find all views in the layout
	 */
	private void findviews() {
		title = (TextView) findViewById(R.id.title);
		noteList = (ListView) findViewById(R.id.list);
		confirmBtn = (Button) findViewById(R.id.confirm);
		cancelBtn = (Button) findViewById(R.id.cancel);
		back = (Button) findViewById(R.id.titlbarback);
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		back.setOnClickListener(this);

		addBtn = (Button) findViewById(R.id.add);
		menuBtn = (Button) findViewById(R.id.menu);
		addBtn.setOnClickListener(this);
		menuBtn.setOnClickListener(this);

		menuView = View.inflate(this, R.layout.menu, null);
		menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		// menuDialog.setView(menuView, 0, 0, 0, 0);
		menuDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)
					dialog.dismiss();
				return false;
			}
		});

		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case TOOLBAR_ITEM_ADD:
					editNotes();
					break;
				case TOOLBAR_ITEM_EDIT:
					deleteNotes();
					break;
				case TOOLBAR_ITEM_DELETE:
					viewAllNotes();
					break;
				case TOOLBAR_ITEM_ALL:
					share();
					break;
				case TOOLBAR_ITEM_SHARE:
					checkNotes();
					break;
				case TOOLBAR_ITEM_ABOUT:
					about();
					break;
				}
				menuDialog.dismiss();
			}
		});
	}

	private void about() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, MoreActivity.class);
		startActivity(intent);
	}

	private void share() {
		StringBuilder sb = new StringBuilder();
		if (mCursor != null) {
			if (mCursor.moveToFirst()) {
				do {
					sb.append(
							mCursor.getString(mCursor
									.getColumnIndex(NOTE_CONTENT))).append(" ");
					if (mCursor.getInt(mCursor.getColumnIndex(IS_FINISH)) == 1) {
						sb.append("Finish");
					} else {
						sb.append("UnFinish");
					}
				} while (mCursor.moveToNext());
			}
		}
		Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
		intent.setType("text/plain"); // 分享发送的数据类型
		intent.putExtra(Intent.EXTRA_SUBJECT, title.getText().toString()); // 分享的主题
		String extra = "";
		if (sb.equals("")) {
			extra = "Today has nothing to do";
		} else {
			extra = sb.toString();
		}
		intent.putExtra(Intent.EXTRA_TEXT, extra); // 分享的内容
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 这个也许是分享列表的背景吧
		ToDoListActivity.this.startActivity(Intent.createChooser(intent, "分享"));
	}

	private ListAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.toolbar_item,
				new String[] { "itemImage", "itemText" }, new int[] {
						R.id.item_image, R.id.item_text });
		return simperAdapter;
	}

	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.confirm:
			if (mListMode == LISTVIEW_MULTI_MODE) {
				confirmDeleteNotes();
			} else if (mListMode == LISTVIEW_CHECK_MODE) {
				confirmCheck();
			}

			break;
		case R.id.cancel:
			cancel();
			break;
		case R.id.titlbarback:
			back();
			break;
		case R.id.add:
			addOneNote();
			break;
		case R.id.menu:
			showDialog();
			break;
		}
	}

	private void checkNotes() {
		// TODO Auto-generated method stub
		mListMode = LISTVIEW_CHECK_MODE;
		initAdapter();
	}

	private void viewAllNotes() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ViewAllNotes.class);
		startActivity(intent);
	}

	private void setBack(int vis) {
		back.setVisibility(vis);
	}

	private void back() {
		// TODO Auto-generated method stub
		mListMode = LISTVIEW_NORMAL_MODE;
		initAdapter();
	}

	private void editNotes() {
		// TODO Auto-generated method stub
		mListMode = LISTVIEW_SINGLE_MODE;
		initAdapter();
	}

	private void cancel() {
		// TODO Auto-generated method stub
		mListMode = LISTVIEW_NORMAL_MODE;
		initAdapter();
	}

	private void confirmDeleteNotes() {
		Log.i(TAG, "idList.size:" + idList.size());
		for (int i = 0; i < idList.size(); i++) {
			adapter.deleteTodo(idList.get(i));
		}
		mListMode = LISTVIEW_NORMAL_MODE;
		initAdapter();
	}

	private void confirmCheck() {
		ContentValues values = new ContentValues();
		Log.i(TAG, "checkList.size:" + checkList.size());
		for (int i = 0; i < checkList.size(); i++) {
			values.clear();
			int isFinish = checkList.get(i).get(IS_FINISH);
			int noteId = checkList.get(i).get(NOTE_ID);
			if (isFinish == FINISH) {
				values.put(IS_FINISH, UNFINISH);
			} else {
				values.put(IS_FINISH, FINISH);
			}
			adapter.update(TODO_TABLE, values, NOTE_ID + " = " + noteId);
		}
		title.setText(getResources().getString(R.string.date));
		mListMode = LISTVIEW_NORMAL_MODE;
		initAdapter();
	}

	/**
	 * delete notes
	 */
	private void deleteNotes() {
		mListMode = LISTVIEW_MULTI_MODE;
		initAdapter();
	}

	/**
	 * Add note
	 */
	private void addOneNote() {
		Intent intent = new Intent(this, EditNoteActivity.class);
		intent.setAction(ACTION_ADD_NOTE);
		startActivityForResult(intent, ADD_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ADD_REQUEST_CODE:
			Log.i("ToDoList", "resultCode:" + resultCode);
			if (resultCode == Activity.RESULT_OK) {
				mCursor = adapter.queryAll();
				sCursorAdapter.changeCursor(mCursor);
				sCursorAdapter.notifyDataSetChanged();// notify the listview to
														// refresh
			}
			break;
		case EDIT_REQUEST_CODE:
			if (resultCode == Activity.RESULT_OK) {
				// mListMode = LISTVIEW_NORMAL_MODE;
				// initAdapter();
			}
			break;
		}
	}

	private void editOneNote(int noteid, String content) {
		Intent intent = new Intent(this, EditNoteActivity.class);
		intent.setAction(ACTION_EDIT_NOTE);
		intent.putExtra(NOTE_ID, noteid);
		intent.putExtra(NOTE_CONTENT, content);
		startActivityForResult(intent, EDIT_REQUEST_CODE);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ToDoCursorAdapter adapter = ((ToDoCursorAdapter) noteList.getAdapter());
		if (mListMode == LISTVIEW_MULTI_MODE) {
			adapter.update(position,
					!ToDoCursorAdapter.checkItemList.get(position));
			Cursor c = (Cursor) noteList.getItemAtPosition(position);
			int columnIndex = c.getColumnIndex(NOTE_ID);
			int noteId = 0;
			boolean isCheck1 = ToDoCursorAdapter.checkItemList.get(position);
			Log.i(TAG, "isCheck1:" + isCheck1);
			if (!c.isNull(columnIndex)) {
				noteId = c.getInt(columnIndex);
				if (isCheck1) {
					idList.add(noteId);
				} else {
					if (idList != null && idList.size() != 0) {
						idList.remove((Integer) noteId);
					}
				}
			}
			//
			// Log.i(TAG, "position " + position + " checkBox.isChecked():"
			// + vHollder.cb.isChecked() + " view, id :" + id
			// + " noteId :" + noteId);
		} else if (mListMode == LISTVIEW_SINGLE_MODE) {
			Cursor c = (Cursor) noteList.getItemAtPosition(position);
			int columnIndex = c.getColumnIndex(NOTE_ID);
			int contentColumnIndex = c.getColumnIndex(NOTE_CONTENT);
			int noteId = 0;
			String content = null;
			if (!c.isNull(columnIndex)) {
				noteId = c.getInt(columnIndex);
				content = c.getString(contentColumnIndex);
				editOneNote(noteId, content);
			}

		} else if (mListMode == LISTVIEW_CHECK_MODE) {
			adapter.update(position,
					!ToDoCursorAdapter.checkItemList.get(position));
			Cursor c = (Cursor) noteList.getAdapter().getItem(position);
			int columnIndex = c.getColumnIndex(NOTE_ID);
			int isFinishColumnIndex = c.getColumnIndex(IS_FINISH);
			int noteId = 0;
			int isFinish = 0;
			boolean isCheck = ToDoCursorAdapter.checkItemList.get(position);
			Log.i(TAG, "isCheck:" + isCheck);
			if (!c.isNull(columnIndex)) {
				noteId = c.getInt(columnIndex);
				isFinish = c.getInt(isFinishColumnIndex);
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				map.put(IS_FINISH, isFinish);
				map.put(NOTE_ID, noteId);
				if (isCheck) {
					checkList.add(map);
				} else {
					if (checkList != null && checkList.size() != 0) {
						checkList.remove((HashMap<String, Integer>) map);
					}
				}
			}
		}

	}

}