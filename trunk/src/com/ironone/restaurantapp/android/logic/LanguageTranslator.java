/*
 * LanguageTranslator.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;

import java.util.List;


import android.content.Context;

/**
 * {Insert class description here}
 * 
 * @Created on Sep 7, 2012 : 2:59:04 PM
 * @author malakag
 */

public class LanguageTranslator {

	private Context context;

	public LanguageTranslator(Context context) {
		this.context = context;
	}

	
	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}


	public String getPreparedString(String inString) {

		try {
			int res = StaticData.context.getResources().getIdentifier(inString, "string",
					StaticData.context.getPackageName());
			String retString = StaticData.context.getResources().getString(res);
			return retString;
		} catch (Exception e) {
			return "";
		}

	}

	public List<Menu> getMainMenu() {
		Menu menu = new Menu(this.context);
		XMLHandler xml = new XMLHandler(this.context);
		menu = xml.loadXML();
		List<Menu> menuList = menu.getSubMenu("restaurant/menus/submenu");
		return menuList;
	}
	
	public List<Menu> getAddOnsList() {
		Menu menu = new Menu(this.context);
		XMLHandler xml = new XMLHandler(this.context);
		menu = xml.loadXML();
		List<Menu> menuList = menu.getAll("restaurant/add_ons_group/add_ons");
		return menuList;
	}

}
