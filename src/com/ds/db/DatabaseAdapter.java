package com.ds.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.ds.todolist.Constants;

public class DatabaseAdapter implements Constants {
	private SQLiteDatabase mDb;
	private Context mContext;
	private DatabaseHelper mDbHelper;
	
	public DatabaseAdapter(Context ctx) {
		mContext = ctx;
		mDbHelper = new DatabaseHelper(mContext);
	}

	public DatabaseAdapter open() throws SQLException {
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long insert(String tableName, ContentValues values) {
		long id = mDb.insert(tableName, null, values);
		return id;
	}

	public boolean update(String tableName, ContentValues values, String where) {

		return mDb.update(tableName, values, where, null) > 0;

	}

	public boolean deleteTodo(long rowId) {
		return mDb.delete(TODO_TABLE, NOTE_ID + "=" + rowId, null) > 0;
	}

	public Cursor queryUndo() {
		return mDb.query(TODO_TABLE, QUERY_UNDO_PROJECTION, null, null, null,
				null, null);
	}

	public Cursor queryAll() {
		return mDb.query(TODO_TABLE, QUERY_ALL_PROJECTION, null, null, null,
				null, null);
	}
	
	public Cursor queryWhere(String month) {
		return mDb.query(TODO_TABLE, QUERY_ALL_PROJECTION, NOTE_MONTH +" = '" + month +"'", null, null,
				null, null);
	}
	
	public Cursor queryWhereDate(String date) {
		return mDb.query(TODO_TABLE, QUERY_ALL_PROJECTION, NOTE_DATE +" = '" + date +"'", null, null,
				null, null);
	}
}
