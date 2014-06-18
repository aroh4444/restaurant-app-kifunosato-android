/*
 * RestaurantAppActivity.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.activity;

/**
 * This class gives the waiter the ability of logging to the system if he is an authorised waiter
 *
 * @Created on June 27, 2012 : 10:14:20 PM
 * @author hasinia
 */

import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.logic.Session;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RestaurantAppActivity extends Activity {

	String TAG = "com.ironone.restaurantapp.android.activity/RestaurantAppActivity.java";

	ProgressBar bar = null;
	TextView loadingMessage = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		loadingMessage = (TextView) findViewById(R.id.connectingToServer);
		bar = (ProgressBar) findViewById(R.id.progressBarConnectingToServer);
		loadingMessage.setVisibility(View.GONE);
		bar.setVisibility(View.GONE);
	}

	public void onCancelClick(View v) {

		try {
			Bundle extras = getIntent().getExtras();
			int previous_view_id = extras.getInt("myView");

			switch (previous_view_id) {
			case R.layout.main:
				startActivity(new Intent(RestaurantAppActivity.this,
						MainMenu.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.itemsmenuview:
				startActivity(new Intent(RestaurantAppActivity.this,
						RestaurantItem.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.viewfoodcart:
				startActivity(new Intent(RestaurantAppActivity.this,
						MyOrder.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.description:
				startActivity(new Intent(RestaurantAppActivity.this,
						Description.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.sentorders:
				startActivity(new Intent(RestaurantAppActivity.this,
						SentOrders.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.suggestions:
				startActivity(new Intent(RestaurantAppActivity.this,
						AppSuggestions.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			default:
				startActivity(new Intent(RestaurantAppActivity.this,
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

	public void onSubmitClick(View v) {
		TextView error = (TextView) findViewById(R.id.error);
		EditText waiterId = (EditText) findViewById(R.id.txtwaiterId);
		EditText accessCode = (EditText) findViewById(R.id.textAccessCode);

		if (waiterId.getText().toString().equals("")) {
			error.setText(getApplicationContext().getString(
					R.string.error_waiterID));
			error.setVisibility(View.VISIBLE);
		}

		// If 'Access Code' field is not filled
		else if (accessCode.getText().toString().equals("")) {
			error.setText(getApplicationContext().getString(
					R.string.error_accessCode));
			error.setVisibility(View.VISIBLE);
		}

		// If all the fields are filled correctly
		else {
			Bundle extras = getIntent().getExtras();
			int previous_view_id = extras.getInt("myView");
//			RoleDataAccess roleData = new RoleDataAccess(
//					getApplicationContext());
//
//			if (roleData.getPassword(waiterId.getText().toString()).trim()
//					.equals(accessCode.getText().toString().trim())) {
				try {
					Session.setUserID(waiterId.getText().toString());
					Intent i = new Intent(RestaurantAppActivity.this,
							SettingsActivity.class);
					i.putExtra("myView", previous_view_id);
					startActivity(i);
					overridePendingTransition(R.anim.right_slide_in,
							R.anim.right_slide_out);
					finish();

				} catch (ActivityNotFoundException e) {
					Log.e(TAG + "/onSubmitClick()", e.getMessage());
				}
//			}
//			else{
//				error.setText(getApplicationContext().getString(
//						R.string.inncorrect_username_password));
//				error.setVisibility(View.VISIBLE);
//			}

		}
	}

	@Override
	public void onBackPressed() {

	}

}