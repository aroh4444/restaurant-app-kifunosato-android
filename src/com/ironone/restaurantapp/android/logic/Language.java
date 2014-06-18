/*
 * Language.java
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

import android.util.Log;

/**
 * {Insert class description here}
 * 
 * @Created on Jul 17, 2012 : 4:35:28 PM
 * @author malakag
 */

public class Language extends RestaurantMenu {

	private String LanID = "";
	public static String TAG = "com.ironone.restaurantapp.android.logic/Language.java";

	public Language(Document doc) {
		xmlDocument = doc;
		xPath = XPathFactory.newInstance().newXPath();
	}

	public Language() {
		xPath = XPathFactory.newInstance().newXPath();
	}

	public String getLanID() {
		return LanID;
	}

	public void setLanID(String lanID) {
		LanID = lanID;
	}


	public static List<Language> prepareLanguageObjects(List<Language> languages) {

		List<Language> result = new ArrayList<Language>();
		for (int i = 0; i < languages.size(); i++) {
			Language language = new Language();
			try {
				language.setLanID(languages.get(i).getString("language/id"));
				language.setName(languages.get(i).getString("language/name"));
				List<Menu> image = languages.get(i).getAll("language/flag");
				Image img = new Image();
				img.setLocation(image.get(0).getString("flag/location"));
				img.setType(image.get(0).getString("flag/type"));
				language.setImage(img);
			} catch (ArrayIndexOutOfBoundsException e) {
				Log.e(TAG, e.toString() + " at iteration " + i);
			}
			result.add(language);
		}
		return result;
	}
	
	
	/*public static void convertList(Order order, List<Menu> baseMenu) {

		for (int i = 0; i < baseMenu.size(); i++) {
			if (baseMenu.get(i).hasMenus()) {
				convertList(order, baseMenu.get(i).getMenus());
			}
			else if(baseMenu.get(i).hasItems()){
				for(Item item : baseMenu.get(i).getItems()){
					for(int j=0; j<order.getMiniOrder().size(); j++){
					if(item.getId() == order.getMiniOrder().get(j).getItem().getId()){
						order.getMiniOrder().set(j, convertMiniOrder(order.getMiniOrder().get(j), item));
					}}
				}
			}
		}
	}
	

	public static MiniOrder convertMiniOrder(MiniOrder miniOrder, Item item) {
		int selected = 0;
		for (int i = 0; i < miniOrder.getItem().getSizes().size(); i++) {
			if (miniOrder.getItem().getSizes().get(i).getName() == miniOrder
					.getItem().getSelectedSize().getName()) {
				selected = i;
			}
		}
		item.setSelectedSize(item.getSizes().get(selected));
		MiniOrder result = new MiniOrder(item, miniOrder.getNoOfItems());

		return result;
	}*/
}
