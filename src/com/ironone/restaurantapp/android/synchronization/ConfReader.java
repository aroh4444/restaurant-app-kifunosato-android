/*
 * ConfReader.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.synchronization;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import com.ironone.restaurantapp.android.logic.Menu;
import android.content.Context;
import android.content.res.Resources;

/**
 * {Insert class description here}
 *
 * @Created on Oct 8, 2012 : 4:22:43 PM
 * @author malakag
 */

public class ConfReader {

	Context context;
	
	public ConfReader(Context context) {
		super();
		this.context = context;
	}
	
	public String getServerUrl(){
		Menu menu= new Menu();
		Resources res = context.getResources();
		try {
			InputStream is = res.getAssets().open("conf.xml");
			menu.setXmlDocument(is);
		} catch (SAXException e) {
			
		} catch (ParserConfigurationException e) {

		} catch (IOException e) {

		}
		return menu.getString("configuration/server-url").trim();
	}
}

