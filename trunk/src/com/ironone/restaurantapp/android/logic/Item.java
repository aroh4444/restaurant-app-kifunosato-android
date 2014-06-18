/*
 * Item.java
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
 * Food item is represented my Item object
 * 
 * @Created on Jun 27, 2012 : 10:14:23 AM
 * @author malakag
 */

public class Item extends RestaurantMenu implements Cloneable {

	private String ingredients = "";
	private String description = "";
	private Integer calories = 0;
	private List<Size> sizes = new ArrayList<Size>();
	private Size selectedSize = new Size();
	private String cookingInst = "";
	private Double price = 0.0;
	private Context context;
	private String itemId = "";
	private String itemStatus = "Pending";

	private List<AddOn> addOnes = new ArrayList<AddOn>();
	private List<AddOn> selectedAddOnes = new ArrayList<AddOn>();

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public Item(Context context) {
		this.context = context;
	}

	public Item(Document doc) {
		xmlDocument = doc;
		xPath = XPathFactory.newInstance().newXPath();
	}

	public Item() {
		xPath = XPathFactory.newInstance().newXPath();
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public static String TAG = "com.ironone.restaurantapp.android.logic/Item.java";

	public String getCookingInst() {
		LanguageTranslator translator = new LanguageTranslator(this.context);
		return translator.getPreparedString("id_" + this.getId()
				+ "_instruction");
	}

	public void setCookingInst(String cookingInst) {
		this.cookingInst = cookingInst.replace("\n", "").replace("\t", "");
	}

	public Size getSelectedSize() {
		return selectedSize;
	}

	public void setSelectedSize(Size selectedSize) {
		this.selectedSize = selectedSize;
	}

	public String getIngredients() {
		LanguageTranslator translator = new LanguageTranslator(this.context);
		return translator.getPreparedString("id_" + this.getId()
				+ "_ingredients");
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients.replace("\n", "").replace("\t", "");
	}

	public String getDescription() {
		LanguageTranslator translator = new LanguageTranslator(this.context);
		return translator.getPreparedString("id_" + this.getId()
				+ "_description");
	}

	public void setDescription(String description) {
		this.description = description.replace("\n", "").replace("\t", "");
	}

	public Integer getCalories() {
		return calories;
	}

	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	public List<Size> getSizes() {
		return sizes;
	}

	public void setSizes(List<Size> sizes) {
		this.sizes = sizes;
	}

	public List<AddOn> getAddOnes() {
		return addOnes;
	}

	public void setAddOnes(List<AddOn> addOnes) {
		this.addOnes = addOnes;
	}

	public List<AddOn> getSelectedAddOnes() {
		return selectedAddOnes;
	}

	public void setSelectedAddOnes(List<AddOn> selectedAddOnes) {
		this.selectedAddOnes = selectedAddOnes;
	}

	public void addSelectedAddOnes(AddOn selectedAddOne) {
		this.selectedAddOnes.add(selectedAddOne);
	}

	public void removeSelectedAddOnes(AddOn selectedAddOne) {
		this.selectedAddOnes.remove(selectedAddOne);
	}

	/**
	 * Check add On is selected using id
	 * 
	 * @param id
	 * @return
	 */
	public boolean isSelectedAddOn(int id) {
		for (AddOn addOn : this.selectedAddOnes) {
			if (addOn.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public Item clone() {
		try {
			Item cloned = (Item) super.clone();
			cloned.selectedAddOnes = cloned.selectedAddOnes;
			return cloned;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * get total cost from add ons
	 * 
	 * @return
	 */
	public Double getSelectedAddOnsCost() {
		Double d = Double.valueOf(0);
		for (AddOn addOn : this.getSelectedAddOnes()) {
			d += addOn.getPrice();
		}
		return d;
	}

	/**
	 * Get Add on from Id
	 * 
	 * @param id
	 * @return
	 */
	public AddOn getAddOnFromId(int id) {
		for (AddOn addOn : this.getAddOnes()) {
			if (addOn.getId() == id) {
				return addOn;
			}
		}
		return null;
	}

	public List<Item> prepareItemObjects(List<Item> items) {

		List<Item> result = new ArrayList<Item>();
		for (int i = 0; i < items.size(); i++) {
			Item item = new Item();
			int id = 0;
			List<Menu> itemDetails = new ArrayList<Menu>();
			try {
				id = items.get(i).getInt("item/id");
				item.setId(id);
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			try {
				item.setName("");
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			try {
				item.setEnable(items.get(i).getBoolean("item/isEnable"));
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			try {
				item.setPrice(items.get(i).getDouble("item/price"));
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			try {
				itemDetails = items.get(i).getAll("item/item_details");
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			try {
				item.setIngredients("");
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			try {
				item.setDescription("");
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			try {
				item.setCookingInst("");
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			try {
				item.setCalories(itemDetails.get(0).getInt(
						"item_details/calories"));
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			Image img = new Image();
			List<Menu> image = new ArrayList<Menu>();
			try {
				image = items.get(i).getAll("item/image");
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			try {
				img.setLocation(image.get(0).getString("image/location"));
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			try {
				img.setType(image.get(0).getString("image/type"));
				item.setImage(img);
			} catch (ArrayIndexOutOfBoundsException e) {
				Log.e(TAG, e.toString());
			}

			List<Menu> sizes = new ArrayList<Menu>();

			try {
				sizes = items.get(i).getAll("item/options");
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			String type = "";

			try {
				type = "";
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			List<Menu> sizeClasses = new ArrayList<Menu>();

			try {
				sizeClasses = sizes.get(0).getAll("options/option");
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			List<Size> sizeList = new ArrayList<Size>();
			for (int j = 0; j < sizeClasses.size(); j++) {
				String name = "";
				try {
					name = "";
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}

				double price = 0;

				try {
					price = sizeClasses.get(j).getDouble("option/price");
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}

				int noOfPersons = 0;

				try {
					noOfPersons = sizeClasses.get(j).getInt(
							"option/no_of_person");
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}

				Size tempSize = new Size();
				tempSize.setType(type);
				tempSize.setName(name);
				tempSize.setPrice(price);
				tempSize.setNoOfPersons(noOfPersons);
				tempSize.setSizeNo(j);
				tempSize.setItemID(id);
				sizeList.add(tempSize);
			}
			item.setSizes(sizeList);

			List<Menu> addOnesMenu = new ArrayList<Menu>();

			try {
				String addOnName = items.get(i).getString("item/add_on_name");

				if (addOnName != null && !addOnName.isEmpty()) {
					for (Menu menu : StaticData.addOns) {

						if (menu.getString("add_ons/name").equals(addOnName)) {
							addOnesMenu = menu.getAll("add_ons/add_on");
						}

					}
				}

			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}

			for (Menu menu : addOnesMenu) {
				item.addOnes.add(new AddOn(menu));
			}

			if (item.isEnable()) {
				result.add(item);
			}
		}
		return result;
	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value
	 * format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	public String toString() {
		final String TAB = "    ";

		String retValue = "";

		retValue = "Item ( " + super.toString() + TAB + "ingredients = "
				+ this.ingredients + TAB + "description = " + this.description
				+ TAB + "calories = " + this.calories + TAB + "sizes = "
				+ this.sizes + TAB + "selectedSize = " + this.selectedSize
				+ TAB + "cookingInst = " + this.cookingInst + TAB + "price = "
				+ this.price + TAB + " )";

		return retValue;
	}

}