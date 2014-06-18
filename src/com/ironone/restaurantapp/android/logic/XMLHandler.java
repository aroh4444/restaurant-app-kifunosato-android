/*
 * XMLHandler.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

/**
 * Create an xml reader and load the xml to the application
 *
 * @Created on Jun 27, 2012 : 10:14:23 AM
 * @author malakag
 */

public class XMLHandler{
	
	Context context;
	public static String TAG = "com.ironone.restaurantapp.android.logic/XMLHandler.java";

	
	public XMLHandler(Context context) {
		super();
		this.context = context;
	}

	public Menu loadXML(){
		
		Menu menu= new Menu();
		Resources res = context.getResources();
		try {
			InputStream is = res.getAssets().open("menu.xml");
			menu.setXmlDocument(is);
		} catch (SAXException e) {
			Log.e(TAG, e.toString());
		} catch (ParserConfigurationException e) {
			Log.e(TAG, e.toString());
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		}
		return menu;
	}

}
