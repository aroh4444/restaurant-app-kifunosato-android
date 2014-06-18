/*
 * Login.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.activity;

import org.apache.http.impl.client.DefaultHttpClient;
import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.dbhandler.RoleDataAccess;
import com.ironone.restaurantapp.android.log.AppLog;
import com.ironone.restaurantapp.android.logic.Session;
import com.ironone.restaurantapp.android.logic.StaticData;
import com.ironone.restaurantapp.android.logic.TempSession;
import com.ironone.restaurantapp.android.synchronization.AutomaticLogin;
import com.ironone.restaurantapp.android.synchronization.ConnectionManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * {Insert class description here}
 * 
 * @Created on Sep 12, 2012 : 12:03:40 PM
 * @author malakag
 */

public class Login extends Activity {

	TextView error = null;
	EditText waiterId = null;
	EditText accessCode = null;
	String previousView = "";
	String message = "";
	Boolean failLogin = false;
	Button submit = null;
	ProgressBar bar = null;
	TextView loadingMessage = null;
	boolean isQueue = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Button cancel = (Button) findViewById(R.id.btn_login_cancel);
		loadingMessage = (TextView) findViewById(R.id.connectingToServer);
		bar = (ProgressBar) findViewById(R.id.progressBarConnectingToServer);
		loadingMessage.setVisibility(View.GONE);
		bar.setVisibility(View.GONE);
		submit = (Button) findViewById(R.id.btn_login_submit);
		cancel.setVisibility(View.GONE);

		if (getIntent().getExtras() != null) {

			Bundle bundle = getIntent().getExtras();
			if (bundle.getString("myView") != null) {
				previousView = bundle.getString("myView");
				System.out.println("previousView :" + previousView);
			}
			if (bundle.getString("message") != null
					&& !bundle.getString("message").equals("")) {
				message = bundle.getString("message");
				Toast.makeText(getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
		}

		TextView loadingMessage = (TextView) findViewById(R.id.connectingToServer);
		ProgressBar bar = (ProgressBar) findViewById(R.id.progressBarConnectingToServer);

		loadingMessage.setVisibility(View.INVISIBLE);
		bar.setVisibility(View.INVISIBLE);

	}

	public void onSubmitClick(View v) {

		submit.setEnabled(false);
		error = (TextView) findViewById(R.id.error);
		waiterId = (EditText) findViewById(R.id.txtwaiterId);
		accessCode = (EditText) findViewById(R.id.textAccessCode);

		loadingMessage = (TextView) findViewById(R.id.connectingToServer);
		ProgressBar bar = (ProgressBar) findViewById(R.id.progressBarConnectingToServer);

		loadingMessage.setVisibility(View.VISIBLE);
		bar.setVisibility(View.VISIBLE);

		TempSession.setUserID(waiterId.getText().toString());
		TempSession.setPassword(accessCode.getText().toString());

		if (waiterId.getText().toString().equals("")) {
			error.setText(getApplicationContext().getString(
					R.string.error_waiterID));
			error.setVisibility(View.VISIBLE);
			AppLog.info("Login", "onSubmitClick", "waiter login",
					"empty waiter id", "waiter id = "
							+ waiterId.getText().toString(), "access code = "
							+ accessCode.getText().toString());
			submit.setEnabled(true);
			loginFailed();
		}

		// If 'Access Code' field is not filled
		else if (accessCode.getText().toString().equals("")) {
			error.setText(getApplicationContext().getString(
					R.string.error_accessCode));
			error.setVisibility(View.VISIBLE);
			AppLog.info("Login", "onSubmitClick", "waiter login",
					"empty access code", "waiter id = "
							+ waiterId.getText().toString(), "access code = "
							+ accessCode.getText().toString());
			submit.setEnabled(true);
			loginFailed();
		}

		// If all the fields are filled correctly
		else {
			VerifyLogin rLogin = new VerifyLogin(waiterId.getText().toString(),
					accessCode.getText().toString());
			rLogin.execute();
			// loginDB();
		}

	}

	public void loginSuccess() {
		submit.setEnabled(true);
		Session.setUserID(waiterId.getText().toString());
		Session.setPassword(accessCode.getText().toString());
		bar.setVisibility(View.INVISIBLE);
		Intent i = null;
		if (isQueue) {
			forQueues();
			i = new Intent(Login.this, SelectLanguage.class);
		} else {
			i = new Intent(Login.this, SetTable.class);
		}
		startActivity(i);
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
		ConnectionManager manager = new ConnectionManager();
		manager.setClient(new DefaultHttpClient());

		finish();
		message = StaticData.context.getResources().getString(
				R.string.logged_into_device);
		AppLog.info("Login", "loginSuccess", "waiter login",
				"successfull login", "waiter id = "
						+ waiterId.getText().toString());

	}

	public void forQueues() {
		StaticData.tableNo = "N/A";
	}

	public void loginFailed() {
		submit.setEnabled(true);

		message = StaticData.context.getResources().getString(
				R.string.logging_to_device_failed);
		AppLog.info("Login", "loginFailed", "waiter login", "login failed",
				"waiter id = " + waiterId.getText().toString());
		bar.setVisibility(View.INVISIBLE);
	}

	public void loginDB() {
		RoleDataAccess roleData = new RoleDataAccess(getApplicationContext());
		if (roleData.getPassword(waiterId.getText().toString()).trim()
				.equals(accessCode.getText().toString().trim())) {
			Session.setUserID(waiterId.getText().toString());
			Session.setPassword(accessCode.getText().toString());
			loadingMessage.setText(getString(R.string.logged_into_device));
			loginSuccess();
		} else {
			loadingMessage
					.setText(getString(R.string.logging_to_device_failed));
			loginFailed();
		}
	}

	@Override
	public void onBackPressed() {

	}

	public class VerifyLogin extends AutomaticLogin {

		public VerifyLogin(String userName, String password) {
			super(userName, password);
		}

		@Override
		protected void onPostExecute(Void result) {
			if (!login) {
				loadingMessage.setText(getString(R.string.offlineLogin));
				loginDB();
				return;
			}
			if (!deviceExist) {

				loadingMessage.setText(getString(R.string.device_not_found)
						+ " : " + StaticData.deviceid);
				loginFailed();

			} else {
				loadingMessage.setText(getString(R.string.offlineLogin));
				loginSuccess();

			}
		}
	}
}
