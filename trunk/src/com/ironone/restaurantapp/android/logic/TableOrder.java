/*
 * TableOrder.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;

import java.util.HashMap;

/**
 * Table specific Order
 *
 * @Created on Aug 22, 2013 : 2:50:19 PM
 * @author Gayanp
 */

public class TableOrder {

	public HashMap<String, Order>  addedOrders = new HashMap<String, Order>();
	
	public Order getOrder(String tableNo) {
		Order o = new Order();
		if(addedOrders.containsKey(tableNo)) {
			o=addedOrders.get(tableNo);
		}
		else {
			addedOrders.put(tableNo, o);
		}
		return o;
	}
	
	
	public void setOrder(Order newOrder, String tableNo) {
		if(!addedOrders.containsKey(tableNo)) {
			addedOrders.remove(tableNo);
		}
		addedOrders.put(tableNo, newOrder);
	}
	
}
