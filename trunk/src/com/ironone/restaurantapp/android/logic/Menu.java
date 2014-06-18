/*
 * Menu.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;


import android.content.Context;
import android.util.Log;

/**
 * Menu item is represented my Menu object
 * 
 * @Created on Jun 27, 2012 : 10:14:23 AM
 * @author malakag
 */

public class Menu extends RestaurantMenu {

	@SuppressWarnings("unused")
	private Context context=null;
	
	public Menu(Context context){
		this.context = context;
	}
	
	public Menu(Document doc) {
		xmlDocument = doc;
		xPath = XPathFactory.newInstance().newXPath();
	}

	public Menu() {
		xPath = XPathFactory.newInstance().newXPath();
	}

	public static String TAG = "com.ironone.restaurantapp.android.logic/Menu.java";

	private boolean isSuggestion = true;;

	public boolean isSuggestion() {
		return isSuggestion;
	}

	public void setSuggestion(boolean isSuggestion) {
		this.isSuggestion = isSuggestion;
	}

	private List<Menu> menus = new ArrayList<Menu>();
	private List<Item> items = new ArrayList<Item>();

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Menu> prepareMenuObjects(List<Menu> menus) {

		//LanguageTranslator translator = new LanguageTranslator(this.context);
		List<Menu> result = new ArrayList<Menu>();
		for (int i = 0; i < menus.size(); i++) {
			Menu menu = new Menu();
			try {
				int id = menus.get(i).getInt("submenu/id");
				menu.setId(id);
				menu.setName("");
				menu.setEnable(menus.get(i).getBoolean("submenu/isEnable"));
				menu.setSuggestion(menus.get(i).getBoolean(
						"submenu/isSuggestion"));
				List<Menu> image = menus.get(i).getAll("submenu/image");
				Image img = new Image();
				img.setLocation(image.get(0).getString("image/location"));
				img.setType(image.get(0).getString("image/type"));
				menu.setMenus(menus.get(i).getSubMenu("submenu/menus/submenu"));
				menu.setItems(menus.get(i).getItem("submenu/items/item"));
				menu.setImage(img);
			} catch (ArrayIndexOutOfBoundsException e) {
				Log.e(TAG, e.toString() + " at iteration " + i);
			}
			if (menu.isEnable()) {
				result.add(menu);
			}
		}
		return result;
	}

	public static List<Menu> getChildMenu(List<Menu> menus, int id) {

		for (int i = 0; i < menus.size(); i++) {
			if (menus.get(i).getId() == id && menus.get(i).hasMenus()) {
				return menus.get(i).getMenus();
			}
		}
		if ( menus.size() > 0 ) {
			return getChildMenu(menus.get(0).getMenus(), id);

		}
		return null;
	}

	public static List<Item> getChildItems(List<Menu> menus, int id) {

		for (int i = 0; i < menus.size(); i++) {
			if (menus.get(i).getId() == id && menus.get(i).hasItems()) {
				return menus.get(i).getItems();
			}
		}
		for (int i = 0; i < menus.size(); i++) {
			List<Item> temp = getChildItems(menus.get(i).getMenus(), id);
			if (temp != null) {
				return temp;
			}
		}
		return null;
	}
	
	public static Menu getParentMenuFromId(List<Menu> menus, int itemId) {

		
		for (int i = 0; i < menus.size(); i++) {
			List<Item> temp = menus.get(i).getItems();
			for (Item item : temp) {
				if(item.getId()==itemId) {
					return menus.get(i);
				}
			}
			
		}
		for (int i = 0; i < menus.size(); i++) {
			Menu menu = getParentMenuFromId(menus.get(i).getMenus(), itemId);
			if(menu !=null) {
				return menu;
			}
		}
		
		return null;
	}
	

	public boolean hasItems() {
		if (this.getItems().size() > 0) {
			return true;
		}
		return false;
	}

	public boolean hasMenus() {
		if (this.getMenus().size() > 0) {
			return true;
		}
		return false;
	}

	public int getParentID(int childID) {
		for (int i = 0; i < StaticData.fullmenu.size(); i++) {
			for (int j = 0; j < StaticData.fullmenu.get(i).getMenus()
					.size(); j++) {
				if (StaticData.fullmenu.get(i).getMenus().get(j).getId() == childID)
					return StaticData.fullmenu.get(i).getId();
			}
		}
		return 0;
	}

}
