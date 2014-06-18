/*
 * RestaurantAppGenericException.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.log;

/**
 * generic exception class for application
 *
 * @Created on Sep 18, 2012 : 11:35:53 AM
 * @author malakag
 */

public class RestaurantAppGenericException extends Exception{

	/**
	 * 
	 */
	
	public RestaurantAppGenericException(int errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
	
	private static final long serialVersionUID = -2303660970922828028L;
	private int errorCode = -1;
	private String message = "";
	
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}

