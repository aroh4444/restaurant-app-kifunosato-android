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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.logic.*;
import com.ironone.restaurantapp.android.synchronization.AutomaticLogin;
import com.ironone.restaurantapp.android.synchronization.ConfReader;
import com.ironone.restaurantapp.android.synchronization.ConnectionManager;
import com.ironone.restaurantapp.android.titlebar.TitleBar;
import com.ironone.restaurantapp.android.util.FormatUtil;

/**
 * 
 * @Created on Jul 17, 2012
 * @author DINESHPATHIRANA
 * 
 * 
 */
public class AppSuggestions extends TitleBar {

	String TAG = "com.ironone.restaurantapp.android.activity/AppSuggestions.java";
	Bitmap bm_item;
	ProgressDialog progress = null;

	private ArrayList<MiniOrder> currentOrders = (ArrayList<MiniOrder>) StaticData.currentOrder
			.getMiniOrder();
	private ArrayList<Menu> selectedMenus = new ArrayList<Menu>();
	private ArrayList<Menu> mainMenu = (ArrayList<Menu>) StaticData.fullmenu;

	/**
	 * this is first called when the activity loads
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggestions);
		loadNotSelectedSuggestionMenus();

		/** load title bar */
		try {
			LinearLayout header = (LinearLayout) findViewById(R.id.layout_heading_suggestions);
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewGroup vg = (ViewGroup) inflater
					.inflate(R.layout.titlebar, null);
			header.addView(vg);
			setHeaderTitle(getResources().getString(R.string.forget));

			ImageButton set = (ImageButton) vg
					.findViewById(R.id.SettingsButton);
			set.setId(R.layout.suggestions);

			setMyOrderItemCount();
			setSubmittedOrderItemCount();
			setTitlebarLanguage();

			TextView txtTableValue = (TextView) findViewById(R.id.txtTableNoValue_Title);
			txtTableValue.setText(": " + Session.getTable());

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

	}

	/**
	 * load suggestions menu list and suggestions text and other UI related
	 * contents
	 */
	public void loadNotSelectedSuggestionMenus() {
		ArrayList<Menu> differedList = getDifferedList();
		LinearLayout suggestionsLayout = (LinearLayout) findViewById(R.id.layout_suggestion_scroll);

		TextView suggestionMessage = (TextView) findViewById(R.id.suggestionMessage);

		TextView pendingPrice = (TextView) findViewById(R.id.textViewSuggestPendingOrderPrice);
		TextView confirmedOrderPrice = (TextView) findViewById(R.id.textViewConfirmedOrderPrice);
		TextView totalPrice = (TextView) findViewById(R.id.textViewTotalSuggestionsView);

		pendingPrice.setText(getTotalPrice());
		confirmedOrderPrice.setText(getTotalSentPrice());
		totalPrice.setText(getFullPrice());

		StringBuilder suggMessage = new StringBuilder();
		suggMessage.append(getResources().getString(
				R.string.suggestion_message_begin)
				+ " ");

		for (Menu menu : differedList) {
			bm_item = null;
			ImageView suggestions = new ImageView(this);
			TextView tv = new TextView(this);
			FrameLayout frml = new FrameLayout(this);

			/** set layout parameters */
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(212,
					350);
			lp.setMargins(0, 0, 10, 0);

			suggestions.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

			/** set item parameters */
			bm_item = Image.decodeSampledBitmapFromResource(menu.getImage()
					.getLocation(), 240, 400);

			if (bm_item != null) {
				suggestions.setImageBitmap(bm_item);
			}

			suggestions.setScaleType(ScaleType.FIT_XY);
			suggestions.setId(menu.getId());
			suggestions.setAdjustViewBounds(true);

			/** set text parameters */
			tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setBackgroundColor(Color.parseColor("#88ffffff"));
			tv.setPadding(15, 15, 15, 15);
			tv.setTextColor(Color.parseColor("#000000"));
			tv.setTextSize(16);
			tv.setTypeface(null, Typeface.BOLD);
			tv.setText(menu.getName());
			tv.setId(menu.getId());
			suggMessage.append(menu.getName() + ", ");

			frml.addView(suggestions);
			frml.addView(tv);
			suggestionsLayout.addView(frml, lp);

			/** set click listener */
			suggestions.setOnClickListener(new OnClickListener() {
				// menu item click
				public void onClick(View v) {

					ImageView b = (ImageView) findViewById(v.getId());
					b.setColorFilter(Color.argb(150, 155, 155, 155));

					// if there are sub-categories in the clicked category
					if (Menu.getChildMenu(mainMenu, v.getId()) != null) {
						try {
							StaticData.copyMenu = Menu.getChildMenu(mainMenu,
									v.getId());
							Intent i = new Intent(AppSuggestions.this,
									MainMenu.class);
							startActivity(i);
							overridePendingTransition(R.anim.right_slide_in,
									R.anim.right_slide_out);
							finish();
						} catch (ActivityNotFoundException e) {
							Log.e(TAG + "/Suggestions.OnClickListner()",
									e.getMessage());
						}

					} else {
						try {
							StaticData.items = Menu.getChildItems(mainMenu,
									v.getId());
							Intent i = new Intent(AppSuggestions.this,
									RestaurantItem.class);
							startActivity(i);
							overridePendingTransition(R.anim.right_slide_in,
									R.anim.right_slide_out);
							finish();
						} catch (ActivityNotFoundException e) {
							Log.e(TAG + "/Suggestions.OnClickListner()",
									e.getMessage());
						}
					}
				}
			});

		}
		try {
			String message = suggMessage.substring(0, suggMessage.length() - 2);
			message = message + ". "
					+ getResources().getString(R.string.suggestion_message_end);
			// suggMessage.append(". Would you like to order now?");
			suggestionMessage.setText(message);
		} catch (NotFoundException e) {
			Log.e(TAG, e.getMessage());
		}

	}

	public void onAcceptSuggestion(View view) {
		try {
			Intent intent = new Intent(this, MainMenu.class);
			StaticData.copyMenu = mainMenu;
			startActivity(intent);
			finish();
		} catch (ActivityNotFoundException e) {
			Log.e(TAG + "/onAcceptSuggestion", e.getMessage());
		}
	}

	public void onDiscardSuggestion(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				getApplicationContext().getString(
						R.string.confirm_submit_message))
				.setCancelable(false)
				.setPositiveButton(
						getApplicationContext().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								progress = ProgressDialog.show(
										AppSuggestions.this,
										"",
										getApplicationContext().getString(
												R.string.sending_order));

								/*************** send order request **********/
								SendOrder sendOrder = new SendOrder();
								sendOrder.execute();
								/***************************************/

							}
						})
				.setNegativeButton(
						getApplicationContext().getString(R.string.no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * 
	 * @return ArrayList of slected main menu objects
	 */
	public ArrayList<Menu> getSelectedMainmenuItems() {

		try {
			Item selectedItem = new Item();
			Menu selectedMenu = new Menu();
			for (MiniOrder orders : currentOrders) {
				selectedItem = orders.getItem();
				selectedMenu = getMainMenu(selectedItem);
				selectedMenus.add(selectedMenu);
			}
		} catch (Exception e) {
			Log.e(TAG + "/getSelectedMainmenuItems()", e.getMessage());
		}

		return selectedMenus;
	}

	/**
	 * 
	 * @param item
	 * @return Main menu object item resides in
	 */
	private Menu getMainMenu(Item item) {
		try {
			for (int i = 0; i < mainMenu.size(); i++) {
				if (Menu.getChildMenu(mainMenu, mainMenu.get(i).getId()) != null) {
					if (hasOrderedItem((ArrayList<Menu>) mainMenu.get(i)
							.getMenus(), item))
						return mainMenu.get(i);
				} else if ((ArrayList<Item>) Menu.getChildItems(mainMenu,
						mainMenu.get(i).getId()) != null) {
					ArrayList<Item> itemList = (ArrayList<Item>) mainMenu
							.get(i).getItems();
					for (int j = 0; j < itemList.size(); j++) {
						if (itemList.get(j).getId() == item.getId())
							return mainMenu.get(i);
					}
				}
			}
		} catch (Exception e) {
			Log.e(TAG + "/getMainMenu()", e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param menu
	 *            Menu object grill down to item level
	 * @param item
	 *            orderd item
	 * @return bollean to identify whether the orderd food item included in the
	 *         given menu
	 */
	private boolean hasOrderedItem(ArrayList<Menu> menu, Item item) {
		try {
			for (int i = 0; i < menu.size(); i++) {
				if (menu.get(i).getId() == item.getId())
					return true;
				/** if menu has sub menus **/
				else if (Menu.getChildMenu(menu, menu.get(i).getId()) != null)
					hasOrderedItem((ArrayList<Menu>) menu.get(i).getMenus(),
							item);

				/**
				 * if menu has items check availability of the item and return
				 * boolean else go to the next menu
				 */
				else if ((ArrayList<Item>) Menu.getChildItems(menu, menu.get(i)
						.getId()) != null) {
					ArrayList<Item> itemList = (ArrayList<Item>) menu.get(i)
							.getItems();
					for (int j = 0; j < itemList.size(); j++) {
						if (itemList.get(j).getId() == item.getId())
							return true;
					}
				} else
					return false;

			}
		} catch (Exception e) {
			Log.e(TAG + "/hasOrderedItem()", e.getMessage());
		}

		return false;

	}

	/**
	 * 
	 * @return suggestions menu list
	 */
	private ArrayList<Menu> getSuggestionsMenuList() {

		ArrayList<Menu> suggList = new ArrayList<Menu>();

		try {
			for (Menu menu : mainMenu) {
				if (menu.isSuggestion())
					suggList.add(menu);
			}
		} catch (Exception e) {
			Log.e(TAG + "/getSuggestionsMenuList()", e.getMessage());
		}
		return suggList;
	}

	/**
	 * 
	 * @param suggestions
	 *            :List of suggestion menus
	 * @param orders
	 *            : List of ordered menus
	 * @return difference of suggestion vs selected
	 */
	private ArrayList<Menu> getNotSelectedSuggestionsMenuList(
			ArrayList<Menu> suggestions, ArrayList<Menu> orders) {
		ArrayList<Menu> differList = new ArrayList<Menu>();
		try {
			for (int i = 0; i < suggestions.size(); i++) {
				boolean hasInOrders = false;
				for (int j = 0; j < orders.size(); j++) {
					if (suggestions.get(i).equals(orders.get(j))) {
						hasInOrders = true;
					}
				}
				if (hasInOrders == false)
					differList.add(suggestions.get(i));
			}
		} catch (Exception e) {
			Log.e(TAG + "/getNotSelectedSuggestionsMenuList()", e.getMessage());
		}
		return differList;
	}

	/**
	 * 
	 * @return differed list which includes the differentiation of ordered menus
	 *         vs suggested menus
	 */
	public ArrayList<Menu> getDifferedList() {
		ArrayList<Menu> selectedMenus = getSelectedMainmenuItems();
		ArrayList<Menu> suggestedMenus = getSuggestionsMenuList();
		ArrayList<Menu> differedList = getNotSelectedSuggestionsMenuList(
				suggestedMenus, selectedMenus);
		return differedList;
	}

	public Integer getItemCount() {
		Integer count = 0;
		try {
			for (MiniOrder order : currentOrders) {
				count = count + order.getNoOfItems();
			}
		} catch (Exception e) {
			Log.e(TAG + "/getItemCount()", e.getMessage());
		}
		return count;
	}

	public Integer getSentItemCount() {
		Integer count = 0;
		try {
			for (MiniOrder order : StaticData.submitedOrders.getOrder(
					Session.getTable()).getMiniOrder()) {
				count = count + order.getNoOfItems();
			}
		} catch (Exception e) {
			Log.e(TAG + "/getItemCount()", e.getMessage());
		}
		return count;
	}

	public String getTotalPrice() {
		Double total = 0.00;
		try {
			for (MiniOrder order : currentOrders) {
				if (order.getItem().getSizes().size() > 0)
					total += (order.getItem().getSelectedSize().getPrice() + order
							.getItem().getSelectedAddOnsCost())
							* order.getNoOfItems();
				else
					total += (order.getItem().getPrice() + order.getItem()
							.getSelectedAddOnsCost()) * order.getNoOfItems();
			}
		} catch (Exception e) {
			Log.e(TAG + "/getTotalPrice()", e.getMessage());
		}
		StaticData.totalPrice = total.toString();
		return StaticData.currency.getSymbol() + " " + FormatUtil.getNumberFormatted(total);
	}

	public String getTotalSentPrice() {
		double total = 0.00;
		try {
			for (MiniOrder order : StaticData.submitedOrders.getOrder(
					Session.getTable()).getMiniOrder()) {
				if (order.getItem().getSizes().size() > 0)
					total += (order.getItem().getSelectedSize().getPrice() + order
							.getItem().getSelectedAddOnsCost())
							* order.getNoOfItems();
				else
					total += (order.getItem().getPrice() + order.getItem()
							.getSelectedAddOnsCost()) * order.getNoOfItems();
			}
		} catch (Exception e) {
			Log.e(TAG + "/getTotalPrice()", e.getMessage());
		}
		return StaticData.currency.getSymbol() + " " + FormatUtil.getNumberFormatted(total);
	}

	public String getFullPrice() {
		double total = 0.00;
		try {
			for (MiniOrder order : currentOrders) {
				if (order.getItem().getSizes().size() > 0)
					total += (order.getItem().getSelectedSize().getPrice() + order
							.getItem().getSelectedAddOnsCost())
							* order.getNoOfItems();
				else
					total += (order.getItem().getPrice() + order.getItem()
							.getSelectedAddOnsCost()) * order.getNoOfItems();
			}
			for (MiniOrder order : StaticData.submitedOrders.getOrder(
					Session.getTable()).getMiniOrder()) {
				if (order.getItem().getSizes().size() > 0)
					total += (order.getItem().getSelectedSize().getPrice() + order
							.getItem().getSelectedAddOnsCost())
							* order.getNoOfItems();
				else
					total += (order.getItem().getPrice() + order.getItem()
							.getSelectedAddOnsCost()) * order.getNoOfItems();
			}
		} catch (Exception e) {
			Log.e(TAG + "/getTotalPrice()", e.getMessage());
		}
		return StaticData.currency.getSymbol() + " " + FormatUtil.getNumberFormatted(total);
	}

	@Override
	public void onBackPressed() {
		try {
			Intent i = new Intent(AppSuggestions.this, MyOrder.class);
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
			// if(StaticData.tableNo == null || StaticData.tableNo.isEmpty()) {
			// StaticData.tableNo = tableData.getTableNo().toString();
			// }

			String tableNum = StaticData.tableNo;
			Integer noOfItems = StaticData.currentOrder.getMiniOrder().size();
			String totalPrice = StaticData.totalPrice;
			Integer restaurantId = StaticData.restaurantId;
			String deviceId = StaticData.deviceid;
			String currencySymbol = StaticData.currency.getSymbol();

			System.out.println("****** Order Parameteres*********");
			System.out.println("Table numb :" + tableNum);
			System.out.println("Number Of items :" + noOfItems.toString());
			System.out.println("Total Price :" + totalPrice);
			System.out.println("Restaurant Id :" + restaurantId.toString());
			System.out.println("Currency symbol :" + currencySymbol);
			System.out.println(deviceId);
			System.out.println("**********************************");

			// if(false) {
			// for (MiniOrder miniOrder : StaticData.currentOrder
			// .getMiniOrder()) {
			// StaticData.successOrders.getOrder(Session.getTable()).getMiniOrder().add(
			// miniOrder);
			// }
			//
			// return null;
			// }

			try {

				URL url = new URL(
						new ConfReader(getApplicationContext()).getServerUrl()
								+ "orders?action=addOrder&format=json");
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

					URL itemURL = new URL(new ConfReader(
							getApplicationContext()).getServerUrl()
							+ "orders?action=addOrderItems&format=json");

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
						ItemNameValuePairs.add(new BasicNameValuePair(
								"specialinstruction", miniOrder
										.getSpecialInstruction()));

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
							StaticData.successOrders
									.getOrder(Session.getTable())
									.getMiniOrder().add(miniOrder);
						}
						if (itemResponseType.equalsIgnoreCase("error")) {
							StaticData.failOrders.getOrder(Session.getTable())
									.getMiniOrder().add(miniOrder);
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
			StaticData.submitedOrders
					.getOrder(Session.getTable())
					.getMiniOrder()
					.addAll(StaticData.successOrders.getOrder(
							Session.getTable()).getMiniOrder());

			try {
				StaticData.successOrders.getOrder(Session.getTable())
						.getMiniOrder().clear();
			} catch (UnsupportedOperationException e) {
				e.printStackTrace();
			}
			if (StaticData.submitedOrders.getOrder(Session.getTable())
					.getMiniOrder().size() > 0) {
				StaticData.currentOrder.getMiniOrder().clear();
				startActivity(new Intent(AppSuggestions.this, SentOrders.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();
				return;
			}

			if (orderResopnseType.equalsIgnoreCase("data")
					&& successItemCount == StaticData.currentOrder
							.getMiniOrder().size()) {
				StaticData.currentOrder.getMiniOrder().clear();

				startActivity(new Intent(AppSuggestions.this, SentOrders.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();
			} else if (orderResopnseType.equalsIgnoreCase("data")
					&& !(successItemCount == StaticData.currentOrder
							.getMiniOrder().size())) {
				StaticData.currentOrder.getMiniOrder().clear();
				StaticData.currentOrder.getMiniOrder().addAll(
						StaticData.failOrders.getOrder(Session.getTable())
								.getMiniOrder());

				if (StaticData.failOrders.getOrder(Session.getTable())
						.getMiniOrder().size() != 0) {
					String message = getApplicationContext().getString(
							R.string.error_sending_order);
					Toast.makeText(getApplicationContext(), message,
							Toast.LENGTH_SHORT).show();
				}
				try {
					StaticData.failOrders.getOrder(Session.getTable())
							.getMiniOrder().clear();
				} catch (UnsupportedOperationException e) {
				}
				startActivity(new Intent(AppSuggestions.this, SentOrders.class));
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

				Intent i = new Intent(AppSuggestions.this, Login.class);
				i.putExtra("myView", "fromSuggestions");
				i.putExtra("message", R.string.not_authorized_to_submit);
				startActivity(i);
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

			}
		}
	}

}
