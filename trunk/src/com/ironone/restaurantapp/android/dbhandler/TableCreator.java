package com.ironone.restaurantapp.android.dbhandler;

/*
 * TableCreator.java
 *
 * Copyright(c) IronOne Technologies Pvt Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies Pvt Ltd.
 *
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import com.ironone.restaurantapp.android.filereader.FileReader;
import com.ironone.restaurantapp.android.log.AppLog;
import com.ironone.restaurantapp.android.log.ErrorCodes;
import com.ironone.restaurantapp.android.log.RestaurantAppGenericException;

/**
 * This class include all database variables, table names and table fields. When
 * we add new DB table in the text file, just put their table names and field
 * names here.
 * 
 * @Created on June 26, 2012 : 09:03:45 AM
 * @author sampathb.
 */

public class TableCreator {

	/** Database variables */
	static final String dbName = "RESTAURANT_DB";
	static final int dbVersion = 1;

	/** CREATE TABLE queries list. */	
	static List<String> createTableQueryList = new ArrayList<String>();

	/** Table names */
	static final String rolesTable = "ROLES";
	static final String rolePermissionsTable = "ROLEPERMISSIONS";
	static final String userRolesTable = "USERROLES";
	static final String usersTable = "USERS";
	static final String tableNumberTable = "TABLE_NUMBER";

	/** Roles Table fields */
	static final String roleID = "roleid";
	static final String roleName = "rolename";

	/** Table Number fields **/
	static final String ID = "id";
	static final String tableNo = "table_no";

	/** RolePermissions Table fields */
	static final String rolePermissionID = "rolepermissionid";
	static final String permissionRoleID = "roleid";
	static final String permission = "permision";

	FileReader filereader;
	Context tableContext;

	String TAG = "com.ironone.restaurantapp.android.dbhandler TableCreator.java";

	/** TableCreator Constructor */
	public TableCreator(Context c) {
		super();
		this.tableContext = c;
		filereader = new FileReader(c);
		prepareCreateTableQuearyList();
	}

	/**
	 * This method is used to set table creation queries from text files to the
	 * class variables.
	 */
	public void prepareCreateTableQuearyList() {

		try {
			
			String queryString = filereader.readTextFile();
			String[] queryArray = queryString.split("@@");

			for (String s : queryArray) {				
				createTableQueryList.add(s);
			}

		} catch (Exception e) {
			RestaurantAppGenericException ex = new RestaurantAppGenericException(
					ErrorCodes.UNKNOWN_ERROR, e.getMessage());
			AppLog.error("TableCreator", "prepareCreateTableQuearyList",
					"Preparing CREATE TABLE query list", ex);
		}

	}

}
