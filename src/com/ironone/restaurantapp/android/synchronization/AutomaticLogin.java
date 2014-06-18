/*
 * AutomaticLogin.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.synchronization;

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
import android.os.AsyncTask;
import com.ironone.restaurantapp.android.logic.StaticData;

/**
 * {Insert class description here}
 * 
 * @Created on Oct 11, 2012 : 11:17:17 AM
 * @author malakag
 */

public abstract class AutomaticLogin extends AsyncTask<Void, Void, Void> {

	DefaultHttpClient httpclient = new DefaultHttpClient();
	StringBuffer responseString = new StringBuffer();
	StringBuffer deviceResponseString = new StringBuffer();
	public boolean deviceExist = false;
	public boolean login = false;
	ConfReader conf = new ConfReader(StaticData.context);
	private String userName="";
	private String password="";
	
	public AutomaticLogin(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	

	@Override
	protected Void doInBackground(Void... params) {

		try {

			URL url = new URL(conf.getServerUrl()
					+ "login?action=login&format=json");
			HttpPost httppost = new HttpPost(url.toString());

			/** Add data **/
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("txtUserName", userName));
			nameValuePairs.add(new BasicNameValuePair("txtPassword", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			/** Execute HTTP Post Request **/
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity httpEntity = response.getEntity();
			InputStream is = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));

			String line;
			while ((line = reader.readLine()) != null) {
				responseString.append(line);
			}

			JSONObject jObject = new JSONObject();

			try {
				jObject = new JSONObject(responseString.toString());
			} catch (JSONException e) {

			}
			try {
				StaticData.loginType = jObject.get("type").toString()
						.trim();
				if(!StaticData.loginType.equals("data")) {
					return null;
				}
				login = true;
				
			} catch (JSONException e) {
				return null;
			}
			try {
				StaticData.loginMessage = jObject.get("description")
						.toString().trim();
			} catch (JSONException e) {

			}
			try {
				StaticData.errorCode = jObject.get("code").toString()
						.trim();
			} catch (JSONException e) {

			}
			try {
				StaticData.restaurantId = Integer.parseInt(jObject
						.get("restaurant").toString().trim());
			} catch (JSONException e) {

			}

			if (StaticData.loginType.equalsIgnoreCase("data")) {

				URL deviceVerifyUrl = new URL(conf.getServerUrl()
						+ "login?action=verifyDevice&format=json");
				HttpPost deviceHttpPost = new HttpPost(
						deviceVerifyUrl.toString());

				/** Add data **/
				List<NameValuePair> deviceParams = new ArrayList<NameValuePair>(
						2);
				deviceParams.add(new BasicNameValuePair("restaurantId",
						StaticData.restaurantId.toString()));
				deviceParams.add(new BasicNameValuePair("deviceID",
						StaticData.deviceid));
				deviceHttpPost
						.setEntity(new UrlEncodedFormEntity(deviceParams));

				/** Execute HTTP Post Request **/
				HttpResponse deviceResponse = httpclient
						.execute(deviceHttpPost);

				HttpEntity deviceHttpEntity = deviceResponse.getEntity();
				is = deviceHttpEntity.getContent();
				reader = new BufferedReader(new InputStreamReader(is));

				line = "";
				while ((line = reader.readLine()) != null) {
					deviceResponseString.append(line);
				}
				System.out.println(deviceResponseString);
				try {
					jObject = new JSONObject(
							deviceResponseString.toString());
				} catch (JSONException e) {

				}
				try {
					StaticData.loginType = jObject.get("type").toString()
							.trim();
				} catch (JSONException e) {

				}
				try {
					StaticData.loginMessage = jObject.get("description")
							.toString().trim();
				} catch (JSONException e) {

				}
				try {
					StaticData.errorCode = jObject.get("code").toString()
							.trim();
				} catch (JSONException e) {

				}

				try {
					deviceExist = jObject.getBoolean("isExists");
				} catch (JSONException e) {

				}

			}

			if (deviceExist) {

				ConnectionManager manager = new ConnectionManager();
				manager.setClient(httpclient);
			}

		} catch (ClientProtocolException e) {

		} catch (IOException e) {

		}

		return null;
	}
	
}
