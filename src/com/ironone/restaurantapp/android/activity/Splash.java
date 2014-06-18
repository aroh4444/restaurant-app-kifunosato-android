/*
 * Splash.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.activity;


import java.util.ArrayList;
import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.dbhandler.DBController;
import com.ironone.restaurantapp.android.dbhandler.TableCreator;
import com.ironone.restaurantapp.android.log.AppLog;
import com.ironone.restaurantapp.android.log.ErrorCodes;
import com.ironone.restaurantapp.android.log.RestaurantAppGenericException;
import com.ironone.restaurantapp.android.logic.Currency;
import com.ironone.restaurantapp.android.logic.Language;
import com.ironone.restaurantapp.android.logic.LanguageTranslator;
import com.ironone.restaurantapp.android.logic.Menu;
import com.ironone.restaurantapp.android.logic.StaticData;
import com.ironone.restaurantapp.android.logic.XMLHandler;
import com.ironone.restaurantapp.android.init.ResourceInitializer;
import com.ironone.restaurantapp.android.init.ResourceManger;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

/**
 * Splash Class
 * 
 * @Created on Jul 6, 2012 : 11:16:39 AM
 * @author malakag
 */

public class Splash extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		new TableCreator(getApplicationContext());
		DBController db = new DBController(getApplicationContext());
		db.getDBToRead();
		
		db.closeDB();

		WifiManager wimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if(wimanager.getConnectionInfo().getMacAddress()==null) {
			Toast.makeText(getApplicationContext(), getString(R.string.no_wifi_connection_error),
					Toast.LENGTH_SHORT).show();
			finish();
			
			return;
		}
		StaticData.deviceid = wimanager.getConnectionInfo().getMacAddress().trim();
		//StaticData.deviceid = "6C:83:36:BE:FD:92";
		

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				new ResourceInitializer().init(getApplicationContext());
				Menu menu = new Menu();
				XMLHandler xml = new XMLHandler(getApplicationContext());
				menu = xml.loadXML();

				AppLog.info("Splash", "doInBachground",
						"loading menu.xml to read",
						"menu.xml is loaded to read");
				try {
					StaticData.currency = menu
							.getCurrency("restaurant/currency");
					AppLog.info("Splash", "doInBachground", "getCurrency",
							"get the available currency list");
				} catch (Exception e) {
					RestaurantAppGenericException ex = new RestaurantAppGenericException(
							ErrorCodes.UNKNOWN_ERROR, e.getMessage());
					AppLog.error("Splash", "doInBachground", "getCurrency", ex);
					StaticData.currency = new Currency();
				}

				try {
					StaticData.languages = menu
							.getLanguages("restaurant/languages/language");
					AppLog.info("Splash", "doInBachground", "getLanguages",
							"get the available language list");
				} catch (Exception e) {
					RestaurantAppGenericException ex = new RestaurantAppGenericException(
							ErrorCodes.UNKNOWN_ERROR, e.getMessage());
					AppLog.error("Splash", "doInBachground", "getLanguages", ex);
					StaticData.languages = new ArrayList<Language>();
				}
				
				

				StaticData.context = getApplicationContext();
				LanguageTranslator translator = new LanguageTranslator(
						StaticData.context);
				
				try {
					StaticData.addOns = translator.getAddOnsList();
					AppLog.info("Splash", "doInBachground", "getAddOns",
							"get the main menu");
				} catch (Exception e) {
					RestaurantAppGenericException ex = new RestaurantAppGenericException(
							ErrorCodes.UNKNOWN_ERROR, e.getMessage());
					AppLog.error("Splash", "doInBachground", "getAddOns", ex);
					StaticData.addOns = new ArrayList<Menu>();
				}
				
				try {
					StaticData.fullmenu = translator.getMainMenu();
					AppLog.info("Splash", "doInBachground", "getMainMenu",
							"get the main menu");
				} catch (Exception e) {
					RestaurantAppGenericException ex = new RestaurantAppGenericException(
							ErrorCodes.UNKNOWN_ERROR, e.getMessage());
					AppLog.error("Splash", "doInBachground", "getMainMenu", ex);
					StaticData.fullmenu = new ArrayList<Menu>();
				}
				
				

				ResourceManger res = new ResourceManger();
				res.CreateResourceDirectoy();

				return null;
			}

			@Override
			public void onPostExecute(Void result) {
				new Handler().post(new Runnable() {
					public void run() {
						try {
							startActivity(new Intent(Splash.this, Login.class));
							finish();
							AppLog.info("Splash", "onPostExecute", "run",
									"Started Login Activity");
						} catch (ActivityNotFoundException e) {
							RestaurantAppGenericException ex = new RestaurantAppGenericException(
									ErrorCodes.ACTIVITY_NOT_FOUND_ERROR, e
											.getMessage());
							AppLog.error("Splash", "onPostExecute", "run", ex);
							finish();
						}
					}
				});
			}
		}.execute();
		
		
	}
	
		
	

}
