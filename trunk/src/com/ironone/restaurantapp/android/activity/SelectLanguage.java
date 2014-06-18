/*
 * Select Language.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.activity;

/**
 * This class gives the facility of selecting a language
 *
 * @Created on June 27, 2012 : 9:18:20 PM
 * @author hasinia
 */

import java.util.List;
import java.util.Locale;
import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.dbhandler.TableDataAccess;
import com.ironone.restaurantapp.android.log.AppLog;
import com.ironone.restaurantapp.android.log.ErrorCodes;
import com.ironone.restaurantapp.android.log.RestaurantAppGenericException;
import com.ironone.restaurantapp.android.logic.Image;
import com.ironone.restaurantapp.android.logic.Language;
import com.ironone.restaurantapp.android.logic.Menu;
import com.ironone.restaurantapp.android.logic.StaticData;
import com.ironone.restaurantapp.android.titlebar.TitleBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectLanguage extends TitleBar {
	/** Called when the activity is first created. */
	ImageButton img;
	TextView tv;
	String tag = "SelectLanguage.java";
	Bitmap bm_item;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.language);

		LinearLayout header = (LinearLayout) findViewById(R.id.layout_heading_language);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.titlebar, null);
		TableDataAccess tableData = new TableDataAccess(getApplicationContext());
		
		if(StaticData.tableNo == null || StaticData.tableNo.isEmpty()) {
			StaticData.tableNo = tableData.getTableNo().toString();
		}

		header.addView(vg);
		hideBackButton();
		hideHomeButton();
		setMyOrderItemCount();
		setSubmittedOrderItemCount();
		setTitlebarLanguage();

		ImageButton set = (ImageButton) vg.findViewById(R.id.SettingsButton);
		set.setId(R.layout.language);

		/* Show the table No. */
		
		TextView tv = (TextView) findViewById(R.id.table_no_language);
		tv.setText(getApplicationContext().getString(R.string.table_no) + " : "
				+ StaticData.tableNo);
		
		StaticData.tableNo = tableData.getTableNo().toString();

		/* for the first time navigation to language selection */
		if (StaticData.copyMenu == null) {
			hideMyOrdersButton();
			hideSubmittedOrdersButton();
		}

		LinearLayout items = (LinearLayout) findViewById(R.id.image);
		final LinearLayout names = (LinearLayout) findViewById(R.id.name);

		final List<Language> langList = StaticData.languages;

		for (int i = 0; i < langList.size(); i++) {
			bm_item = null;
			img = new ImageButton(this);
			img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100,
					75);
			lp.setMargins(25, 0, 25, 0);

			bm_item = Image.decodeSampledBitmapFromResource(langList.get(i)
					.getImage().getLocation(), 200, 200);

			if (bm_item != null) {
				img.setImageBitmap(bm_item);
			}

			img.setTag(langList.get(i).getLanID());
			img.setId(i);

			tv = new TextView(this);
			tv.setTextColor(Color.BLACK);
			tv.setTextSize(15);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setText(langList.get(i).getName());

			tv.setTextColor(Color.parseColor("#ffffff"));
			tv.setTypeface(null, Typeface.BOLD);

			img.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Animation anim = AnimationUtils.loadAnimation(
							SelectLanguage.this, R.anim.button_click);
					anim.setFillEnabled(true);
					anim.setFillAfter(true);
					v.startAnimation(anim);

					AppLog.info("SelectLanguage", "onClick", "select language",
							"selected one language", "language id = "
									+ v.getTag().toString());
					changeDeviceLanguage(v.getTag().toString());
					List<Menu> lastMenu = StaticData.fullmenu;
					StaticData.copyMenu = StaticData.fullmenu;

					if (StaticData.currentLanguageID != v.getTag().toString()
							&& lastMenu.size() > 0) {

						StaticData.currentLanguageID = v.getTag().toString();
					}

					try {
						Intent intent = new Intent(SelectLanguage.this,
								MainMenu.class);
						startActivity(intent);
						overridePendingTransition(R.anim.right_slide_in,
								R.anim.right_slide_out);
						finish();
						AppLog.info("SelectLanguage", "onClick",
								"start activity MainMenu",
								"started main menu activity", "language id = "
										+ v.getTag().toString());

					} catch (Exception e) {
						tag.concat("_onCreate");
						RestaurantAppGenericException ex = new RestaurantAppGenericException(
								ErrorCodes.ACTIVITY_NOT_FOUND_ERROR, e
										.getMessage());
						AppLog.error("SelectLanguage", "onClick",
								"start activity MainMenu", ex, "language id = "
										+ v.getTag().toString());
					}

				}
			});

			items.addView(img, lp);
			names.addView(tv, lp);

		}
	}

	// settings button functionality
	public void onSettingsClick(View v) {
		startActivity(new Intent(SelectLanguage.this,
				RestaurantAppActivity.class));
		finish();
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
	}

	/**
	 * this method is to change device language
	 * 
	 * @param country
	 *            code
	 **/
	public void changeDeviceLanguage(String lang) {
		Locale locale_en = new Locale(lang);
		Locale.setDefault(locale_en);
		Configuration config_en = new Configuration();
		config_en.locale = locale_en;
		getBaseContext().getResources().updateConfiguration(config_en,
				getBaseContext().getResources().getDisplayMetrics());
	}

	@Override
	public void onBackPressed() {

	}
}
