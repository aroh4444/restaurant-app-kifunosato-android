/*
 * SetTable.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.activity;

import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.dbhandler.TableDataAccess;
import com.ironone.restaurantapp.android.logic.Session;
import com.ironone.restaurantapp.android.logic.StaticData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class gives the waiter the ability of setting the table number
 * 
 * @Created on June 27, 2012 : 10:14:20 PM
 * @author hasinia
 */

public class SetTable extends Activity {
	
	String TAG = "com.ironone.restaurantapp.android.activity/SetTable.java";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settable);
		EditText tableNo = (EditText) findViewById(R.id.txttableNo);
		tableNo.setText( Session.getTable());
	}

	public void onSubmitClick(View v) {
		
		Bundle extras = getIntent().getExtras();		
		
		
		EditText tableNo = (EditText) findViewById(R.id.txttableNo);
		TextView errortable = (TextView) findViewById(R.id.errorTable);
		TableDataAccess tableData = new TableDataAccess(getApplicationContext());
		if (tableNo.getText().toString().equals("")) {
			errortable.setText(getApplicationContext().getString(
					R.string.error_tableNo));
			errortable.setVisibility(View.VISIBLE);
		} else{
			try {
				StaticData.tableNo = tableNo.getText().toString();
				tableData.insert(tableNo.getText().toString());
				Session.setTable(tableNo.getText().toString());
				if(extras == null) {
					Intent i = new Intent(SetTable.this, SelectLanguage.class);
					startActivity(i);
					overridePendingTransition(R.anim.right_slide_in,
							R.anim.right_slide_out);
					finish();
					return;
				}
				
				int previous_view_id = extras.getInt("myView");
				Intent i = new Intent(SetTable.this, MainMenu.class);
				i.putExtra("myView", previous_view_id);
				startActivity(i);
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

			} catch (Exception e) {
				Log.e(TAG+"/onSubmitClick()", e.getMessage());
			}
		}
	}

	public void onClearClick(View v) {
		
		try {
			Bundle extras = getIntent().getExtras();		
			int previous_view_id = extras.getInt("myView");
			
			Intent i = new Intent(SetTable.this, SettingsActivity.class);
			i.putExtra("myView", previous_view_id);
			startActivity(i);
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (Exception e) {
			Log.e(TAG+"/onCancelClick()", e.getMessage());
		}
	}
	
	public void onClearSentOrdersClick(View v) {
		
		EditText tableNo = (EditText) findViewById(R.id.txttableNo);
		TextView errortable = (TextView) findViewById(R.id.errorTable);
		TableDataAccess tableData = new TableDataAccess(getApplicationContext());
		if (tableNo.getText().toString().equals("")) {
			errortable.setText(getApplicationContext().getString(
					R.string.error_tableNo));
			errortable.setVisibility(View.VISIBLE);
		} else{
			try {
				tableData.insert(tableNo.getText().toString());
				Session.setTable(tableNo.getText().toString());
			} catch (Exception e) {
				Log.e(TAG+"/onSubmitClick()", e.getMessage());
			}
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SetTable.this);
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
	
	public void onExitClick(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SetTable.this);
		builder.setMessage(getApplicationContext().getString(
				R.string.exit_confirmation));
		builder.setCancelable(false);
		builder.setPositiveButton(
				getApplicationContext().getString(R.string.yes),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						finish();
						android.os.Process.killProcess(android.os.Process.myPid());						
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
	
	public void onLogoutClick(View v) {
		
		try {
			//Bundle extras = getIntent().getExtras();		
			//int previous_view_id = extras.getInt("myView");
			
			Intent i = new Intent(SetTable.this, Login.class);
			//i.putExtra("myView", previous_view_id);
			startActivity(i);
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (Exception e) {
			Log.e(TAG+"/onCancelClick()", e.getMessage());
		}
	}
}
