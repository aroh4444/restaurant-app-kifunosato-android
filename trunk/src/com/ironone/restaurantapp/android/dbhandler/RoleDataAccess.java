
package com.ironone.restaurantapp.android.dbhandler;

/*
 * WaiterDataAccess.java
 *
 * Copyright(c) IronOne Technologies Pvt Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies Pvt Ltd.
 *
 */

import java.security.MessageDigest;

import com.ironone.restaurantapp.android.log.AppLog;
import com.ironone.restaurantapp.android.log.ErrorCodes;
import com.ironone.restaurantapp.android.log.LogFile;
import com.ironone.restaurantapp.android.log.RestaurantAppGenericException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class handles waiter data access
 * 
 * @Created on June 28, 2012 : 12:13:00 PM
 * @author sampathb.
 */

public class RoleDataAccess {

	Context context;
	DBController dbController;
	
	String TAG= "com.ironone.restaurantapp.android.dbhandler/RoleDataAccess";
	
	// capture waiter table fields
		public static final String rolesTable = TableCreator.rolesTable;
		public static final String roleName = TableCreator.roleName;
		public static final String roleID = TableCreator.roleID;


	public RoleDataAccess(Context context) {
		super();
		this.context = context;
		dbController = new DBController(context);
	}
	
	/** GetName query */
	public String getName(String id) {

		String result = null;
	
		try {
			SQLiteDatabase sqLiteDatabase = dbController.getDBToRead();
			String[] DB_TEXT_COLUMNS = new String[] { roleName };

			Cursor cursor = sqLiteDatabase.query(true, rolesTable,
					DB_TEXT_COLUMNS, "(" + roleID + " = \"" + id
							+ "\")", null, null, null, null, null);

			int index_PASSWORD = cursor.getColumnIndex(roleName);
			for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
				result = cursor.getString(index_PASSWORD);
			}
			
			cursor.close();
			dbController.closeDB();
		} catch (Exception e) {
			RestaurantAppGenericException ex = new RestaurantAppGenericException(
					ErrorCodes.SQL_ERROR, e.getMessage());
			AppLog.error("RoleDataAccess", "getName", "get name from db", ex, id);
			LogFile.d(TAG+"/getName()", e.getMessage());

		}
		
		return result;
	}
	
	
	public String getPassword(String username) {

		String result = "";
	
		try {
			SQLiteDatabase sqLiteDatabase = dbController.getDBToRead();
			String[] DB_TEXT_COLUMNS = new String[] { "password" };

			Cursor cursor = sqLiteDatabase.query(true, "USERS",
					DB_TEXT_COLUMNS, "(" + "username" + " = \"" + username
							+ "\")", null, null, null, null, null);

			int index_PASSWORD = cursor.getColumnIndex("password");
			for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
				result = cursor.getString(index_PASSWORD);
			}
			
			cursor.close();
			dbController.closeDB();
		} catch (Exception e) {

		}
		
		return result;
	}
	
	public static String sha256(String base) {
	    try{
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }

	        return hexString.toString();
	    } catch(Exception ex){
	       throw new RuntimeException(ex);
	    }
	}

}
