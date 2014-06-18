package com.ironone.restaurantapp.android.dbhandler;

/*
 * DBController.java
 *
 * Copyright(c) IronOne Technologies Pvt Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies Pvt Ltd.
 *
 */


import com.ironone.restaurantapp.android.log.AppLog;
import com.ironone.restaurantapp.android.log.ErrorCodes;
import com.ironone.restaurantapp.android.log.RestaurantAppGenericException;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * this class handles all the database creations, upgrading, and connections to
 * the database.
 * 
 * @Created on June 25, 2012 : 11:18:23 AM
 * @author sampathb
 */

public class DBController {

	private SQLiteHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	private Context context;

	/** database variables */	
	public static final String dbName = "RES_App_DB";
	public static final int dbVersion = 1;

	public DBController(Context context) {
		this.context = context;
		sqLiteHelper = new SQLiteHelper(context, dbName, null, dbVersion);

	}

	public static class SQLiteHelper extends SQLiteOpenHelper {
		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {

				for (int i = 0; i < TableCreator.createTableQueryList.size(); i++) {
					db.execSQL(TableCreator.createTableQueryList.get(i));
				}

				
				/** add tempory data for the first time */	
				db.execSQL("INSERT INTO TABLE_NUMBER VALUES (\"1\",\"00\");");
				db.execSQL("INSERT INTO USERS(username,password) VALUES (\"waiter\",\"123\");");
				
				

			} catch (SQLException e) {
				RestaurantAppGenericException ex = new RestaurantAppGenericException(
						ErrorCodes.SQL_ERROR, e.getMessage());
				AppLog.error("DBControllerr", "onCreate",
						"Creating tales and inserting values", ex);
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}

	}

	/**
	 * give reference to the database to read
	 * 
	 * @return
	 * @throws android.database.SQLException
	 */
	public SQLiteDatabase getDBToRead() throws android.database.SQLException {

		sqLiteHelper = new SQLiteHelper(context, dbName, null, dbVersion);

		try {
			sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		} catch (SQLException e) {
			RestaurantAppGenericException ex = new RestaurantAppGenericException(
					ErrorCodes.SQL_ERROR, e.getMessage());
			AppLog.error("DBController", "getDBToRead",
					"Opening database to read", ex);
		}
		return sqLiteDatabase;

	}

	/**
	 * give reference to the database to write
	 * 
	 * @return
	 * @throws android.database.SQLException
	 */
	public SQLiteDatabase getDBToWrite() throws android.database.SQLException {
		sqLiteHelper = new SQLiteHelper(context, dbName, null, dbVersion);
		try {
			sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		} catch (SQLException e) {
			RestaurantAppGenericException ex = new RestaurantAppGenericException(
					ErrorCodes.SQL_ERROR, e.getMessage());
			AppLog.error("DBController", "getDBToWrite",
					"Opening database to write", ex);
		}

		return sqLiteDatabase;
	}

	public void closeDB() {
		if (this.sqLiteHelper != null) {
			try {
				sqLiteDatabase = sqLiteHelper.getReadableDatabase();
				sqLiteDatabase.close();
			} catch (SQLException e) {
				RestaurantAppGenericException ex = new RestaurantAppGenericException(
						ErrorCodes.SQL_ERROR, e.getMessage());
				AppLog.error("DBController", "closeDB",
						"Closing database", ex);
			}

		}
	}

}
