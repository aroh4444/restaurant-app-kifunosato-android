/*
 * SettingsActivity.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.activity;

/**
 * This enables the user to change device settings
 *
 * @Created on June 28, 2012 : 3:42:20 PM
 * @author hasinia
 */

import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.logic.Session;
import com.ironone.restaurantapp.android.logic.StaticData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * This class is for changing the settings and synchronizing
 * 
 * @Created on June 27, 2012 : 10:14:20 PM
 * @author hasinia
 */

public class SettingsActivity extends Activity {

	String TAG = "com.ironone.restaurantapp.android.activity/SettingsActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setttings);

	}

	// Action when Set Table button is clicked
	public void onTableNoClick(View v) {
		try {
			Bundle extras = getIntent().getExtras();
			int previous_view_id = extras.getInt("myView");
			Intent i = new Intent(SettingsActivity.this, SetTable.class);
			i.putExtra("myView", previous_view_id);
			startActivity(i);
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (Exception e) {
			Log.e(TAG + "/onTableNoClick()", e.getMessage());
		}
	}

	// Action when Set Device Name button is clicked
	public void onDeviceNameClick(View v) {
		try {
			Bundle extras = getIntent().getExtras();
			int previous_view_id = extras.getInt("myView");

			Intent i = new Intent(SettingsActivity.this, SetDeviceName.class);
			i.putExtra("myView", previous_view_id);
			startActivity(i);

			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (Exception e) {
			Log.e(TAG + "/onDeviceNameClick()", e.getMessage());
		}
	}

	public void onClearSentOrdersClick(View v) {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingsActivity.this);
		builder.setMessage(getApplicationContext().getString(
				R.string.confirm_clear_orders));
		builder.setCancelable(false);
		builder.setPositiveButton(
				getApplicationContext().getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						try {
							StaticData.currentOrder.getMiniOrder().clear();
							StaticData.submitedOrders.getOrder(Session.getTable()).getMiniOrder()
									.clear();
							StaticData.successOrders.getOrder(Session.getTable()).getMiniOrder().clear();
							StaticData.failOrders.getOrder(Session.getTable()).getMiniOrder().clear();							

							Toast.makeText(getApplicationContext(),
									R.string.clear_order_success,
									Toast.LENGTH_LONG).show();
						} catch (Exception e) {
							Log.d(TAG + "/onClearSentOrdersClick()",
									e.getMessage());
							Toast.makeText(getApplicationContext(),
									R.string.clear_order_success,
									Toast.LENGTH_LONG).show();
							dialog.cancel();
						}

					}
				});

		builder.setNegativeButton(getApplicationContext()
				.getString(R.string.no), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();

			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	// Action when Synchronize menu is clicked
	public void onSyncMenuClick(View v) {
		ProgressBar syncMenuSpinner = (ProgressBar) findViewById(R.id.syncMenuSpinner);
		syncMenuSpinner.setVisibility(View.VISIBLE);
	}

	// Action when Synchronize other data is clicked
	public void onSyncOtherClick(View v) {
		ProgressBar syncOtherSpinner = (ProgressBar) findViewById(R.id.syncOtherSpinner);
		syncOtherSpinner.setVisibility(View.VISIBLE);
	}

	// Action when Close is clicked
	public void onCloseClick(View view) {

		try {
			Bundle extras = getIntent().getExtras();
			int previous_view_id = extras.getInt("myView");

			switch (previous_view_id) {
			case R.layout.main:
				startActivity(new Intent(SettingsActivity.this, MainMenu.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.itemsmenuview:
				startActivity(new Intent(SettingsActivity.this,
						RestaurantItem.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.viewfoodcart:
				startActivity(new Intent(SettingsActivity.this, MyOrder.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.description:
				startActivity(new Intent(SettingsActivity.this,
						Description.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.sentorders:
				startActivity(new Intent(SettingsActivity.this,
						SentOrders.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.suggestions:
				startActivity(new Intent(SettingsActivity.this,
						AppSuggestions.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			default:
				startActivity(new Intent(SettingsActivity.this,
						SelectLanguage.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;
			}
		} catch (Exception e) {
			Log.e(TAG + "/onCloseClick()", e.getMessage());
		}

	}

	public void onExitClick(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingsActivity.this);
		builder.setMessage(getApplicationContext().getString(
				R.string.exit_confirmation));
		builder.setCancelable(false);
		builder.setPositiveButton(
				getApplicationContext().getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						finish();

					}
				});

		builder.setNegativeButton(getApplicationContext()
				.getString(R.string.no), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();

			}
		});

		AlertDialog alert = builder.create();
		alert.show();

	}

	@Override
	public void onBackPressed() {

	}
}
