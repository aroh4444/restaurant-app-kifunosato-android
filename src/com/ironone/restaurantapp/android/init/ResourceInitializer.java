/*
 * ResourceInitializer.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.init;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ironone.restaurantapp.android.log.AppLog;
import com.ironone.restaurantapp.android.log.ErrorCodes;
import com.ironone.restaurantapp.android.log.RestaurantAppGenericException;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

/**
 * Check and copy init resources
 * 
 * @Created on Jun 19, 2013 : 12:19:17 PM
 * @author Gayanp
 */

public class ResourceInitializer extends Resources {
	String TAG = "com.ironone.restaurantapp.android.init/ResourceManger";

	public void init(Context c) {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			Log.d(TAG + "ER101", "SD card is not writable");
			return;
		}
		APP_DIR_FILE = new File(Environment.getExternalStorageDirectory() + "/"
				+ getAppDirName());
		IMG_DIR_FILE = new File(Environment.getExternalStorageDirectory() + "/"
				+ getAppDirName() + "/" + getImgDirName());
		createExternalDir(APP_DIR_FILE);
		createExternalDirAndCopy(IMG_DIR_FILE, c);

	}

	private void createExternalDir(File file) {
		boolean hasDir = false;
		if (!file.exists()) {
			hasDir = file.mkdir();
		}
		if (!hasDir) {
			Log.d(TAG, "Directory already exists");
		} else {
			Log.d(TAG, "Directory created at path " + file.getPath());
		}
	}

	private void createExternalDirAndCopy(File file, Context c) {
		boolean hasDir = false;
		if (!file.exists()) {
			Log.i(TAG, "Directory created at path " + file.getPath());
			hasDir = file.mkdir();
		}
		if (!hasDir) {
			Log.i(TAG, "Directory already exists");
		} else {
			Log.i(TAG, "Copy Assets exists");
			copyAssets(c);

		}
	}

	private void copyAssets(Context c) {
		AssetManager assetManager = c.getAssets();
		String[] files = null;
		try {
			files = assetManager.list("Images");
		} catch (IOException e) {
			RestaurantAppGenericException ex = new RestaurantAppGenericException(
					ErrorCodes.IO_ERROR, e.getMessage());
			AppLog.error("ResourceInitializer", "CopyAssets", "read assests",
					ex);
		}

		for (String filename : files) {
			AppLog.info("ResourceInitializer", "CopyAssets", "write file",
					"File name => " + filename);
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open("Images/" + filename); // if files
																// resides
																// inside the
																// "Files"
																// directory
																// itself
				out = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/"
						+ Resources.getAppDirName()
						+ "/"
						+ Resources.getImgDirName() + "/" + filename);
				copyFile(in, out);
				in.close();
				in = null;
				out.flush();
				out.close();
				out = null;
			} catch (Exception e) {
				RestaurantAppGenericException ex = new RestaurantAppGenericException(
						ErrorCodes.UNKNOWN_ERROR, e.getMessage());
				AppLog.error("ResourceInitializer", "CopyAssets", "write file",
						ex);
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
}
