/*
 * Session.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;

/**
 * {Insert class description here}
 *
 * @Created on Sep 11, 2012 : 12:11:43 PM
 * @author malakag
 */

public class Session {

	private static String userID = "non_logged_in_user";
	private static String password = "";
	private static int restaurantID = 0;
	private static String table="";

	public static int getRestaurantID() {
		return restaurantID;
	}

	public static void setRestaurantID(int restaurantID) {
		Session.restaurantID = restaurantID;
	}

	public static String getUserID() {
		return userID;
	}

	public static void setUserID(String userID) {
		Session.userID = userID;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		Session.password = password;
	}

	public static String getTable() {
		return table;
	}

	public static void setTable(String table) {
		Session.table = table;
	}

	
}

