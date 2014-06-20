package com.ironone.restaurantapp.android.activity;

import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.logic.Session;
import android.os.Bundle;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PasscodeActivity extends Activity {

	String TAG = "com.ironone.restaurantapp.android.activity/PasscodeActivity.java";
	ProgressBar bar = null;
	TextView loadingMessage = null;
	boolean isQueue = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passcode);
		loadingMessage = (TextView) findViewById(R.id.txtConnectingToServerPasscode);
		bar = (ProgressBar) findViewById(R.id.progressBarConnectingToServerPasscode);
		bar.setVisibility(View.GONE);
		TextView waiterId = (TextView) findViewById(R.id.txtwaiterIdPasscode);
		waiterId.setText(Session.getUserID());
		loadingMessage.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	public void onSubmitClick(View v) {

		//Toast.makeText(this, "00", Toast.LENGTH_SHORT).show();
		EditText passCodeView = (EditText) findViewById(R.id.txtAccessCodePasscode);

		if (passCodeView.getText().toString().equals("")) {
			loadingMessage.setText(getApplicationContext().getString(
					R.string.error_accessCode));
			loadingMessage.setVisibility(View.VISIBLE);
			return;
		}
		if (!passCodeView.getText().toString().equals(Session.getPassword())) {
			loadingMessage.setText(getApplicationContext().getString(
					R.string.logging_to_device_failed));
			loadingMessage.setVisibility(View.VISIBLE);
			return;
		}

		Bundle extras = getIntent().getExtras();
		int previous_view_id = extras.getInt("myView");
		try {
			Intent i = null;
			if (isQueue) {
				i = new Intent(PasscodeActivity.this, QueueSettings.class);
			} else {
				i = new Intent(PasscodeActivity.this, SetTable.class);
			}
			i.putExtra("myView", previous_view_id);
			startActivity(i);
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (ActivityNotFoundException e) {
			Log.e(TAG + "/onSubmitClick()", e.getMessage());
			//Toast.makeText(this, "11"+e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			//Toast.makeText(this, "22"+e.getMessage(), Toast.LENGTH_LONG).show();
		}
		
	}

	public void onCancelClick(View v) {

		try {
			Bundle extras = getIntent().getExtras();
			int previous_view_id = extras.getInt("myView");

			switch (previous_view_id) {
			case R.layout.main:
				startActivity(new Intent(PasscodeActivity.this, MainMenu.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.itemsmenuview:
				startActivity(new Intent(PasscodeActivity.this,
						RestaurantItem.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.viewfoodcart:
				startActivity(new Intent(PasscodeActivity.this, MyOrder.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.description:
				startActivity(new Intent(PasscodeActivity.this,
						Description.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.sentorders:
				startActivity(new Intent(PasscodeActivity.this,
						SentOrders.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			case R.layout.suggestions:
				startActivity(new Intent(PasscodeActivity.this,
						AppSuggestions.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();

				break;

			default:
				startActivity(new Intent(PasscodeActivity.this,
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

}
