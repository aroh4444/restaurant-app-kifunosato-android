package com.ironone.restaurantapp.android.init;

import java.io.File;

public class Resources {
	private static final String APP_DIR_NAME = "RestaurantApp";
	private static final String IMG_DIR_NAME = "Images";
	protected static File APP_DIR_FILE = null;
	protected static File IMG_DIR_FILE = null;
	
	public static File getResourceDirectory(){
		return APP_DIR_FILE;
	}
	
	
	public static File getImageDirectory(){
		return IMG_DIR_FILE;
	}


	public static String getAppDirName() {
		return APP_DIR_NAME;
	}


	public static String getImgDirName() {
		return IMG_DIR_NAME;
	}
	
}
