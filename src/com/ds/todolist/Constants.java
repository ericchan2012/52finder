package com.ds.todolist;

public interface Constants {
	
	int TYPE_TODO = 0;
	int TYPE_NEWME = 1;
	int TYPE_USER = 2;
	int TYPE_INS = 3;

	String ACTION_ADD_NOTE = "com.ds.todolist.add_note";
	String ACTION_EDIT_NOTE = "com.ds.todolist.edit_note";
	String ACTION_DELETE_NOTE = "com.ds.todolist.delete_note";
	int INVALID_ID = -1;

	int LISTVIEW_NORMAL_MODE = 0;
	int LISTVIEW_SINGLE_MODE = 1;
	int LISTVIEW_MULTI_MODE = 2;
	int LISTVIEW_CHECK_MODE = 3;

	int FINISH = 1;
	int UNFINISH = 0;

	int ADD_REQUEST_CODE = 0;
	int EDIT_REQUEST_CODE = 1;
	int DELETE_REQUEST_CODE = 2;

	String DATABASE_NAME = "todolist";
	int DATABASE_VERSION = 1;
	String TODO_TABLE = "todo";
	String NEWME_TABLE = "newme";
	String USR_TABLE = "user";
	String INSPIRATION_TABLE = "inspiration";

	String NOTE_ID = "_id";
	String NOTE_CONTENT = "content";
	String NOTE_DATE = "note_date";
	String IS_FINISH = "is_finish";
	String NOTE_MONTH = "note_month";

	String NEWME_ID = "_id";
	String NEWME_CONTENT = "newme_content";
	String NEWME_DATE = "newme_date";
	String IS_SECRET = "is_secret";
	String USR_ID = "user_id";

	String USR_KEY_ID = "_id";
	String USR_NAME = "username";
	String USR_EMAIL = "useremail";
	String USR_PASSWD = "usr_passwd";

	String INSPIRATION_ID = "_id";
	String INSPIRATION_CONTENT = "ins_content";
	String INSPIRATION_DATE = "ins_date";

	String[] QUERY_ALL_PROJECTION = new String[] { NOTE_ID, NOTE_CONTENT,
			NOTE_DATE, IS_FINISH };

	String[] QUERY_UNDO_PROJECTION = new String[] { NOTE_ID };

	String[] QUERY_CONTENT_PROJECTION = new String[] { NOTE_CONTENT, IS_FINISH };

	String DATABASE_CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + " ("
			+ NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOTE_CONTENT
			+ " TEXT NOT NULL," + IS_FINISH + " INTEGER DEFAULT 0," + NOTE_DATE
			+ " TEXT NOT NULL," + NOTE_MONTH + " TEXT NOT NULL" + ");";

	String DATABASE_CREATE_TABLE_NEWME = "CREATE TABLE " + NEWME_TABLE + " ("
			+ NEWME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NEWME_CONTENT
			+ " TEXT NOT NULL," + NEWME_DATE + " TEXT NOT NULL," + IS_SECRET
			+ " INTEGER DEFAULT 0," + USR_ID + " INTEGER NOT NULl" + ");";

	String DATABASE_CREATE_TABLE_USER = "CREATE TABLE " + USR_TABLE + " ("
			+ USR_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USR_NAME
			+ " TEXT NOT NULL," + USR_EMAIL + " TEXT NOT NULL," + USR_PASSWD
			+ " TEXT NOT NULL" + ");";

	String DATABASE_CREATE_TABLE_INSPIRATION = "CREATE TABLE "
			+ INSPIRATION_TABLE + " (" + INSPIRATION_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + INSPIRATION_CONTENT
			+ " TEXT NOT NULL," + INSPIRATION_DATE + " TEXT NOT NULL" + ");";

}
