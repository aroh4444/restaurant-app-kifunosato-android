/*
 * Image.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;

import java.io.File;
import com.ironone.restaurantapp.android.init.ResourceManger;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Represent an image
 * 
 * @Created on Jun 27, 2012 : 10:14:23 AM
 * @author malakag
 */

public class Image {
	private String type = "png";
	private String location = "";
	public Bitmap finalBitmap = null;


	public String getLocation() {
		return location + "." + getType();
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}
	
	
	public static Bitmap decodeSampledBitmapFromResource(String imageName,
	        int reqWidth, int reqHeight) {

		File imgDir = ResourceManger.getImageDirectory();

		//System.out.println(imgDir.getAbsolutePath() + " " + imageName);
		String path = imgDir.getAbsolutePath() + "/" + imageName;
		
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(path, options);
	    
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(path, options);
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

		retValue = "Image ( " + super.toString() + TAB + "type = " + this.type
				+ TAB + "location = " + this.location + TAB + " )";

		return retValue;
	}

}
