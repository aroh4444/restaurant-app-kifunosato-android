/*
 * Order.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent an restaurant order in class level
 *
 * @Created on Jul 11, 2012 : 3:31:56 PM
 * @author malakag
 */

public class Order {

	private List<MiniOrder> miniOrder = new ArrayList<MiniOrder>();

	public List<MiniOrder> getMiniOrder() {
		return miniOrder;
	}
	public void setMiniOrder(List<MiniOrder> miniOrder) {
		this.miniOrder = miniOrder;
	}


	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "Order ( "
	        + super.toString() + TAB
	        + "miniOrder = " + this.miniOrder + TAB
	        + " )";
	
	    return retValue;
	}
	
	
}

