/*
 * MainMenu.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.activity;

import java.util.ArrayList;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.logic.Image;
import com.ironone.restaurantapp.android.logic.Menu;
import com.ironone.restaurantapp.android.logic.Session;
import com.ironone.restaurantapp.android.logic.StaticData;
import com.ironone.restaurantapp.android.titlebar.TitleBar;

/**
 * This class is to change the main menu and sub menu UIs dynamically
 * 
 * @Created on June 29, 2012 : 8:59:20 PM
 * @author harshanak
 */

public class MainMenu extends TitleBar {
	/** Called when the activity is first created. */
	FrameLayout frml;
	ImageView img; // image of the food item
	TextView tv;
	Button btn; // name of the food item
	public ArrayList<Menu> menuList;
	String tag = "MainMenu.java";
	String TAG = "com.ironone.restaurantapp.android.activity/MainMenu.java";
	Bitmap bm_item = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tag.concat("_onCreate");

		LinearLayout header = (LinearLayout) findViewById(R.id.layout_heading_main);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.titlebar, null);

		header.addView(vg);
		setHeaderTitle(getApplicationContext().getString(R.string.mainmenu));
		if (StaticData.copyMenu == StaticData.fullmenu) {
			hideBackButton();
			hideHomeButton();
		}
		setMyOrderItemCount();
		setSubmittedOrderItemCount();
		setTitlebarLanguage();

		/**
		 * set the id of the settings button
		 */
		ImageButton set = (ImageButton) vg.findViewById(R.id.SettingsButton);
		set.setId(R.layout.main);

		ArrayList<Menu> menuList = (ArrayList<Menu>) StaticData.copyMenu;

		final LinearLayout items = (LinearLayout) findViewById(R.id.linearLayoutItems);

		/**
		 * dynamically loading the menu items from menuList
		 * 
		 */
		for (int i = 0; i < menuList.size(); i++) {
			if (menuList.get(i).isEnable()) {

				frml = new FrameLayout(this);
				
				Resources res = getResources();
				int[] bits = res.getIntArray(R.array.imgMainMenuSize);

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						bits[0], bits[1]);
				lp.setMargins(0, 0, 10, 0);
				img = new ImageView(this);

				LinearLayout.LayoutParams lp_img = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
				int[] imgSizes = res.getIntArray(R.array.imgMainMenuImageSize);
				
				bm_item = Image.decodeSampledBitmapFromResource(menuList.get(i)
						.getImage().getLocation(), imgSizes[0], imgSizes[1]);

				if (bm_item != null) {
					Log.d(TAG, "Bitmap image is not null");
					img.setImageBitmap(bm_item);
				}
				

				img.setId(menuList.get(i).getId());
				img.setTag(i);
				img.setScaleType(ScaleType.FIT_XY);
				img.setAdjustViewBounds(true);

				tv = new TextView(this);
				tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.WRAP_CONTENT));

				tv.setGravity(Gravity.CENTER_HORIZONTAL);
				tv.setBackgroundColor(Color.parseColor("#88ffffff"));
				tv.setPadding(15, 15, 15, 15);				
				tv.setTextColor(Color.parseColor("#000000"));
				tv.setText(menuList.get(i).getName());
				tv.setTextSize(22);
				//tv.setTypeface(null, Typeface.BOLD);

				frml.addView(img, lp_img);
				frml.addView(tv);
				items.addView(frml, lp);

				img.setOnClickListener(new OnClickListener() {
					/**
					 * menu item click
					 */
					public void onClick(View v) {
						/**
						 * if there are sub-categories in the clicked category
						 */
						ImageView b = (ImageView) items.findViewWithTag(v.getTag());
						b.setColorFilter(Color.argb(150, 155, 155, 155));
						if (Menu.getChildMenu(StaticData.fullmenu,
								v.getId()) != null) {
							StaticData.lastMenu = StaticData.copyMenu;
							StaticData.copyMenu = Menu.getChildMenu(
									StaticData.fullmenu, v.getId());
							try {
								Intent i = new Intent(MainMenu.this,
										MainMenu.class);
								StaticData.clickedIndex = v.getId();
								startActivity(i);
								overridePendingTransition(
										R.anim.right_slide_in,
										R.anim.right_slide_out);
								finish();
							} catch (ActivityNotFoundException e) {
								Log.e(TAG + "/img.setOnClickListener()",
										e.getMessage());
							} catch (Exception e) {
								Log.e(TAG + "/img.setOnClickListener()",
										e.getMessage());
							}

						} else if (Menu.getChildItems(StaticData.fullmenu,
								v.getId()) != null) {
							StaticData.items = Menu.getChildItems(
									StaticData.fullmenu, v.getId());							
							try {
								Intent i = new Intent(MainMenu.this,
										RestaurantItem.class);
								i.putExtra("MenuPosition", (Integer) v.getTag());
								startActivity(i);
								overridePendingTransition(
										R.anim.right_slide_in,
										R.anim.right_slide_out);
								finish();
							} catch (ActivityNotFoundException e) {
								Log.e(TAG + "/img.setOnClickListener()",
										e.getMessage());
							} catch (Exception e) {
								Log.e(TAG + "/img.setOnClickListener()",
										e.getMessage());
							}

						}
					}
				});
				}
		}
		TextView txtTableValue = (TextView)findViewById(R.id.txtTableNoValue_Title);
		txtTableValue.setText(": " + Session.getTable());
	}

	/**
	 * 
	 */
	@Override
	public void onBackPressed() {
		if (StaticData.copyMenu != StaticData.fullmenu) {
			StaticData.copyMenu = StaticData.lastMenu;
			try {
				Intent i = new Intent(MainMenu.this, MainMenu.class);
				startActivity(i);
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();
			} catch (ActivityNotFoundException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}
}