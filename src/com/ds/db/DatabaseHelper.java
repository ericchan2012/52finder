package com.ds.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ds.todolist.Constants;

public class DatabaseHelper extends SQLiteOpenHelper implements Constants {

	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE_TODO_TABLE);
		db.execSQL(DATABASE_CREATE_TABLE_NEWME);
		db.execSQL(DATABASE_CREATE_TABLE_USER);
		db.execSQL(DATABASE_CREATE_TABLE_INSPIRATION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + NEWME_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + USR_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + INSPIRATION_TABLE);
			onCreate(db);
		}
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}

}
