/*
 * SetDeviceName.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.activity;

import com.ironone.restaurantapp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class is to change the device name
 * 
 * @Created on June 29, 2012 : 8:59:20 PM
 * @author hasinia
 */

public class SetDeviceName extends Activity {

	protected static final int REQUEST_ENABLE_BT = 0;
	String TAG = "com.ironone.restaurantapp.android.activity/SetDeviceName.java";
	final BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devicename);
	}

	// Action when Cancel button is clicked.
	public void onCancelClick(View v) {
		
		try {
			Bundle extras = getIntent().getExtras();		
			int previous_view_id = extras.getInt("myView");
			
			Intent i = new Intent(SetDeviceName.this, SettingsActivity.class);
			i.putExtra("myView", previous_view_id);
			startActivity(i);
			overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
			finish();

		} catch (Exception e) {
			Log.e(TAG+"/onCancelClick()", e.getMessage());
		}
	}

	// Action when 'Submit' button is filled
	public void onSubmitClick(View v) {
		
		try {
			Bundle extras = getIntent().getExtras();		
			int previous_view_id = extras.getInt("myView");
			
			final EditText deviceName = (EditText) findViewById(R.id.txtdeviceName);
			final TextView error = (TextView) findViewById(R.id.errorDevice);

			// If 'Device Name' field is not filled
			if (deviceName.getText().toString().equals("")) {
				error.setText(getApplicationContext().getString(
						R.string.error_deviceName));
				error.setVisibility(View.VISIBLE);
			}

			// If 'Device Name' field is filled
			else {
				String devName;

				// Get the text entered in the 'Device Name' text field
				devName = deviceName.getText().toString();

				// If the device is not bluetooth compatible
				if (bluetooth == null) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							SetDeviceName.this);
					builder.setMessage(getApplicationContext().getString(
							R.string.error_bluetooth));
					builder.setCancelable(false);
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									
									Bundle extras = getIntent().getExtras();		
									int previous_view_id = extras.getInt("myView");
									Intent i = new Intent(SetDeviceName.this,
											SettingsActivity.class);
									i.putExtra("myView", previous_view_id);
									startActivity(i);
									overridePendingTransition(
											R.anim.right_slide_in,
											R.anim.right_slide_out);
									finish();


								}
							});

					builder.setNegativeButton(
							getApplicationContext().getString(R.string.cancel),
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
									deviceName.setText("");

								}
							});

					AlertDialog alert = builder.create();
					alert.show();

				}

				// If the device is bluetooth compatible
				else {

					// If bluetooth is not enabled in the device
					if (!bluetooth.isEnabled()) {

						// Notify bluetooth is not enabled
						Toast.makeText(getApplicationContext(),
								"Bluetooth is not enabled!", Toast.LENGTH_LONG)
								.show();

						// Enable bluetooth
						Intent enableBtIntent = new Intent(
								BluetoothAdapter.ACTION_REQUEST_ENABLE);
						enableBtIntent.putExtra("myView", previous_view_id);
						startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
						overridePendingTransition(R.anim.right_slide_in,
								R.anim.right_slide_out);
						finish();

						// Notify bluetooth is enabled
						Toast.makeText(getApplicationContext(),
								"Bluetooth enabled!", Toast.LENGTH_LONG).show();

					}

					// If bluetooth is enabled ion the devices

					// set the device name to the entered name
					bluetooth.setName("" + devName + "");

					// Show message with the set name

					Toast.makeText(getApplicationContext(),
							R.string.set_device_name_succesfully , Toast.LENGTH_LONG)
							.show();

					Intent i = new Intent(SetDeviceName.this, SettingsActivity.class);
					i.putExtra("myView", previous_view_id);
					startActivity(i);
					overridePendingTransition(R.anim.right_slide_in,
							R.anim.right_slide_out);
					finish();

				}

			}
		} catch (Exception e) {
			Log.e(TAG+"/onSubmitClick()", e.getMessage());
		}
	}
}
