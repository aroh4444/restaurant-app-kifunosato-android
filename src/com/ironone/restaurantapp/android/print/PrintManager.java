/*
 * PrintManager.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.print;


import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.util.Log;
import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.logic.AddOn;
import com.ironone.restaurantapp.android.logic.MiniOrder;
import com.ironone.restaurantapp.android.logic.Order;
import com.ironone.restaurantapp.android.logic.Session;
import com.ironone.restaurantapp.android.logic.StaticData;
import com.ironone.restaurantapp.android.util.FormatUtil;

/**
 * Manage Printing for Woosim
 * 
 * @Created on May 28, 2014 : 12:47:56 PM
 * @author Gayanp
 */

public class PrintManager {

	private int totalWidth = 31;
	// private int itemWidth = 20;
	private int qtyWidth = 3;
	private int sizeWidth = 8;
	private int priceWidth = 7;
	private int gap = 1;
	Context context;

	public PrintManager(Context context) {
		this.context = context;
	}

	private String getStringFromStream(InputStream inStream) {
		java.util.Scanner s = new java.util.Scanner(inStream)
				.useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public String getPrintData(Order order) {
		StringBuffer strbuffAll = new StringBuffer();

		StringBuffer strbuff = new StringBuffer();

		InputStream inStream = context.getResources().openRawResource(
				R.raw.print);

		for (MiniOrder miniOrder : order.getMiniOrder()) {
			strbuff.append(getHTMLForItem(miniOrder));
		}

		String[] formats = new String[] { "MM/dd/yyyy", "hh:mm:ss aa" };

		strbuffAll.append(String.format(getStringFromStream(inStream),
				Session.getUserID(), "N/A",
				new SimpleDateFormat(formats[0]).format(new Date()),
				new SimpleDateFormat(formats[1]).format(new Date()),				
				strbuff.toString(), FormatUtil.getNumberFormatted(StaticData.totalPrice)));
		Log.e("ashfhs", strbuff.toString());
		Log.e("ashfhs", strbuffAll.toString());
		return strbuffAll.toString();
	}

	private String getHTMLForItem(MiniOrder miniOrder) {

		InputStream inStream = context.getResources().openRawResource(
				R.raw.item_row);

		StringBuffer strbuff = new StringBuffer();
		strbuff.append(String.format(getStringFromStream(inStream), miniOrder
				.getItem().getName(), miniOrder.getItem().getSelectedSize()
				.getName() != null ? miniOrder.getItem().getSelectedSize()
				.getName() : "",  miniOrder.getNoOfItems(), FormatUtil.getNumberFormatted( miniOrder.getItem()
				.getSizes().size() > 0 ? miniOrder.getItem().getSelectedSize()
				.getPrice() :miniOrder.getItem().getPrice()),
				FormatUtil.getNumberFormatted(miniOrder.getNoOfItems()
				* (miniOrder.getItem().getSizes().size() > 0 ? miniOrder
				.getItem().getSelectedSize().getPrice() : miniOrder.getItem()
				.getPrice()))));

		if (miniOrder.getItem().getSelectedAddOnes().size() > 0) {
			InputStream inStreamAddOn = context.getResources().openRawResource(
					R.raw.addon_row);
			String htmlAddOn = getStringFromStream(inStreamAddOn);
			for (int i = 0; i < miniOrder.getItem().getSelectedAddOnes().size(); i++) {
				AddOn addOn = miniOrder.getItem().getSelectedAddOnes().get(i);
				String s = String.format(
						htmlAddOn,
						addOn.getName(),miniOrder.getNoOfItems().toString(), 
						FormatUtil.getNumberFormatted(addOn.getPrice()),
						FormatUtil.getNumberFormatted(miniOrder.getNoOfItems()
								* addOn.getPrice()));
				strbuff.append(s);
			}

		}
		if (miniOrder.getSpecialInstruction() != null
				&& miniOrder.getSpecialInstruction().length() > 0) {
			InputStream inStreamInstruction = context.getResources()
					.openRawResource(R.raw.instruction_row);

			strbuff.append(String.format(
					getStringFromStream(inStreamInstruction),
					miniOrder.getSpecialInstruction()));
		}

		return strbuff.toString();
	}

	/**
	 * get Titles
	 * 
	 * @return
	 */
	private String getTitles() {
		StringBuffer strbuff = new StringBuffer();
		strbuff.append("---Printout For MyBurgerLab---");

		return strbuff.toString();
	}

	/**
	 * get Table Titles
	 * 
	 * @return
	 */
	private String getTableTitles() {
		StringBuffer strbuff = new StringBuffer();
		strbuff.append("   Item      Size   Qty  Price ");

		return strbuff.toString();
	}

	/**
	 * get Dot Line Characters
	 * 
	 * @return
	 */
	private String getDotLine() {
		StringBuffer strbuff = new StringBuffer();
		for (int i = 0; i < totalWidth; i++) {
			strbuff.append(".");
		}
		return strbuff.toString();
	}

	/**
	 * get End Line Characters
	 * 
	 * @return
	 */
	private String getLineEnd() {
		StringBuffer strbuff = new StringBuffer();
		strbuff.append("\r\n\r\n");
		return strbuff.toString();
	}

}
