/*
 * AddOn.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;

/**
 * Value Class to Hold Add Ons data
 *
 * @Created on Apr 25, 2014 : 12:17:47 PM
 * @author Gayanp
 */

public class AddOn {
	
	private String name = "";
	private Double price = 0.0;
	private int id=0;
	
	public AddOn(Menu menu) {
		this.setName(menu.getString("add_on/name"));
		this.setPrice(menu.getDouble("add_on/price"));
		this.setId(menu.getInt("add_on/id"));
	}
	
	public String getName() {
		return name;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	

}
