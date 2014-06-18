/*
 * AppLog.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ironone.restaurantapp.android.logic.Session;

import android.util.Log;

/**
 * {Insert class description here}
 *
 * @Created on Sep 11, 2012 : 10:55:29 AM
 * @author malakag
 */

public class AppLog {

	public static boolean info(String className, String methodName, String event, String message, String...param){
		
		String user = Session.getUserID();
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String time = s.format(new Date());
		for(String temp : param){
			message += ", " + temp;
		}
		Log.i("EVT", "Event : " + event + ", Time : " + time + ",Class : " + className + ", Method : " + methodName + ", Code : 0" + ", User : " + user + ", Message : " + message );
		return true;
	}
	
	public static boolean error(String className, String methodName, String event, RestaurantAppGenericException ex, String...param){
		
		String user = Session.getUserID();
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		String time = s.format(new Date());
		String message = ex.getMessage();
		for(String temp : param){
			message += ", " + temp;
		}
		Log.i("ERR", "Event : " + event + ", Time : " + time + ",Class : " + className + ", Method : " + methodName + ", Code : " + ex.getErrorCode() + ", User : " + user + ", ErrorMessage : " + message);
		return true;
	}
}

