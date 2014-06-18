/*
 * Size.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;


/**
 * {Insert class description here}
 *
 * @Created on Jun 27, 2012 : 10:14:23 AM
 * @author malakag
 */

public class Size {

	private String type = "";
	private String name = "";
	private Double price = 0.0;
	private Integer noOfPersons = 0;
	private int sizeNo = -1;
	private int itemID = 0;
	
	public int getSizeNo() {
		return sizeNo;
	}

	public void setSizeNo(int sizeNo) {
		this.sizeNo = sizeNo;
	}

	public String getType() {
		LanguageTranslator translator = new LanguageTranslator(StaticData.context);
		return translator.getPreparedString("id_"+this.itemID+"_options_name");
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		LanguageTranslator translator = new LanguageTranslator(StaticData.context);
		return translator.getPreparedString("id_"+this.itemID+"_option_"+this.sizeNo+"_name");
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getNoOfPersons() {
		return noOfPersons;
	}

	public void setNoOfPersons(Integer noOfPersons) {
		this.noOfPersons = noOfPersons;
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
	    
	    retValue = "Size ( "
	        + super.toString() + TAB
	        + "type = " + this.type + TAB
	        + "name = " + this.name + TAB
	        + "price = " + this.price + TAB
	        + "noOfPersons = " + this.noOfPersons + TAB
	        + " )";
	
	    return retValue;
	}
		
}
