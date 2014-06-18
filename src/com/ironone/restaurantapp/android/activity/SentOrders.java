/*
 * SentOrders.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.logic.Image;
import com.ironone.restaurantapp.android.logic.MiniOrder;
import com.ironone.restaurantapp.android.logic.Session;
import com.ironone.restaurantapp.android.logic.StaticData;
import com.ironone.restaurantapp.android.synchronization.AutomaticLogin;
import com.ironone.restaurantapp.android.synchronization.ConfReader;
import com.ironone.restaurantapp.android.synchronization.ConnectionManager;
import com.ironone.restaurantapp.android.titlebar.TitleBar;
import com.ironone.restaurantapp.android.util.FormatUtil;

/**
 * {Insert class description here}
 *
 * @Created on Jul 20, 2012 : 10:28:02 AM
 * @author sampathb
 */

public class SentOrders extends TitleBar{

	List<MiniOrder> miniOrderList;
	View layout;
	TableLayout table;
	String TAG = "com.ironone.restaurantapp.android.activity/AppSuggestions.java";
	Button refreshBtn;

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sentorders);

		onCreateActivity();
		
		TextView txtTableValue = (TextView)findViewById(R.id.txtTableNoValue_Title);
		txtTableValue.setText(": " + Session.getTable());
	}

	private void onCreateActivity() {
		LinearLayout header = (LinearLayout) findViewById(R.id.layout_heading_sent_orders);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.titlebar, null);
		header.addView(vg);
			
		setHeaderTitle(getApplicationContext().getString(R.string.my_sent_order));
		setTitlebarLanguage();
		setMyOrderItemCount();
		setSubmittedOrderItemCount();
		miniOrderList = StaticData.submitedOrders.getOrder(Session.getTable()).getMiniOrder();

		ImageButton set = (ImageButton) vg.findViewById(R.id.SettingsButton);
		set.setId(R.layout.sentorders);
		
		for (int i = 0; i < miniOrderList.size(); i++) {
			
			LayoutInflater layoutInflater = LayoutInflater
					.from(getBaseContext());
			layout = new View(this);
			layout = layoutInflater.inflate(R.layout.dynamicsentdorder, null);
			
			TextView textViewFoodItemName = (TextView) layout
					.findViewById(R.id.txt_sent_food_name);
			TextView textViewFoodItemSize = (TextView) layout
					.findViewById(R.id.txt_sent_Portion);
			TextView textViewFoodItemPrice = (TextView) layout
					.findViewById(R.id.txt_sent_food_price);
			TextView textViewFoodItemQuantity = (TextView) layout
					.findViewById(R.id.txt_sent_food_quantity);
			TextView textViewFoodItemStatus = (TextView) layout
					.findViewById(R.id.txt_sent_status);
			
			TextView textViewPendingPrice = (TextView) findViewById(R.id.textViewPendingOrderPrice);
			TextView textViewSentPrice = (TextView) findViewById(R.id.textViewSubmitOrderPrice);
			TextView textViewTotalPrice = (TextView) findViewById(R.id.textViewTotalPrice);
			
			AppSuggestions app = new AppSuggestions();
						
			textViewPendingPrice.setText(app.getTotalPrice());
			textViewSentPrice.setText(app.getTotalSentPrice());
			textViewTotalPrice.setText(app.getFullPrice());		
			textViewFoodItemStatus.setText(miniOrderList.get(i).getItem().getItemStatus());
			
			textViewFoodItemName.setText(miniOrderList.get(i).getItem().getName());
			if(miniOrderList.get(i).getItem().getSizes().size()>0){
				textViewFoodItemSize.setText(miniOrderList.get(i).getItem().getSelectedSize().getType() + " : " + miniOrderList.get(i).getItem().getSelectedSize().getName());
				textViewFoodItemPrice.setText(StaticData.currency.getSymbol()+" "+FormatUtil.getNumberFormatted(miniOrderList.get(i).getItem().getSelectedSize().getPrice()*miniOrderList.get(i).getNoOfItems()));
			}
			else{
				textViewFoodItemSize.setVisibility(View.GONE);
				textViewFoodItemPrice.setText(StaticData.currency.getSymbol()+" "+FormatUtil.getNumberFormatted(miniOrderList.get(i).getItem().getPrice()*miniOrderList.get(i).getNoOfItems()));
			}
			textViewFoodItemQuantity.setText(miniOrderList.get(i).getNoOfItems().toString());
			//textViewFoodItemStatus.setText();  //to be set after sync

			
			ImageView foodImg = (ImageView) layout
					.findViewById(R.id.image_sent_food);			
			Bitmap bm_item = null;
			
			bm_item = Image.decodeSampledBitmapFromResource(miniOrderList.get(i).getItem().getImage().getLocation(), 200, 200);
			
			Bitmap finalBitmap = null;

			try {
				finalBitmap = Bitmap.createBitmap(bm_item, 24, 44, 120, 72);
				foodImg.setImageBitmap(finalBitmap);
			} catch (Exception e) {
				Log.d(TAG," image not found");
			}		
			
			table = (TableLayout) findViewById(R.id.tableLayoutSentFoodItems);

			TableRow tr = new TableRow(this);
			

			TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 0, 2);
			tr.addView(layout);
			table.addView(tr, lp);
		}
	}

	public void onRefreshOrderClick(View v) {
		
		refreshBtn = (Button) findViewById(R.id.sentorders_refresh);
		refreshBtn.setEnabled(false);
		RefreshOrder refreshOrder = new RefreshOrder();
		refreshOrder.execute();
				
	}
	
	@Override
	public void onBackPressed() {
		try {
			StaticData.copyMenu = StaticData.fullmenu;
			Intent i = new Intent(SentOrders.this, MainMenu.class);
			startActivity(i);
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (ActivityNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
	}
	
	public class RefreshOrder extends AsyncTask<Void, Void, Void>{
		
		DefaultHttpClient httpclient;
		
		@Override
		protected Void doInBackground(Void... params) {

			ConnectionManager manager = new ConnectionManager();
			try {
				httpclient = manager.getClient();
			} catch (ClientProtocolException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			try {
				URL statusmURL = new URL(
						new ConfReader(getApplicationContext()).getServerUrl() + "orders?action=getItemStatus&format=json");
				int successCount=0;
				
				for(MiniOrder submited : StaticData.submitedOrders.getOrder(Session.getTable()).getMiniOrder()){
					
					HttpPost statusHttppost = new HttpPost(statusmURL.toString());
					
					StringBuffer statusResponseString = new StringBuffer();

					List<NameValuePair> statusNameValuePairs = new ArrayList<NameValuePair>(
							2);
					statusNameValuePairs.add(new BasicNameValuePair(
							"itemid", submited.getItem().getItemId()));
					
					statusHttppost.setEntity(new UrlEncodedFormEntity(statusNameValuePairs));

					/** Execute HTTP Post Request **/
					HttpResponse statusResponse = httpclient.execute(statusHttppost);
					

					HttpEntity statusHttpEntity = statusResponse.getEntity();
					InputStream statusIs = statusHttpEntity.getContent();
					BufferedReader statusReader = new BufferedReader(
							new InputStreamReader(statusIs));

					String lineStatus;
					while ((lineStatus = statusReader.readLine()) != null) {
						statusResponseString.append(lineStatus);
					}
					
					System.out.println("Status response for"+submited.getItem().getName()+" :"+statusResponseString.toString());
					
					JSONObject statusJsonObject = new JSONObject();
					try {
						statusJsonObject = new JSONObject(statusResponseString.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String responseType = "";
					try {
						responseType = statusJsonObject.get("type").toString().trim();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String itemStatus = "";
					try {
						itemStatus = statusJsonObject.get("itemStatus").toString().trim();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					submited.getItem().setItemStatus(itemStatus!=null?itemStatus:"");
					
					if(responseType.equalsIgnoreCase("data")){
						successCount ++;
					}
					System.out.println("Status of the "+submited.getItem().getName() +" "+itemStatus);
					
					if(responseType.equalsIgnoreCase("error")){
						VerifyLogin login = new VerifyLogin();
						login.execute();
					}
					
				}
				
				System.out.println("1 "+successCount);
				System.out.println("2 "+StaticData.submitedOrders.getOrder(Session.getTable()).getMiniOrder().size());
					
				
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			refreshBtn.setEnabled(true);
			Intent i = new Intent(SentOrders.this, SentOrders.class);
			startActivity(i);
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();
		}
		
	}
	
	public class VerifyLogin extends AutomaticLogin {
		
		public VerifyLogin() {
			super(Session.getUserID(), Session.getPassword());
		}

		@Override
		protected void onPostExecute(Void result) {
			if (deviceExist) {

				RefreshOrder refresh = new RefreshOrder();
				refresh.execute();
			} else {

				Intent i = new Intent(SentOrders.this, Login.class);
				i.putExtra("myView", "fromSentOrders");
				i.putExtra("message", R.string.not_authorized_to_refresh);
				startActivity(i);
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

			}
		}
	}


}

