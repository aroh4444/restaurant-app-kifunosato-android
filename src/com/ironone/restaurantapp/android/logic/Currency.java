/*
 * Currency.java
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
 * @Created on Aug 6, 2012 : 12:41:52 PM
 * @author malakag
 */

public class Currency extends RestaurantMenu {

	public Currency(Document doc) {
		xmlDocument = doc;
		xPath = XPathFactory.newInstance().newXPath();
	}

	public Currency() {
		xPath = XPathFactory.newInstance().newXPath();
	}

	private String name = "";
	private String symbol = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return "Currency [name=" + name + ", symbol=" + symbol + "]";
	}
	
	public static List<Currency> prepareCurrencyObjects(List<Currency> currencies) {

		List<Currency> result = new ArrayList<Currency>();
		for (int i = 0; i < currencies.size(); i++) {
			Currency currency = new Currency();
			try {				
				currency.setName(currencies.get(i).getString("currency/name"));
				currency.setSymbol(currencies.get(i).getString("currency/symbol"));
				
			} catch (ArrayIndexOutOfBoundsException e) {
				Log.e(TAG, e.toString() + " at iteration " + i);
			}
			result.add(currency);
		}
		return result;
	}

}
