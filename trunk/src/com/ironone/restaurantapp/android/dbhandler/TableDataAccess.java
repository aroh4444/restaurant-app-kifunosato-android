/*
 * TableDataAccess.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.dbhandler;

import com.ironone.restaurantapp.android.log.LogFile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * {Insert class description here}
 * 
 * @Created on Jul 9, 2012 : 2:32:23 PM
 * @author malakag.
 */

public class TableDataAccess {

	Context context;
	DBController dbController;

	final static String tableName = TableCreator.tableNumberTable;
	final static String tableNumber = TableCreator.tableNo;
	final static String id = "id";

	String TAG = "com.ironone.restaurantapp.android.dbhandler TableDataAccess.java";

	public TableDataAccess(Context context) {
		super();
		this.context = context;
		dbController = new DBController(context);
	}

	public boolean insert(String value) {
		try {
			SQLiteDatabase sqLiteDatabase = dbController.getDBToWrite();
			ContentValues cv = new ContentValues();
			cv.put(tableNumber, value);
			sqLiteDatabase.insert(tableName, null, cv);
			//sqLiteDatabase.close();
			dbController.closeDB();
		} catch (Exception e) {
			LogFile.d(TAG + "in Insert method", e.getMessage());
		}

		return true;
	}
	

	public String getTableNo() {

		String result = null;

		try {
			SQLiteDatabase sqLiteDatabase = dbController.getDBToRead();

			String[] DB_TEXT_COLUMNS = new String[] { tableNumber };

			Cursor cursor = sqLiteDatabase.query(true, tableName,
					DB_TEXT_COLUMNS, null, null, null, null, null, null);

			int index_table_no = cursor.getColumnIndex(tableNumber);
			cursor.moveToLast();
			result = cursor.getString(index_table_no);

			cursor.close();
			dbController.closeDB();

		} catch (Exception e) {
			Log.e(" ERROR LoginDataAccess ", "getPassword " + e.toString());
			LogFile.d(TAG + "in getTableNo() method", e.getMessage());

		}

		return result;
	}
}
