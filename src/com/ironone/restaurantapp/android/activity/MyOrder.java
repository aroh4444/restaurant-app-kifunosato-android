package com.ironone.restaurantapp.android.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.ironone.restaurantapp.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.ironone.restaurantapp.android.logic.AddOn;
import com.ironone.restaurantapp.android.logic.Image;
import com.ironone.restaurantapp.android.logic.Item;
import com.ironone.restaurantapp.android.logic.MiniOrder;
import com.ironone.restaurantapp.android.logic.Session;
import com.ironone.restaurantapp.android.logic.StaticData;
import com.ironone.restaurantapp.android.synchronization.AutomaticLogin;
import com.ironone.restaurantapp.android.synchronization.ConfReader;
import com.ironone.restaurantapp.android.synchronization.ConnectionManager;
import com.ironone.restaurantapp.android.titlebar.TitleBar;
import com.ironone.restaurantapp.android.util.FormatUtil;

public class MyOrder extends TitleBar {

	String delimiter = "_";
	View layout;
	RadioGroup radGroup;
	Item item;
	PopupWindow popup;
	int selectedRadio = 0;
	TableLayout table;
	List<MiniOrder> miniOrderList;
	Integer selectedItem = 0;
	AppSuggestions suggestion;
	TextView myOrdersPrice;
	Integer removeItem = -1;
	TextView totalprice;
	TextView sentPrice;
	ProgressDialog progress = null;
	
	Bitmap bm_item = null;
	String TAG = "com.ironone.restaurantapp.android.activity/MyOrder.java";

	
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewfoodcart);

		try {
			Bundle extras = getIntent().getExtras();
			removeItem = extras.getInt("remove");

		} catch (Exception e) {
		}
		
		
		table = (TableLayout) findViewById(R.id.tableLayoutFoodItems);

		LinearLayout header = (LinearLayout) findViewById(R.id.layout_heading_foodCart);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.titlebar, null);
		header.addView(vg);
		setHeaderTitle(getApplicationContext().getString(R.string.my_order));
		miniOrderList = StaticData.currentOrder.getMiniOrder();
		setMyOrderItemCount();
		setSubmittedOrderItemCount();
		setTitlebarLanguage();

		ImageButton set = (ImageButton) vg.findViewById(R.id.SettingsButton);
		set.setId(R.layout.viewfoodcart);

		suggestion = new AppSuggestions();
		myOrdersPrice = (TextView) findViewById(R.id.textViewMyOrderPrice);
		totalprice = (TextView) findViewById(R.id.textViewTotal);
		sentPrice = (TextView) findViewById(R.id.textViewSentOrderPrice);

		for (int i = 0; i < miniOrderList.size(); i++) {

			LayoutInflater layoutInflater = LayoutInflater
					.from(getBaseContext());
			layout = new View(this);
			layout = layoutInflater.inflate(R.layout.dynamicfoodcart, null);

			myOrdersPrice.setText(suggestion.getTotalPrice());
			sentPrice.setText(suggestion.getTotalSentPrice());
			totalprice.setText(suggestion.getFullPrice());

			TextView textViewFoodItemName = (TextView) layout
					.findViewById(R.id.txt_edit_food_name);
			TextView textViewFoodItemCalory = (TextView) layout
					.findViewById(R.id.txt_edit_food_calory);
			TextView textViewFoodItemPrice = (TextView) layout
					.findViewById(R.id.txt_edit_food_price);
			TextView textViewFoodItemQuantity = (TextView) layout
					.findViewById(R.id.txt_edit_food_quantity);
			final ImageButton delete = (ImageButton) layout
					.findViewById(R.id.imageButtonRemove);
			final ImageButton increase = (ImageButton) layout
					.findViewById(R.id.imageButtonIncreaseAmount);
			final ImageButton decrease = (ImageButton) layout
					.findViewById(R.id.imageButtonDecreaseAmount);

			Item tempItem = miniOrderList.get(i).getItem();

			int selectedSize = 0;

			ImageView foodImg = (ImageView) layout
					.findViewById(R.id.image_edit_food);
			bm_item = Image.decodeSampledBitmapFromResource(tempItem.getImage()
					.getLocation(), 200, 200);

			Bitmap finalBitmap = null;

			try {
				finalBitmap = Bitmap.createBitmap(bm_item, 24, 44, 120, 72);
				foodImg.setImageBitmap(finalBitmap);
			} catch (Exception e) {
				Log.d(TAG, " image not found");
			}

			textViewFoodItemName.setText(miniOrderList.get(i).getItem()
					.getName());

			Double price;
			if (miniOrderList.get(i).getItem().getSizes().size() > 0) {
				price = miniOrderList.get(i).getItem().getSelectedSize()
						.getPrice();

				for (int j = 0; j < tempItem.getSizes().size(); j++) {
					if (tempItem.getSelectedSize().getName() == tempItem
							.getSizes().get(j).getName()) {
						selectedSize = j;
					}
				}

				textViewFoodItemCalory.setText(miniOrderList.get(i).getItem()
						.getSizes().get(0).getType()
						+ " : "
						+ miniOrderList.get(i).getItem().getSizes()
								.get(selectedSize).getName());

			} else {
				price = miniOrderList.get(i).getItem().getPrice();

			}
			textViewFoodItemPrice.setText(StaticData.currency.getSymbol()
					+ " "
					+ FormatUtil.getNumberFormatted((price+ tempItem.getSelectedAddOnsCost())
							* miniOrderList.get(i).getNoOfItems()));

			textViewFoodItemQuantity.setText(miniOrderList.get(i)
					.getNoOfItems().toString());

			TableRow tr = new TableRow(this);

			tr.setTag(i);
			tr.setId(i);
			tr.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					selectedItem = v.getId();
					TableRow temp = (TableRow) findViewById(v.getId());
					temp.setBackgroundColor(Color.argb(150, 155, 155, 155));
					try {
						StaticData.item = StaticData.currentOrder
								.getMiniOrder().get(selectedItem).getItem();
						Intent i = new Intent(MyOrder.this, Description.class);
						i.putExtra("fromOrder", selectedItem);
						startActivity(i);
						overridePendingTransition(R.anim.right_slide_in,
								R.anim.right_slide_out);
						finish();
					} catch (Exception e) {
						Log.e("", e.getMessage());
					}
				}
			});

			textViewFoodItemQuantity.setTag(i + "_quantity");
			textViewFoodItemPrice.setTag(i + "_price");
			delete.setTag(i + "_del");
			delete.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String[] temp = delete.getTag().toString().split(delimiter);
					onDeleteClick(temp, miniOrderList, table);
				}
			});
			decrease.setTag(i + "_dec");
			decrease.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					String[] temp = decrease.getTag().toString()
							.split(delimiter);
					int n = Integer.parseInt(temp[0]);
					if (miniOrderList.get(n).getNoOfItems() > 1) {
						onDecreaseClick(n, table, miniOrderList);
					}

				}
			});

			increase.setTag(i + "_inc");
			increase.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String[] temp = increase.getTag().toString()
							.split(delimiter);
					int n = Integer.parseInt(temp[0]);
					onIncreaseClick(n, table, miniOrderList);
				}
			});
			
			if (tempItem.getAddOnes().size() > 0) {
				
				

				((LinearLayout) layout.findViewById(R.id.lnAddOnDynamicFood))
						.setVisibility(View.VISIBLE);

				LinearLayout addOnLayout = (LinearLayout) layout.findViewById(R.id.checkBoxlayoutDynamicFood);

				for (int j = 0; j < tempItem.getSelectedAddOnes().size(); j++) {

					AddOn addOn = tempItem.getSelectedAddOnes().get(j);
					TextView txtAddOn = new TextView(this);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lp.setMargins(0, 1, 0, 1);

					
					//txtAddOn.setTag(addOn.getId());
					txtAddOn.setText(addOn.getName().toString() + " - "
							+ StaticData.currency.getSymbol() + " "
							+ FormatUtil.getNumberFormatted(addOn.getPrice()));
					Log.e("Test", txtAddOn.getText().toString());
					txtAddOn.setPadding(50, 0, 0, 0);
					txtAddOn.setTextColor(Color.BLACK);
					addOnLayout.addView(txtAddOn, 2 * j, lp);

					// adding a line between Add On buttons
					if (j != tempItem.getAddOnes().size() - 1) {

						View line = new View(this);
						line.setLayoutParams(new LayoutParams(
								LayoutParams.FILL_PARENT, 1));
						line.setBackgroundColor(Color.BLACK);
						addOnLayout.addView(line, 2 * j + 1);
					}
					
				}

			}
			else {
				((LinearLayout) layout.findViewById(R.id.lnAddOnDynamicFood))
				.setVisibility(View.GONE);
			}

			TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 0, 2);
			tr.addView(layout);
			table.addView(tr, lp);
		}

		if (removeItem != -1) {
			String[] temp = { removeItem.toString(), "_del" };
			onDeleteClick(temp, miniOrderList, table);
		}
		
		TextView txtTableValue = (TextView)findViewById(R.id.txtTableNoValue_Title);
		txtTableValue.setText(": " + Session.getTable());
		
		
		
		
		
	}

	
	
    
	public void onCancelInPopup(View v) {
		selectedRadio = 0;
		popup.dismiss();

	}

	public void onDeleteClick(String[] temp, List<MiniOrder> itemList,
			TableLayout table) {
		int n = Integer.parseInt(temp[0]);
		table.removeView(table.getChildAt(n));
		itemList.remove(n);
		StaticData.currentOrder.setMiniOrder(itemList);
		setMyOrderItemCount();

		for (Integer i = n + 1; i <= itemList.size(); i++) {
			TableRow tr = (TableRow) table.findViewById(i);
			tr.setId(i - 1);
			ImageButton btn = (ImageButton) table.findViewWithTag(i.toString()
					+ "_del");
			btn.setTag(i - 1 + "_del");
			btn = (ImageButton) table.findViewWithTag(i.toString() + "_inc");
			btn.setTag(i - 1 + "_inc");
			btn = (ImageButton) table.findViewWithTag(i.toString() + "_dec");
			btn.setTag(i - 1 + "_dec");
			TextView tv = (TextView) table.findViewWithTag(i.toString()
					+ "_quantity");
			tv.setTag(i - 1 + "_quantity");
			tv = (TextView) table.findViewWithTag(i.toString() + "_price");
			tv.setTag(i - 1 + "_price");

		}
		myOrdersPrice.setText(suggestion.getTotalPrice());
		sentPrice.setText(suggestion.getTotalSentPrice());
		totalprice.setText(suggestion.getFullPrice());
	}

	public void onIncreaseClick(int n, TableLayout table,
			List<MiniOrder> miniOrderList) {
		miniOrderList.get(n).setNoOfItems(
				miniOrderList.get(n).getNoOfItems() + 1);
		StaticData.currentOrder.setMiniOrder(miniOrderList);
		TextView tv = (TextView) table.findViewWithTag(n + "_quantity");
		tv.setText(miniOrderList.get(n).getNoOfItems().toString());
		tv = (TextView) table.findViewWithTag(n + "_price");
		Double price;
		if (miniOrderList.get(n).getItem().getSizes().size() > 0)
			price = miniOrderList.get(n).getItem().getSelectedSize().getPrice();
		else
			price = miniOrderList.get(n).getItem().getPrice();
		tv.setText(StaticData.currency.getSymbol() + " "
				+ FormatUtil.getNumberFormatted((price + miniOrderList.get(n).getItem().getSelectedAddOnsCost()) * miniOrderList.get(n).getNoOfItems()));
		myOrdersPrice.setText(suggestion.getTotalPrice());
		sentPrice.setText(suggestion.getTotalSentPrice());
		totalprice.setText(suggestion.getFullPrice());
		setMyOrderItemCount();
	}

	public void onDecreaseClick(int n, TableLayout table,
			List<MiniOrder> miniOrderList) {
		miniOrderList.get(n).setNoOfItems(
				miniOrderList.get(n).getNoOfItems() - 1);
		StaticData.currentOrder.setMiniOrder(miniOrderList);
		TextView tv = (TextView) table.findViewWithTag(n + "_quantity");
		tv.setText(miniOrderList.get(n).getNoOfItems().toString());
		Double price;
		if (miniOrderList.get(n).getItem().getSizes().size() > 0)
			price = miniOrderList.get(n).getItem().getSelectedSize().getPrice();
		else
			price = miniOrderList.get(n).getItem().getPrice();

		tv = (TextView) table.findViewWithTag(n + "_price");
		tv.setText(StaticData.currency.getSymbol() + " "
				+ FormatUtil.getNumberFormatted((price+ miniOrderList.get(n).getItem().getSelectedAddOnsCost()) * miniOrderList.get(n).getNoOfItems()));
		myOrdersPrice.setText(suggestion.getTotalPrice());
		sentPrice.setText(suggestion.getTotalSentPrice());
		totalprice.setText(suggestion.getFullPrice());
		setMyOrderItemCount();
	}
	
	public void onPrintOrderClick(View v) {
		Intent serverIntent = new Intent(this, PrintPreview.class);
		startActivity(serverIntent);
	}

	public void onSubmitOrderClick(View v) {

		if (StaticData.currentOrder.getMiniOrder().size() > 0) {
			if (suggestion.getDifferedList().size() > 0) {
				startActivity(new Intent(MyOrder.this, AppSuggestions.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);

				finish();
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MyOrder.this);
				builder.setMessage(getApplicationContext().getString(
						R.string.confirm_submit_message));
				builder.setCancelable(false);
				builder.setPositiveButton(
						getApplicationContext().getString(R.string.yes),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

								progress = ProgressDialog.show(
										MyOrder.this,
										"",
										getApplicationContext().getString(
												R.string.sending_order));
//								StaticData.submitedOrders.getOrder(Session.getTable()).getMiniOrder()
//										.addAll(StaticData.currentOrder
//												.getMiniOrder());

								SendOrder sendOrder = new SendOrder();
								sendOrder.execute();

							}
						});

				builder.setNegativeButton(
						getApplicationContext().getString(R.string.no),
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();

							}
						});

				AlertDialog alert = builder.create();
				alert.show();

			}
		} else {
			Toast.makeText(getApplicationContext(),
					R.string.submit_empty_order_message, Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	public void onBackPressed() {
		try {
			StaticData.copyMenu = StaticData.fullmenu;
			Intent i = new Intent(MyOrder.this, MainMenu.class);
			startActivity(i);
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (ActivityNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
	}

	public class SendOrder extends AsyncTask<Void, Void, Void> {

		StringBuffer responseString = new StringBuffer();
		String responseValue = "";
		DefaultHttpClient httpclient;
		String orderResopnseType = "";
		int successItemCount = 0;

		@Override
		protected Void doInBackground(Void... params) {

			ConnectionManager manager = new ConnectionManager();
			
			try {
				httpclient = manager.getClient();
			} catch (ClientProtocolException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}

//			TableDataAccess tableData = new TableDataAccess(
//					getApplicationContext());
//			if(StaticData.tableNo == null || StaticData.tableNo.isEmpty()) {
//				StaticData.tableNo = tableData.getTableNo().toString();
//			}

			String tableNum = StaticData.tableNo;
			Integer noOfItems = StaticData.currentOrder.getMiniOrder().size();
			String totalPrice = FormatUtil.getNumberFormatted(StaticData.totalPrice);
			Integer restaurantId = StaticData.restaurantId;
			String deviceId = StaticData.deviceid;
			String currencySymbol = StaticData.currency.getSymbol();

			System.out.println("****** Order Parameteres*********");
			System.out.println("Table no :" + tableNum);
			System.out.println("Number Of items :" + noOfItems.toString());
			System.out.println("Total Price :" + totalPrice);
			System.out.println("Restaurant Id :" + restaurantId.toString());
			System.out.println("Currency symbol :" + currencySymbol);
			System.out.println(deviceId);
			System.out.println("**********************************");

			try {

				URL url = new URL(new ConfReader(getApplicationContext()).getServerUrl()+
						"orders?action=addOrder&format=json");
				HttpPost httppost = new HttpPost(url.toString());

				/** Add data **/
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);

				nameValuePairs.add(new BasicNameValuePair("tableNumber",
						tableNum));
				nameValuePairs.add(new BasicNameValuePair("noOfItems",
						noOfItems.toString()));
				nameValuePairs.add(new BasicNameValuePair("totalPrice",
						totalPrice));
				nameValuePairs.add(new BasicNameValuePair("restaurantId",
						restaurantId.toString()));
				nameValuePairs
						.add(new BasicNameValuePair("deviceId", deviceId));
				nameValuePairs.add(new BasicNameValuePair("currencySymbol",
						currencySymbol));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				/** Execute HTTP Post Request. **/
				HttpResponse response = httpclient.execute(httppost);

				HttpEntity httpEntity = response.getEntity();
				InputStream is = httpEntity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				String line;
				while ((line = reader.readLine()) != null) {
					responseString.append(line);
				}

				System.out.println("Response for Order Request : "
						+ responseString.toString());

				JSONObject jObject = new JSONObject();
				try {
					jObject = new JSONObject(responseString.toString());
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String orderId = "";
				try {
					orderId = jObject.get("orderid").toString().trim();
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				try {
					orderResopnseType = jObject.get("type").toString().trim();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				
				

				if (orderResopnseType.equalsIgnoreCase("data")
						&& orderId != null) {
					successItemCount = 0;

					URL itemURL = new URL(
							new ConfReader(getApplicationContext()).getServerUrl()
							+"orders?action=addOrderItems&format=json");

					for (MiniOrder miniOrder : StaticData.currentOrder
							.getMiniOrder()) {

						HttpPost itemHttppost = new HttpPost(itemURL.toString());

						StringBuffer ItemResponseString = new StringBuffer();

						List<NameValuePair> ItemNameValuePairs = new ArrayList<NameValuePair>(
								2);

						ItemNameValuePairs.add(new BasicNameValuePair(
								"itemName", miniOrder.getItem().getName()));
						ItemNameValuePairs.add(new BasicNameValuePair(
								"orderId", orderId));
						ItemNameValuePairs
								.add(new BasicNameValuePair(
										"price",
										miniOrder.getItem().getSizes().size() > 0 ? miniOrder
												.getItem().getSelectedSize()
												.getPrice().toString()
												: miniOrder.getItem()
														.getPrice().toString()));
						ItemNameValuePairs
								.add(new BasicNameValuePair("quantity",
										miniOrder.getNoOfItems().toString()));
						ItemNameValuePairs
								.add(new BasicNameValuePair("size",
										miniOrder.getItem().getSelectedSize()
												.getName() != null ? miniOrder
												.getItem().getSelectedSize()
												.getName() : ""));
						ItemNameValuePairs
						.add(new BasicNameValuePair("specialinstruction",
								miniOrder.getSpecialInstruction()));

						itemHttppost.setEntity(new UrlEncodedFormEntity(
								ItemNameValuePairs));

						/** Execute HTTP Post Request **/
						HttpResponse Itemresponse = httpclient
								.execute(itemHttppost);

						HttpEntity itemHttpEntity = Itemresponse.getEntity();
						InputStream itemIs = itemHttpEntity.getContent();
						BufferedReader itemReader = new BufferedReader(
								new InputStreamReader(itemIs));

						String line2;
						while ((line2 = itemReader.readLine()) != null) {
							ItemResponseString.append(line2);
						}

						System.out.println("response String for "
								+ miniOrder.getItem().getName() + " :"
								+ ItemResponseString.toString());

						JSONObject itemJsonObject = new JSONObject();
						try {
							itemJsonObject = new JSONObject(
									ItemResponseString.toString());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						String itemId = "";
						try {
							itemId = itemJsonObject.get("itemid").toString()
									.trim();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						String itemResponseType = "";
						try {
							itemResponseType = itemJsonObject.get("type")
									.toString().trim();
						} catch (JSONException e) {
							e.printStackTrace();
						}

						if (itemResponseType.equalsIgnoreCase("data")) {
							successItemCount++;
							miniOrder.getItem().setItemId(itemId);
							StaticData.successOrders.getOrder(Session.getTable()).getMiniOrder().add(
									miniOrder);
						}
						if (itemResponseType.equalsIgnoreCase("error")) {
							StaticData.failOrders.getOrder(Session.getTable()).getMiniOrder().add(miniOrder);
						}

					}

				} else if (orderResopnseType.equalsIgnoreCase("error")) {

					VerifyLogin verify = new VerifyLogin();
					verify.execute();
				}

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			StaticData.submitedOrders.getOrder(Session.getTable()).getMiniOrder().addAll(
					StaticData.successOrders.getOrder(Session.getTable()).getMiniOrder());

			try {
				StaticData.successOrders.getOrder(Session.getTable()).getMiniOrder().clear();
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			}

			if (orderResopnseType.equalsIgnoreCase("data")
					&& successItemCount == StaticData.currentOrder
							.getMiniOrder().size()) {
				StaticData.currentOrder.getMiniOrder().clear();

				startActivity(new Intent(MyOrder.this, SentOrders.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();
			} else if (orderResopnseType.equalsIgnoreCase("data")
					&& !(successItemCount == StaticData.currentOrder
							.getMiniOrder().size())) {
				StaticData.currentOrder.getMiniOrder().clear();
				StaticData.currentOrder.getMiniOrder().addAll(
						StaticData.failOrders.getOrder(Session.getTable()).getMiniOrder());

				if (StaticData.failOrders.getOrder(Session.getTable()).getMiniOrder().size() != 0) {
					String message = getApplicationContext().getString(
							R.string.error_sending_order);
					Toast.makeText(getApplicationContext(), message,
							Toast.LENGTH_SHORT).show();
				}
				try {
					StaticData.failOrders.getOrder(Session.getTable()).getMiniOrder().clear();
				} catch (UnsupportedOperationException e) {
				}
				startActivity(new Intent(MyOrder.this, SentOrders.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();
			} else {
				progress.dismiss();
				Toast.makeText(
						getApplicationContext(),
						getApplicationContext().getString(
								R.string.try_again_to_submit),
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	public class VerifyLogin extends AutomaticLogin {

		public VerifyLogin() {
			super(Session.getUserID(), Session.getPassword());
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if (deviceExist) {

				SendOrder sendOrder = new SendOrder();
				sendOrder.execute();
			} else {

				Intent i = new Intent(MyOrder.this, Login.class);
				i.putExtra("myView", "fromMyOrders");
				i.putExtra("message", R.string.not_authorized_to_submit);
				startActivity(i);
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

			}
		}
	}

}
