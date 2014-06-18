/*
 * MiniOrder.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;

/**
 * Represent a order with more items only having same item type
 *
 * @Created on Jul 11, 2012 : 3:42:41 PM
 * @author malakag
 */

public class MiniOrder {
	
	private Item item = new Item();
	private Integer noOfItems = 0;
	private String specialInstruction = "";

	public MiniOrder(Item item, Integer noOfItems, String specialInstruction) {
		super();
		this.item = item;
		this.noOfItems = noOfItems;
		this.specialInstruction = specialInstruction;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Integer getNoOfItems() {
		return noOfItems;
	}
	public void setNoOfItems(Integer noOfItems) {
		this.noOfItems = noOfItems;
	}
	
	public String getSpecialInstruction() {
		return specialInstruction;
	}
	public void setSpecialInstruction(String specialInstruction) {
		this.specialInstruction = specialInstruction;
	}
	
	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	@Override
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "MiniOrder ( "
	        + super.toString() + TAB
	        + "item = " + this.item + TAB
	        + "noOfItems = " + this.noOfItems + TAB
	        + "Special Instruction = " + this.specialInstruction + TAB
	        + " )";
	
	    return retValue;
	}
	
	
	
}

