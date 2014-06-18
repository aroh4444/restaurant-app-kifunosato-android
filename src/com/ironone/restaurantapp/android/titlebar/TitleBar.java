package com.ironone.restaurantapp.android.titlebar;


import java.util.List;
import java.util.Locale;
import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.activity.AppSuggestions;
import com.ironone.restaurantapp.android.activity.MainMenu;
import com.ironone.restaurantapp.android.activity.MyOrder;
import com.ironone.restaurantapp.android.activity.PasscodeActivity;
import com.ironone.restaurantapp.android.activity.SelectLanguage;
import com.ironone.restaurantapp.android.activity.SentOrders;
import com.ironone.restaurantapp.android.logic.Image;
import com.ironone.restaurantapp.android.logic.Language;
import com.ironone.restaurantapp.android.logic.StaticData;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * this class is to create title bar on activities and all other activities
 * should extend from this class
 * 
 * @author DINESHPATHIRANA
 * 
 */

public class TitleBar extends Activity {

	String TAG = "com.ironone.restaurantapp.android.titlebar/TitleBar";

	private Button homeButton;
	private Button backButton;
	private ImageButton settingsButton;
	private ImageButton languageButton;
	private RelativeLayout myOrdersButton;
	private RelativeLayout submitedOrdersButton;

	/** this is called when the activity starts **/
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.titlebar);
			
		} catch (Exception e) {
			Log.e(TAG + "/onCreate", e.getMessage());
		}
	}

	/**
	 * hide title bar back button
	 **/
	public void hideBackButton() {
		backButton = (Button) findViewById(R.id.BackButton);
		backButton.setVisibility(View.GONE);
	}

	/**
	 * hide title bar home button
	 **/
	public void hideHomeButton() {
		homeButton = (Button) findViewById(R.id.HomeButton);
		homeButton.setVisibility(View.GONE);
	}

	/**
	 * hide title bar settings button
	 **/
	public void hideSettingsButton() {
		settingsButton = (ImageButton) findViewById(R.id.SettingsButton);
		settingsButton.setVisibility(View.GONE);
	}

	/**
	 * hide title bar language button
	 **/
	public void hideLanguageButton() {
		languageButton = (ImageButton) findViewById(R.id.LanguageButton);
		languageButton.setVisibility(View.GONE);
	}

	/**
	 * hide my orders button
	 **/
	public void hideMyOrdersButton() {
		myOrdersButton = (RelativeLayout) findViewById(R.id.layoutNotifications_myOrders);
		myOrdersButton.setVisibility(View.GONE);
	}

	/**
	 * hide submitted orders button
	 **/
	public void hideSubmittedOrdersButton() {
		submitedOrdersButton = (RelativeLayout) findViewById(R.id.layoutNotifications_submittedOrders);
		submitedOrdersButton.setVisibility(View.GONE);
	}

	/**
	 * called when title bar back button clicked
	 * 
	 * @param view
	 *            to get the appropriate button view
	 * 
	 **/
	public void onBackButtonClick(View view) {
		try {
			onBackPressed();
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
		} catch (Exception e) {
			Log.e(TAG + "/onBackButtonClick()", e.getMessage());
		}

	}

	/**
	 * called when title bar home button clicked
	 * 
	 * @param view
	 *            to get the appropriate button view
	 * 
	 **/
	public void onHomeButtonClick(View view) {
		try {
			Intent intent = new Intent(this, MainMenu.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			StaticData.copyMenu = StaticData.fullmenu;
			startActivity(intent);
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (ActivityNotFoundException e) {
			Log.e(TAG + "/onHomeButtonClick()", e.getMessage());
		}
	}

	/**
	 * called when title bar MyOrder button clicked
	 * 
	 * @param view
	 *            to get the appropriate button view
	 * 
	 **/
	public void onMyOrderButtonClick(View view) {
		try {
			startActivity(new Intent(this, MyOrder.class));
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (ActivityNotFoundException e) {
			Log.e(TAG + "/onMyOrderButtonClick()", e.getMessage());
		}
	}

	/**
	 * called when title bar SentOrder button clicked
	 * 
	 * @param view
	 *            to get the appropriate button view
	 * 
	 **/
	public void onSentOrderButtonClick(View view) {
		try {
			startActivity(new Intent(this, SentOrders.class));
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (ActivityNotFoundException e) {
			Log.e(TAG + "/onSentOrderButtonClick()", e.getMessage());
		}
	}

	/**
	 * called when title bar settings button clicked
	 * 
	 * @param view
	 *            to get the appropriate button view
	 * 
	 **/
	public void onSettingsButtonClick(View view) {
		try {
			Intent intent = new Intent(this, PasscodeActivity.class);
			intent.putExtra("myView", view.getId());
			startActivity(intent);
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (ActivityNotFoundException e) {
			Log.e(TAG + "/onSettingsButtonClick()", e.getMessage());
		}
	}

	/**
	 * called when title bar language selection button clicked
	 * 
	 * @param view
	 *            to get the appropriate button view
	 * 
	 **/
	public void onLanguageButtonClick(View view) {
		try {
			Animation anim = AnimationUtils.loadAnimation(TitleBar.this, R.anim.button_click);
			anim.setFillEnabled(true);
			anim.setFillAfter(true);
			view.startAnimation(anim);
			Intent intent_settings = new Intent(this, SelectLanguage.class);
			startActivity(intent_settings);
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (ActivityNotFoundException e) {
			Log.e(TAG + "/onLanguageButtonClick()", e.getMessage());
		}
	}

	/**
	 * this method is to change the title bar title
	 * 
	 * @param title
	 *            : get the title for the title bar
	 * 
	 **/
	public void setHeaderTitle(String title) {
		TextView titleView = (TextView) findViewById(R.id.ActivityTitle);
		titleView.setText(title);
	}

	/**
	 * this method is to set my orders item count
	 * 
	 * @param count
	 *            : pass number of items
	 * 
	 **/
	public void setMyOrderItemCount() {
		AppSuggestions app = new AppSuggestions();
		Integer count = app.getItemCount();
		try {
			if (count > 0) {
				String countString;
				if (count < 10) {
					countString = "0" + count.toString();
				} else {
					countString = count.toString();
				}
				TextView numOfItemsText = (TextView) findViewById(R.id.Notification_myorders_itemcount);
				ImageView numOfItemsButton = (ImageView) findViewById(R.id.NotificationButton_myordrs);
				numOfItemsText.setText(countString);
				numOfItemsButton.setVisibility(View.VISIBLE);
				numOfItemsText.setVisibility(View.VISIBLE);
			} else {
				TextView numOfItemsText = (TextView) findViewById(R.id.Notification_myorders_itemcount);
				ImageView numOfItemsButton = (ImageView) findViewById(R.id.NotificationButton_myordrs);
				numOfItemsButton.setVisibility(View.GONE);
				numOfItemsText.setVisibility(View.GONE);
			}
		} catch (NullPointerException e) {
			Log.e(TAG + "/setMyOrderItemCount()", e.getMessage());
		} catch (Exception e) {
			Log.e(TAG + "/setMyOrderItemCount()", e.getMessage());
		}
	}

	/**
	 * this method is to set submitted orders item count
	 * 
	 * @param count
	 *            : pass number of items
	 * 
	 **/
	public void setSubmittedOrderItemCount() {
		AppSuggestions app = new AppSuggestions();
		Integer count = app.getSentItemCount();
		try {
			if (count > 0) {
				String countString;
				if (count < 10) {
					countString = "0" + count.toString();
				} else {
					countString = count.toString();
				}
				TextView numOfItemsText = (TextView) findViewById(R.id.Notification_submitorders_itemcount);
				ImageView numOfItemsButton = (ImageView) findViewById(R.id.NotificationButton_submittedOrders);
				numOfItemsText.setText(countString);
				numOfItemsButton.setVisibility(View.VISIBLE);
				numOfItemsText.setVisibility(View.VISIBLE);
			} else {
				TextView numOfItemsText = (TextView) findViewById(R.id.Notification_submitorders_itemcount);
				ImageView numOfItemsButton = (ImageView) findViewById(R.id.NotificationButton_submittedOrders);
				numOfItemsButton.setVisibility(View.GONE);
				numOfItemsText.setVisibility(View.GONE);
			}
		} catch (NullPointerException e) {
			Log.e(TAG + "/setSubmittedOrderItemCount()", e.getMessage());
		} catch (Exception e) {
			Log.e(TAG + "/setSubmittedOrderItemCount()", e.getMessage());
		}
	}

	public void setTitlebarLanguage() {

		try {
			final List<Language> langList = StaticData.languages;
			ImageButton language = (ImageButton) findViewById(R.id.LanguageButton);
			for (Language lang : langList) {				
				if (Locale.getDefault().getLanguage()
						.equals(lang.getLanID().toString())) {
					Bitmap bm_item = null;
					bm_item = Image.decodeSampledBitmapFromResource(lang.getImage().getLocation(), 60, 50);
					
					if(bm_item != null){
						language.setImageBitmap(bm_item);
					}		
					
					language.getLayoutParams().height = 45;
					language.getLayoutParams().width = 55;

					ImageButton settings = (ImageButton) findViewById(R.id.SettingsButton);
					settings.getLayoutParams().height = 45;
					settings.getLayoutParams().width = 45;

				}

			}

		} catch (NullPointerException e) {
			Log.e(TAG + "/setTitlebarLanguage()", e.getMessage());
		} catch (Exception e) {
			Log.e(TAG + "/setTitlebarLanguage()", e.getMessage());
		}

	}

}
