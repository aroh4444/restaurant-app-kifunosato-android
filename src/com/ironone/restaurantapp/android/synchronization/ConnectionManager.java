/*
 * ConnectionManager.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.synchronization;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import com.ironone.restaurantapp.android.logic.StaticData;

/**
 * This class manages
 * 
 * @Created on Oct 8, 2012 : 4:18:20 PM
 * @author malakag
 */

public class ConnectionManager {

	
	public ConnectionManager() {
		
	}

	public DefaultHttpClient getClient() throws ClientProtocolException,
			IOException {
		if (StaticData.httpClient == null) {
	
			if(StaticData.loginType == "data"){
				return StaticData.httpClient;
			}
			else{
				return new DefaultHttpClient();
			}
			
		} else {
			return StaticData.httpClient;
		}
	}
	
	public void setClient(DefaultHttpClient client){
		StaticData.httpClient = client;
	}
}