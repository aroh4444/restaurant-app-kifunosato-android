/*
 * Description.java
 *
 * Copyright(c) IronOne Technologies (Pvt) Ltd.  All Rights Reserved.
 * This software is the proprietary information of IronOne Technologies (Pvt) Ltd.
 *
 */

package com.ironone.restaurantapp.android.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.android.init.ResourceManger;
import com.ironone.restaurantapp.android.logic.AddOn;
import com.ironone.restaurantapp.android.logic.Item;
import com.ironone.restaurantapp.android.logic.MiniOrder;
import com.ironone.restaurantapp.android.logic.Session;
import com.ironone.restaurantapp.android.logic.StaticData;
import com.ironone.restaurantapp.android.logic.Size;
import com.ironone.restaurantapp.android.titlebar.TitleBar;
import com.ironone.restaurantapp.android.util.FormatUtil;

/**
 * This class is for showing the selected food description and choose options
 * 
 * @Created on Jul 26, 2012 : 9:22:39 AM
 * @author harshanak
 */

public class Description extends TitleBar {

	int selectedRadio = -1;
	RadioGroup radGroup;
	RadioButton rad;
	private File imgDir = ResourceManger.getImageDirectory();
	Item item = null;
	int selectedItem = -1;
	Bitmap bm_item = null;
	String TAG = "com.ironone.restaurantapp.android.activity/Description.java";

	// LinearLayout lnrSpecialInstructionsData=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.description);
		

		try {
			Bundle extras = getIntent().getExtras();
			selectedItem = extras.getInt("fromOrder");

		} catch (Exception e) {
		}

		LinearLayout header = (LinearLayout) findViewById(R.id.layout_heading_description);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.titlebar, null);

		header.addView(vg);
		setHeaderTitle(getApplicationContext().getString(R.string.mainmenu));
		setMyOrderItemCount();
		setSubmittedOrderItemCount();
		setTitlebarLanguage();

		ImageButton set = (ImageButton) vg.findViewById(R.id.SettingsButton);
		set.setId(R.layout.description);

		item = StaticData.item.clone();

		TextView foodName = (TextView) findViewById(R.id.food_name);
		foodName.setText(item.getName());

		ImageView img = (ImageView) findViewById(R.id.food_image);

		try {
			InputStream is_item = new FileInputStream(new File(imgDir, item
					.getImage().getLocation()));
			bm_item = BitmapFactory.decodeStream(is_item);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (bm_item != null) {
			img.setImageBitmap(bm_item);

		}
		img.setScaleType(ScaleType.FIT_XY);
		
		if (selectedItem == -1) { 
			item.setSelectedAddOnes(new ArrayList<AddOn>());
		}

		// if the previous view is Myorder view
		if (selectedItem != -1) {

			Button order = (Button) findViewById(R.id.btn_order);
			order.setText(R.string.change_order);

			TextView amount = (TextView) findViewById(R.id.amount);
			amount.setText(StaticData.currentOrder.getMiniOrder()
					.get(selectedItem).getNoOfItems().toString());

			EditText specialInst = (EditText) findViewById(R.id.txtSpecialInstruction_description);
			specialInst.setText(StaticData.currentOrder.getMiniOrder()
					.get(selectedItem).getSpecialInstruction());

			Button remove = (Button) findViewById(R.id.btn_remove);
			remove.setVisibility(View.VISIBLE);
			remove.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					Intent i = new Intent(Description.this, MyOrder.class);
					i.putExtra("remove", selectedItem);
					startActivity(i);
					overridePendingTransition(R.anim.right_slide_in,
							R.anim.right_slide_out);
					finish();

				}
			});
		}

		ArrayList<String> sizes = new ArrayList<String>();

		if (item.getSizes().size() > 0) {
			for (int i = 0; i < item.getSizes().size(); i++) {
				sizes.add(item.getSizes().get(i).getName() + " - "
						+ StaticData.currency.getSymbol() + " "
						+ FormatUtil.getNumberFormatted(item.getSizes().get(i).getPrice()));
			}
			selectedRadio = 0;
			// load portions to the View
			radGroup = (RadioGroup) findViewById(R.id.options_group);
			for (int i = 0; i < sizes.size(); i++) {
				rad = new RadioButton(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, 40);
				lp.setMargins(0, 0, 0, 10);

				rad.setId(i);
				rad.setText(sizes.get(i).toString());
				rad.setPadding(50, 0, 0, 0);
				radGroup.addView(rad, lp);

				// adding a line between option buttons
				if (i != sizes.size() - 1) {

					View line = new View(this);
					line.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, 1));
					line.setBackgroundColor(Color.rgb(121, 159, 84));
					radGroup.addView(line);
				}

				rad.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						v.setBackgroundDrawable(getResources().getDrawable(
								R.drawable.radio_button_state));
						onRadioClick(v);

					}
				});

			}
			// if the previous view is Myorder view
			if (selectedItem != -1) {

				// select the correct option button
				for (int i = 0; i < item.getSizes().size(); i++) {
					if (item.getSelectedSize().getName()
							.equalsIgnoreCase(item.getSizes().get(i).getName()))
						selectedRadio = i;
				}

				radGroup.check(selectedRadio);

				RadioButton r = (RadioButton) radGroup
						.findViewById(selectedRadio);
				r.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.radio_button_state));

			} else {
				radGroup.check(0);
				RadioButton r = (RadioButton) radGroup.findViewById(0);
				r.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.radio_button_state));
				setPrice(0);
			}
			setPrice(selectedRadio);
		} else {
			setPrice();
			LinearLayout radiolaout = (LinearLayout) findViewById(R.id.radiolayout);
			radiolaout.setVisibility(View.GONE);
			TextView price = (TextView) findViewById(R.id.food_item_price);
			String priceText = price.getText().toString();
			price.setText(priceText + " " + StaticData.currency.getSymbol() + " "
					+ FormatUtil.getNumberFormatted(item.getPrice()).toString());
			price.setVisibility(View.VISIBLE);
		}

		if (item.getAddOnes().size() > 0) {

			((LinearLayout) findViewById(R.id.lnAddOn))
					.setVisibility(View.VISIBLE);

			LinearLayout addOnLayout = (LinearLayout) findViewById(R.id.checkBoxlayout);

			for (int i = 0; i < item.getAddOnes().size(); i++) {

				AddOn addOn = item.getAddOnes().get(i);
				CheckBox chkAddOn = new CheckBox(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, 40);
				lp.setMargins(0, 5, 0, 5);

				chkAddOn.setId(addOn.getId());
				chkAddOn.setTag(addOn.getId());
				chkAddOn.setText(addOn.getName().toString() + " - "
						+ StaticData.currency.getSymbol() + " "
						+ FormatUtil.getNumberFormatted(addOn.getPrice()));
				chkAddOn.setPadding(50, 0, 0, 0);
				// if the previous view is Myorder view
				if (selectedItem != -1) {
					if (item.isSelectedAddOn(addOn.getId())) {
						chkAddOn.setChecked(true);
					}
				}
				addOnLayout.addView(chkAddOn, 2 * i, lp);

				// adding a line between Add On buttons
				if (i != item.getAddOnes().size() - 1) {

					View line = new View(this);
					line.setLayoutParams(new LayoutParams(
							LayoutParams.FILL_PARENT, 1));
					line.setBackgroundColor(Color.rgb(121, 159, 84));
					addOnLayout.addView(line, 2 * i + 1);
				}
				chkAddOn.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {

						onAddOnClick(v);

					}
				});
			}

		}

		TextView calories = (TextView) findViewById(R.id.energy_value);
		calories.setText(getApplicationContext().getString(R.string.energy)
				+ " " + item.getCalories().toString() + " "
				+ getApplicationContext().getString(R.string.cal));

		TextView description = (TextView) findViewById(R.id.descript_value);
		description.setText(item.getDescription());

		TextView cookingInst = (TextView) findViewById(R.id.cooking_value);
		cookingInst.setText(item.getCookingInst());

		TextView txtTableValue = (TextView) findViewById(R.id.txtTableNoValue_Title);
		txtTableValue.setText(": " + Session.getTable());

		// lnrSpecialInstructionsData= (LinearLayout)
		// findViewById(R.id.lnrLayoutSpecialInstructionData_desription);
		//
		// View layout1 = inflater.inflate(R.layout.special_instructions, null);
		//
		//
		// TableLayout layout =
		// (TableLayout)layout1.findViewById(R.id.tblSpecialInstructionButtons_special_instructions);
		// layout.setLayoutParams( new TableLayout.LayoutParams() );
		//
		// layout.setPadding(1,1,1,1);
		//
		// for (int f=0; f<=1; f++) {
		// TableRow tr = new TableRow(this);
		// for (int c=0; c<=3; c++) {
		//
		// View specialButton = inflater.inflate(R.layout.dynamicbutton, null);
		//
		// Button b = (Button) specialButton.findViewById(R.id.btnDynamic);
		//
		//
		//
		// tr.addView(specialButton, 100, 50);
		// } // for
		// layout.addView(tr);
		// }
		//
		// //lnrSpecialInstructionsData.addView(layout1);
		// lnrSpecialInstructionsData.setVisibility(View.INVISIBLE);
	}

	public void onIncreaseClick(View v) {
		TextView amount = (TextView) findViewById(R.id.amount);
		int number = Integer.parseInt((String) amount.getText());
		number = number + 1;
		amount.setText("" + number);
		if (item.getSizes().size() > 0)
			setPrice(selectedRadio);
		else
			setPrice();
	}

	public void onDecreaseClick(View v) {
		TextView amount = (TextView) findViewById(R.id.amount);
		int number = Integer.parseInt((String) amount.getText());
		if (number != 1) {
			number = number - 1;
			amount.setText("" + number);

		}
		if (item.getSizes().size() > 0)
			setPrice(selectedRadio);
		else
			setPrice();
	}

	public void onOrderClick(View v) {
		TextView viewAmount = (TextView) findViewById(R.id.amount);
		int amount = Integer.parseInt((String) viewAmount.getText());
		Item fooditem = item;
		if (selectedRadio != -1) {
			Size selectedSize = fooditem.getSizes().get(selectedRadio);
			fooditem.setSelectedSize(selectedSize);
		}
		//item.setAddOnes(null);

		EditText specialInst = (EditText) findViewById(R.id.txtSpecialInstruction_description);

		MiniOrder miniOrder = new MiniOrder(fooditem, amount, specialInst
				.getText().toString());
		// TableDataAccess tableData = new
		// TableDataAccess(getApplicationContext());
		// // String tableNo = tableData.getTableNo();
		// //
		// // StaticData.tableNo = tableNo;
		selectedRadio = 0;
		if (selectedItem != -1) {
			StaticData.currentOrder.getMiniOrder().set(selectedItem, miniOrder);
			try {
				startActivity(new Intent(Description.this, MyOrder.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();
			} catch (Exception e) {
				Log.e("", e.getMessage());
			}
		} else {
			StaticData.currentOrder.getMiniOrder().add(miniOrder);
			setMyOrderItemCount();
			try {
				startActivity(new Intent(Description.this, RestaurantItem.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();
			} catch (Exception e) {
				Log.e("", e.getMessage());
			}

		}
		finish();
	}

	public void onRadioClick(View v) {
		selectedRadio = radGroup.getCheckedRadioButtonId();
		setPrice(selectedRadio);
	}

	public void onCancelClick(View v) {
		onBackPressed();
	}

	public void setPrice(int selectedSize) {
		TextView amount = (TextView) findViewById(R.id.amount);
		int number = Integer.parseInt((String) amount.getText());
		item.setSelectedSize(item.getSizes().get(selectedSize));
		Double price = number * (item.getSizes().get(selectedSize).getPrice()
				+ item.getSelectedAddOnsCost());
		TextView priceText = (TextView) findViewById(R.id.price_value);
		priceText.setText(getApplicationContext().getString(R.string.price)
				+ " " + StaticData.currency.getSymbol() + " "
				+ FormatUtil.getNumberFormatted(price));
	}

	public void setPrice() {
		TextView amount = (TextView) findViewById(R.id.amount);
		int number = Integer.parseInt((String) amount.getText());
		Double price = Double.valueOf(0);
		if (item.getSizes().size() > 0) {
			price = number
					* (item.getSelectedSize().getPrice() + item.getSelectedAddOnsCost());
		} else {
			price = number * (item.getPrice() + item.getSelectedAddOnsCost());
		}
		TextView priceText = (TextView) findViewById(R.id.price_value);
		priceText.setText(getApplicationContext().getString(R.string.price)
				+ " " + StaticData.currency.getSymbol() + " "
				+ FormatUtil.getNumberFormatted(price));
	}

	public void onAddOnClick(View v) {
		// Is the view now checked?
		boolean checked = ((CheckBox) v).isChecked();
		AddOn addOn = item.getAddOnFromId(Integer.parseInt((v.getTag().toString())));
		if (addOn == null) {
			return;
		}
		if (checked) {
			item.addSelectedAddOnes(addOn);
		} else {
			item.removeSelectedAddOnes(addOn);
		}
		setPrice();
	}

	

	@Override
	public void onBackPressed() {
		selectedRadio = -1;
		try {
			if (selectedItem != -1) {
				startActivity(new Intent(Description.this, MyOrder.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();
			} else {
				startActivity(new Intent(Description.this, RestaurantItem.class));
				overridePendingTransition(R.anim.right_slide_in,
						R.anim.right_slide_out);
				finish();
			}
			finish();
		} catch (Exception e) {
			Log.e("", e.getMessage());
		}
	}
}
