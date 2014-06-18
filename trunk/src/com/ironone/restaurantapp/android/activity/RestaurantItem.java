package com.ironone.restaurantapp.android.activity;


import java.util.ArrayList;
import java.util.List;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.logic.Image;
import com.ironone.restaurantapp.android.logic.Item;
import com.ironone.restaurantapp.android.logic.Menu;
import com.ironone.restaurantapp.android.logic.Session;
import com.ironone.restaurantapp.android.logic.StaticData;
import com.ironone.restaurantapp.android.titlebar.TitleBar;
import com.ironone.restaurantapp.android.util.FormatUtil;

/**
 * This class is to load menu items and respective food items dynamically
 * 
 * @Created on June 28, 2012 : 4:25:10 PM
 * @author DINESHPATHIRANA
 */

public class RestaurantItem extends TitleBar {

	private ExpandableMenuAdapter mAdapter;
	private ExpandableListView mlist;
	private ArrayList<Menu> menuList_full = (ArrayList<Menu>) StaticData.fullmenu;
	public final static String EXTRA_MESSAGE = "test";
	public ArrayList<Menu> menuList;
	public ArrayList<Item> itemList;
	int level = 0;
	int selectedRadio = 0;
	boolean isLeaf = false;
	private int menuPosition;
	TableLayout table;
	TableLayout sizes_table;
	TextView category;

	/* Spinner spinner; */
	Button orderButton;
	PopupWindow m_pw;
	Item item = null;
	View layout;
	View layout2;
	RadioGroup radGroup;
	RadioButton rad;
	TableRow tr;
	TableRow sizeTr;
	String TAG = "com.ironone.restaurantapp.android.activity/RestaurantItem.java";
	Bitmap bm_item = null;

	
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemsmenuview);
		category = (TextView) findViewById(R.id.txt_category);

		/** set title bar */
		LinearLayout header = (LinearLayout) findViewById(R.id.layout_heading_items);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.titlebar, null);

		header.addView(vg);
		setHeaderTitle(getApplicationContext().getString(R.string.select_item));
		setMyOrderItemCount();
		setSubmittedOrderItemCount();
		setTitlebarLanguage();

		ImageButton set = (ImageButton) vg.findViewById(R.id.SettingsButton);
		set.setId(R.layout.itemsmenuview);

		menuPosition = getIntent().getIntExtra("MenuPosition", 0);

		

		mAdapter = new ExpandableMenuAdapter(this);

		mlist = (ExpandableListView) findViewById(R.id.ex_list_menu);
		mlist.setAdapter(mAdapter);
		/* mlist.setGroupIndicator(null); */

		itemList = (ArrayList<Item>) StaticData.items;
		if(itemList.size()>0) {
			category.setText(Menu.getParentMenuFromId(StaticData.fullmenu, itemList.get(0).getId()).getName());
		}
		
		if (StaticData.clickedIndex >= 0) {
			mlist.expandGroup(StaticData.clickedIndex - 1);
			StaticData.clickedIndex = -1;
		}
		loadItemList();

		/**
		 * Called when expanding a group closes existing groups if it is not the
		 * group about to expand
		 ***/
		mlist.setOnGroupExpandListener(new OnGroupExpandListener() {

			public void onGroupExpand(int groupPosition) {
				int len = mAdapter.getGroupCount();
				for (int i = 0; i < len; i++) {
					if (i != groupPosition) {
						mlist.collapseGroup(i);
					}
				}
			}
		});

		/** Called for expandable list group click **/

		mlist.setOnGroupClickListener(new OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				try {
					v.setSelected(true);

					ArrayList<Menu> childMenu = (ArrayList<Menu>) menuList_full;
					ArrayList<Item> childItem = (ArrayList<Item>) childMenu
							.get(groupPosition).getItems();
					if ((ArrayList<Item>) Menu.getChildItems(childMenu,
							childMenu.get(groupPosition).getId()) != null) {
						itemList = childItem;
						table.removeAllViews();
						category.setText(childMenu.get(groupPosition).getName()
								.toString());
						
						loadItemList();
					}
				} catch (Exception e) {
					Log.e(TAG + "/mlist.setOnGroupClickListener",
							e.getMessage());
				}

				return false;
			}

		});

		/** Called for expandable list child click **/

		mlist.setOnChildClickListener(new OnChildClickListener() {

			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				try {
					v.setSelected(true);

					ArrayList<Menu> childMenu = (ArrayList<Menu>) menuList_full
							.get(groupPosition).getMenus();
					ArrayList<Item> childItem = (ArrayList<Item>) childMenu
							.get(childPosition).getItems();
					if ((ArrayList<Item>) Menu.getChildItems(childMenu,
							childMenu.get(childPosition).getId()) != null) {

						itemList = childItem;
						table.removeAllViews();
						category.setText(childMenu.get(childPosition).getName()
								.toString());
						loadItemList();
					}
				} catch (Exception e) {
					Log.e(TAG + "/mlist.setOnGroupClickListener",
							e.getMessage());
				}

				return false;
			}
		});
		
		
		TextView txtTableValue = (TextView)findViewById(R.id.txtTableNoValue_Title);
		txtTableValue.setText(": " + Session.getTable());
	}

	public Item getItemById(List<Item> items, int id) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getId() == id) {
				return items.get(i);
			}
		}
		return null;
	}

	/**
	 * This inner class is to define expandable list adapter
	 * 
	 */

	public void loadItemList() {
		for (int i = 0; i < itemList.size(); i++) {

			LayoutInflater inflater = LayoutInflater.from(getBaseContext());
			View layout = new View(this);
			layout = inflater.inflate(R.layout.dynamicfooditems, null);

			TextView textViewFoodItemName = (TextView) layout
					.findViewById(R.id.textViewFoodItemName);
			TextView textViewFoodItemPrice = (TextView) layout
					.findViewById(R.id.textViewNoSizePrice);
			TextView textViewFoodItemDescrip = (TextView) layout
					.findViewById(R.id.textViewDescrip);

			ImageView foodImg = (ImageView) layout.findViewById(R.id.img_food);
			bm_item = Image.decodeSampledBitmapFromResource(itemList.get(i)
					.getImage().getLocation(), 200, 200);

			Bitmap finalBitmap = null;

			try {
				finalBitmap = Bitmap.createBitmap(bm_item, 24, 44, 120, 72);
				foodImg.setImageBitmap(finalBitmap);
			} catch (Exception e) {
				Log.d(TAG, itemList.get(i).getName() + " image not found");
			}

			textViewFoodItemName.setText(itemList.get(i).getName());
			textViewFoodItemDescrip.setText(itemList.get(i).getDescription());

			item = itemList.get(i);

			// Set sizes if exist...

			if (itemList.get(i).getSizes().size() > 0) {

				for (int j = 0; j < itemList.get(i).getSizes().size(); j++) {

					
					
					
					View layout2 = new View(this);
					layout2 = inflater.inflate(R.layout.dynamicsizes, null);

					sizes_table = (TableLayout) layout
							.findViewById(R.id.subsizestable);
					sizeTr = new TableRow(this);

					TextView sizename = (TextView) layout2
							.findViewById(R.id.txtsizename);
					TextView sizeprice = (TextView) layout2
							.findViewById(R.id.txtsizeprice);
					
					
					sizename.setText(itemList.get(i).getSizes().get(j)
							.getName());
					
					String ss = StaticData.currency
							.getSymbol()
							+ " "
							+ FormatUtil.getNumberFormatted(itemList.get(i).getSizes().get(j)
							.getPrice());
					
					sizeprice.setText(StaticData.currency
							.getSymbol()
							+ " "
							+ FormatUtil.getNumberFormatted(itemList.get(i).getSizes().get(j)
							.getPrice()));

					TableLayout.LayoutParams l_para = new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					l_para.setMargins(25, 0, 0, 1);

					sizeTr.addView(layout2);
					sizes_table.addView(sizeTr, l_para);
					if(j == 2 && itemList.get(i).getSizes().size() > 4) {
						sizename.setText(getString(R.string.more_sizes));
						sizeprice.setText("");
						break;
					}
				}

			} else {
				textViewFoodItemPrice.setText(StaticData.currency
						.getSymbol()
						+ " "
						+ FormatUtil.getNumberFormatted(itemList.get(i).getPrice()));

			}

			table = (TableLayout) findViewById(R.id.item_table);
			tr = new TableRow(this);

			tr.setId(itemList.get(i).getId());
			tr.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					TableRow temp = (TableRow) findViewById(v.getId());
					StaticData.item = getItemById(itemList, v.getId());
					temp.setBackgroundColor(Color.argb(150, 155, 155, 155));
					try {
						StaticData.items = itemList;
						Intent i = new Intent(RestaurantItem.this,
								Description.class);
						startActivity(i);
						overridePendingTransition(R.anim.right_slide_in,
								R.anim.right_slide_out);
						finish();

					} catch (ActivityNotFoundException e) {
						Log.e(TAG, e.getMessage());
					}

				}
			});

			TableLayout.LayoutParams lp = new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 0, 2);
			tr.addView(layout);
			table.addView(tr, lp);

		}
	}

	public class ExpandableMenuAdapter extends BaseExpandableListAdapter {

		private Context context;

		public ExpandableMenuAdapter(Context context) {
			this.context = context;
		}

		public Object getChild(int groupPosition, int childPosition) {
			return menuList_full.get(groupPosition).getMenus()
					.get(childPosition).getName();
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return menuList_full.get(groupPosition).getMenus().size();
		}

		public TextView getGenericView() {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 64);

			TextView textView = new TextView(RestaurantItem.this);
			textView.setLayoutParams(lp);
			// Center the text vertically
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			// Set the text starting position
			textView.setPadding(66, 0, 0, 0);
			return textView;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater
						.inflate(R.layout.child_layout, null);
			}
			TextView textView = (TextView) convertView
					.findViewById(R.id.text_child);
			textView.setText(getChild(groupPosition, childPosition).toString());
			if (menuPosition == menuList_full.get(groupPosition).getMenus()
					.get(childPosition).getId()) {
				convertView
						.setBackgroundResource(R.drawable.ex_menu_list_default);
			}
			return convertView;
		}

		public Object getGroup(int groupPosition) {
			return menuList_full.get(groupPosition).getName();
		}

		public int getGroupCount() {
			return menuList_full.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater
						.inflate(R.layout.group_layout, null);
			}
			TextView textView = (TextView) convertView
					.findViewById(R.id.text_group);
			textView.setText(getGroup(groupPosition).toString());

			return convertView;

		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

	}

	@Override
	public void onBackPressed() {
		try {
			Intent i = new Intent(RestaurantItem.this, MainMenu.class);
			startActivity(i);
			overridePendingTransition(R.anim.right_slide_in,
					R.anim.right_slide_out);
			finish();

		} catch (ActivityNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
	}
}
