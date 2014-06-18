package com.ironone.restaurantapp.android.filereader;

/*
 * FileReader.java
 *
 * Copyright(c) IronOne Technologies Pvt Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies Pvt Ltd.
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.ironone.restaurantapp.android.log.AppLog;
import com.ironone.restaurantapp.android.log.ErrorCodes;
import com.ironone.restaurantapp.android.log.RestaurantAppGenericException;

import android.content.Context;
import android.content.res.Resources;


/**
 * This class is used to read the 'table creation queries' text file.
 * 
 * @Created on June 27, 2012 : 12:13:00 PM
 * @author sampathb.
 */
public class FileReader {

	public String output = "";
	Context context;

	public FileReader(Context context) {
		super();
		this.context = context;
	}

	public String readTextFile() {
		try {

			Resources r = context.getResources();
			InputStream in = r.getAssets().open("create_table_queries.txt");

			AppLog.info("FileReader", "readTextFile", "opening file to read",
					"File opened successfuly to read");

			if (in != null) {

				InputStreamReader input = new InputStreamReader(in);
				BufferedReader buffreader = new BufferedReader(input);

				String line;
				while ((line = buffreader.readLine()) != null) {
					output += line;
				}
				try {
					in.close();
				} catch (Exception e) {
					RestaurantAppGenericException ex = new RestaurantAppGenericException(
							ErrorCodes.IO_ERROR, e.getMessage());
					AppLog.error("FileReader", "readTextFile",
							"Closing input stream", ex);
				}

			}

		} catch (IOException e) {
			RestaurantAppGenericException ex = new RestaurantAppGenericException(
					ErrorCodes.IO_ERROR, e.getMessage());
			AppLog.error("FileReader", "readTextFile",
					"Opening specified file", ex);
		}
		return output;
	}

}
