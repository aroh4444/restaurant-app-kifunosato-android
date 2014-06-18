/*
 * StaticData.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;


/**
 * {Insert class description here}
 *
 * @Created on Sep 7, 2012 : 4:05:08 PM
 * @author malakag
 */

public class StaticData {

	/** static refferences **/
	public static String selectedMenu="";
	public static List<Menu> fullmenu = new ArrayList<Menu>();
	public static List<Menu> copyMenu = new ArrayList<Menu>();
	public static List<Menu> lastMenu = new ArrayList<Menu>();
	public static List<Menu> addOns = new ArrayList<Menu>();
	public static Item item = new Item();
	public static List<Item> items = new ArrayList<Item>();
	public static Order currentOrder = new Order();
	public static TableOrder submitedOrders = new TableOrder();
	public static TableOrder successOrders = new TableOrder();
	public static TableOrder failOrders =  new TableOrder();
	public static List<Language> languages = new ArrayList<Language>();
	public static Currency currency = new Currency();
	public static String currentLanguageID = "en";
	public static int clickedIndex = -1;
	public static Context context = null;
	public static DefaultHttpClient httpClient = null;
	public static String serverURL = "";
	public static Integer restaurantId = -1;
	public static String deviceid ="45:45:45:45:45:45";
	public static String totalPrice="";
	public static String tableNo = "";
	
	public static String loginType="";
	public static String loginMessage ="";
	public static String errorCode ="";
	
	public static String errorTag ="";
	
}

