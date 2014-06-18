/*
 * FormatUtil.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.util;


/**
 * {Insert class description here}
 *
 * @Created on May 30, 2014 : 4:21:57 PM
 * @author Gayanp
 */

public class FormatUtil {
	
	public static String getNumberFormatted(Double number) {
		return String.format("%d", number.intValue());
	}

	
	public static String getNumberFormatted(String number) {
		try {
			if(number ==  null || number.isEmpty()) {
				number = "0";
			}
			return String.format("%.2f", Double.parseDouble(number));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
	}
	
}
