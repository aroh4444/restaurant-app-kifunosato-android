/*
 * RestaurentMenu.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import android.content.Context;
import android.util.Log;

/**
 * {Insert class description here}
 * 
 * @Created on Jun 27, 2012 : 10:14:23 AM
 * @author malakag
 */

public class RestaurantMenu {

	public RestaurantMenu(Context context) {
		this.context = context;
	}

	public RestaurantMenu(Document doc) {
		xmlDocument = doc;
		xPath = XPathFactory.newInstance().newXPath();
	}

	public RestaurantMenu() {
		xPath = XPathFactory.newInstance().newXPath();
	}

	protected Document xmlDocument;
	protected XPath xPath;

	public static String TAG = "com.ironone.restaurantapp.android.logic/RestaurantMenu.java";

	private String name = "";
	private int id = 0;
	private boolean isEnable = true;;
	private Image image = new Image();
	private Context context;


	public String getName() {
		LanguageTranslator translator = new LanguageTranslator(this.context);
		return translator.getPreparedString("id_"+this.id+"_name");
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	

	/**
	 * get a common menu object read from XML
	 * 
	 * @param path
	 * @return List<Menu>
	 */
	public List<Menu> getAll(String path) {
		List<Menu> results = new ArrayList<Menu>();
		if (path != null && !path.equalsIgnoreCase("")) {
			{
				try {
					XPathExpression xPathExpression = getXPath().compile(path);
					NodeList nodes = (NodeList) xPathExpression.evaluate(
							getXmlDocument(), XPathConstants.NODESET);

					for (int i = 0; i < nodes.getLength(); i++) {
						Document newXmlDocument = DocumentBuilderFactory
								.newInstance().newDocumentBuilder()
								.newDocument();
						Node node = nodes.item(i);
						// System.out.println(node.getNodeName());
						Node copyNode = newXmlDocument.importNode(node, true);
						newXmlDocument.appendChild(copyNode);
						Menu menu = new Menu(newXmlDocument);
						menu.setName(node.getNodeName());
						results.add(menu);
						// printXmlDocument(newXmlDocument);
					}
					return results;
				} catch (XPathExpressionException e) {
					Log.e(TAG, e.toString());
				} catch (DOMException e) {
					Log.e(TAG, e.toString());
				} catch (ParserConfigurationException e) {
					Log.e(TAG, e.toString());
				}
			}
		}
		return null;
	}

	/**
	 * return a prepared menu object list with initializing all atributes
	 * 
	 * @param path
	 * @return List<Menu>
	 */
	public List<Menu> getSubMenu(String path) {
		List<Menu> results = new ArrayList<Menu>();
		if (path != null && !path.equalsIgnoreCase("")) {
			{
				try {
					XPathExpression xPathExpression = getXPath().compile(path);
					NodeList nodes = (NodeList) xPathExpression.evaluate(
							getXmlDocument(), XPathConstants.NODESET);

					for (int i = 0; i < nodes.getLength(); i++) {
						Document newXmlDocument = DocumentBuilderFactory
								.newInstance().newDocumentBuilder()
								.newDocument();
						Node node = nodes.item(i);
						// System.out.println(node.getNodeName());
						if ("submenu".equals(node.getNodeName())) {
							Node copyNode = newXmlDocument.importNode(node,
									true);
							newXmlDocument.appendChild(copyNode);
							Menu menu = new Menu(newXmlDocument);
							results.add(menu);

						}
						// printXmlDocument(newXmlDocument);
					}
					List<Menu> ret = new ArrayList<Menu>();
					Menu temp = new Menu(this.context);
					ret = temp.prepareMenuObjects(results);
					return ret;
				} catch (XPathExpressionException e) {
					Log.e(TAG, e.toString());
				} catch (DOMException e) {
					Log.e(TAG, e.toString());
				} catch (ParserConfigurationException e) {
					Log.e(TAG, e.toString());
				}
				
			}
		}
		return null;
	}

	/**
	 * return a prepared Currency object with initializing all atributes
	 * 
	 * @param path
	 * @return Currency
	 */

	public Currency getCurrency(String path) {
		List<Currency> results = new ArrayList<Currency>();
		if (path != null && !path.equalsIgnoreCase("")) {
			{
				try {
					XPathExpression xPathExpression = getXPath().compile(path);
					NodeList nodes = (NodeList) xPathExpression.evaluate(
							getXmlDocument(), XPathConstants.NODESET);

					for (int i = 0; i < nodes.getLength(); i++) {
						Document newXmlDocument = DocumentBuilderFactory
								.newInstance().newDocumentBuilder()
								.newDocument();
						Node node = nodes.item(i);
						// System.out.println(node.getNodeName());
						if ("currency".equals(node.getNodeName())) {
							Node copyNode = newXmlDocument.importNode(node,
									true);
							newXmlDocument.appendChild(copyNode);
							Currency currency = new Currency(newXmlDocument);
							results.add(currency);

						}
						// printXmlDocument(newXmlDocument);
					}
					List<Currency> ret = new ArrayList<Currency>();
					ret = Currency.prepareCurrencyObjects(results);
					try {
						return ret.get(0);
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
					}
				} catch (XPathExpressionException e) {
					Log.e(TAG, e.getMessage());
				} catch (DOMException e) {
					Log.e(TAG, e.getMessage());
				} catch (ParserConfigurationException e) {
					Log.e(TAG, e.getMessage());
				} catch (ArrayIndexOutOfBoundsException e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}
		return null;
	}

	/**
	 * return a prepared Item object list with initializing all atributes
	 * 
	 * @param path
	 * @return List<Item>
	 */
	public List<Item> getItem(String path) {
		List<Item> results = new ArrayList<Item>();
		if (path != null && !path.equalsIgnoreCase("")) {
			{
				try {
					XPathExpression xPathExpression = getXPath().compile(path);
					NodeList nodes = (NodeList) xPathExpression.evaluate(
							getXmlDocument(), XPathConstants.NODESET);

					for (int i = 0; i < nodes.getLength(); i++) {
						Document newXmlDocument = DocumentBuilderFactory
								.newInstance().newDocumentBuilder()
								.newDocument();
						Node node = nodes.item(i);
						// System.out.println(node.getNodeName());
						if ("item".equals(node.getNodeName())) {
							Node copyNode = newXmlDocument.importNode(node,
									true);
							newXmlDocument.appendChild(copyNode);
							Item item = new Item(newXmlDocument);
							results.add(item);
						}
						// printXmlDocument(newXmlDocument);
					}
					Item temp = new Item(this.context);
					List<Item> ret = temp.prepareItemObjects(results);
					return ret;
				} catch (XPathExpressionException e) {
					Log.e(TAG, e.toString());
				} catch (DOMException e) {
					Log.e(TAG, e.toString());
				} catch (ParserConfigurationException e) {
					Log.e(TAG, e.toString());
				}
			}
		}
		return null;
	}

	/**
	 * return a prepared Language object list with initializing all atributes
	 * 
	 * @param path
	 * @return List<Language>
	 */
	public List<Language> getLanguages(String path) {
		List<Language> results = new ArrayList<Language>();
		if (path != null && !path.equalsIgnoreCase("")) {
			{
				try {
					XPathExpression xPathExpression = getXPath().compile(path);
					NodeList nodes = (NodeList) xPathExpression.evaluate(
							getXmlDocument(), XPathConstants.NODESET);

					for (int i = 0; i < nodes.getLength(); i++) {
						Document newXmlDocument = DocumentBuilderFactory
								.newInstance().newDocumentBuilder()
								.newDocument();
						Node node = nodes.item(i);
						// System.out.println(node.getNodeName());
						if ("language".equals(node.getNodeName())) {
							Node copyNode = newXmlDocument.importNode(node,
									true);
							newXmlDocument.appendChild(copyNode);
							Language language = new Language(newXmlDocument);
							results.add(language);

						}
						// printXmlDocument(newXmlDocument);
					}
					List<Language> ret = new ArrayList<Language>();
					ret = Language.prepareLanguageObjects(results);
					return ret;
				} catch (XPathExpressionException e) {
					Log.e(TAG, e.toString());
				} catch (DOMException e) {
					Log.e(TAG, e.toString());
				} catch (ParserConfigurationException e) {
					Log.e(TAG, e.toString());
				}
			}
		}
		return null;
	}

	/**
	 * Return a String by processing the xPath expression
	 * 
	 * @param path
	 *            xPath expression used to query the xml document
	 * @return text related to xml tag which is related to given xPath
	 *         expression
	 */
	public String getString(String path) {
		if (path != null && !path.equalsIgnoreCase("")) {

			try {
				XPathExpression xPathExpression = getXPath().compile(path);
				return (String) xPathExpression.evaluate(getXmlDocument(),
						XPathConstants.STRING);
			} catch (Exception ex) {

			}
		}
		return "";
	}

	/**
	 * Return a int value by processing the xPath expression
	 * 
	 * @param path
	 *            xPath expression used to query the xml document
	 * @return int value related to xml tag which is related to given xPath
	 *         expression
	 */
	public Integer getInt(String path) {
		if (path != null && !path.equalsIgnoreCase("")) {

			try {
				XPathExpression xPathExpression = getXPath().compile(path);
				Double d = (Double) xPathExpression.evaluate(getXmlDocument(),
						XPathConstants.NUMBER);
				return d.intValue();
			} catch (Exception ex) {

			}
		}
		return Integer.valueOf(-1);
	}

	/**
	 * Return a double value by processing the xPath expression
	 * 
	 * @param path
	 *            xPath expression used to query the xml document
	 * @return text related to xml tag which is related to given xPath
	 *         expression
	 */
	public Double getDouble(String path) {
		if (path != null && !path.equalsIgnoreCase("")) {

			try {
				XPathExpression xPathExpression = getXPath().compile(path);
				return (Double) xPathExpression.evaluate(getXmlDocument(),
						XPathConstants.NUMBER);
			} catch (Exception ex) {

			}
		}
		return Double.valueOf(-1);
	}

	/**
	 * Return a boolean value by processing the xPath expression
	 * 
	 * @param path
	 *            xPath expression used to query the xml document
	 * @return text related to xml tag which is related to given xPath
	 *         expression
	 */
	public boolean getBoolean(String path) {
		if (path != null && !path.equalsIgnoreCase("")) {

			try {
				XPathExpression xPathExpression = getXPath().compile(path);
				String s = (String) xPathExpression.evaluate(getXmlDocument(),
						XPathConstants.STRING);
				if ("true".equalsIgnoreCase(s)) {
					return true;
				}
			} catch (Exception ex) {

			}
		}
		return false;
	}

	public XPath getXPath() {
		return xPath;
	}

	public void setXPath(XPath path) {
		xPath = path;
	}

	public Document getXmlDocument() {
		return xmlDocument;
	}

	public void setXmlDocument(InputStream is) throws SAXException,
			ParserConfigurationException, IOException {

		xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(is);

	}

	/**
	 * Constructs a <code>String</code> with all attributes in name = value
	 * format.
	 * 
	 * @return a <code>String</code> representation of this object.
	 */
	public String toString() {
		final String TAB = "    ";

		String retValue = "";

		retValue = "RestaurantMenu ( " + super.toString() + TAB
				+ "xmlDocument = " + this.xmlDocument + TAB + "xPath = "
				+ this.xPath + TAB + "name = " + this.name + TAB + "id = "
				+ this.id + TAB + "isEnable = " + this.isEnable + TAB
				+ "image = " + this.image.toString() + TAB + " )";

		return retValue;
	}

}
