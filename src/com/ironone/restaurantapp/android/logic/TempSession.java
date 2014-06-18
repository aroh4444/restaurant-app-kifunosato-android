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

public class TempSession {

	private static String userID = "";
	private static String password = "";

	public static String getUserID() {
		return userID;
	}

	public static void setUserID(String userID) {
		TempSession.userID = userID;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		TempSession.password = password;
	}

	
}

