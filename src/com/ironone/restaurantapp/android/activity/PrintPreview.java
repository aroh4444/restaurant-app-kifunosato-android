package com.ironone.restaurantapp.android.activity;

import java.io.IOException;
import java.io.InputStream;

import com.ironone.restaurantapp.R;
import com.ironone.restaurantapp.R.layout;
import com.ironone.restaurantapp.R.menu;
import com.ironone.restaurantapp.android.logic.StaticData;
import com.ironone.restaurantapp.android.print.BluetoothPrintService;
import com.ironone.restaurantapp.android.print.PrintManager;
import com.woosim.printer.WoosimImage;
import com.woosim.printer.WoosimService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebView.PictureListener;
import android.widget.Toast;

public class PrintPreview extends Activity {
	private WebView webView;
	private String htmlDocument = "";
	private boolean pictureDrawn = false;
	private Bitmap bm = null;
	private String TAG = "com.ironone.restaurantapp.android.activity/PrintPreview.java";
	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the print services
	private BluetoothPrintService mPrintService = null;
	private WoosimService mWoosim = null;

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	private static final byte EOT = 0x04;
	private static final byte LF = 0x0a;
	private static final byte ESC = 0x1b;
	private static final byte GS = 0x1d;
	private static final byte[] CMD_INIT_PRT = { ESC, 0x40 }; // Initialize
																// printer (ESC
																// @)

	// Message types sent from the BluetoothPrintService Handler
	public static final int MESSAGE_DEVICE_NAME = 1;
	public static final int MESSAGE_TOAST = 2;
	public static final int MESSAGE_READ = 3;

	// Key names received from the BluetoothPrintService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.print_preview);
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, R.string.blutooth_adaptor_not_found,
					Toast.LENGTH_LONG).show();
			return;
		}

		// Create a WebView object specifically for printing
		webView = (WebView) findViewById(R.id.wbViewPrintPreview);
		webView.setWebViewClient(new WebViewClient() {

			public void onPageStarted(WebView paramWebView, String paramString,
					Bitmap paramBitmap) {
				super.onPageStarted(paramWebView, paramString, paramBitmap);

			}

			public void onPageFinished(WebView webView, String url) {
				webView.capturePicture();

			}
		});

		PrintManager manager = new PrintManager(getApplicationContext());
		htmlDocument = manager.getPrintData(StaticData.currentOrder);

		// Generate an HTML document on the fly:
		// htmlDocument = "<html><body><h1>Test Content</h1><p>Testing, "+
		// "testing, testing...</p></body></html>";
		webView.loadDataWithBaseURL("", htmlDocument, "text/html", null, "");
		webView.setPictureListener(new PictureListener() { // check
			// picture
			// is
			// set

			public void onNewPicture(WebView view, Picture picture) {
				if (picture != null) {
					if (!pictureDrawn) {
						pictureDrawn = true;
						try {
							bm = pictureDrawable2Bitmap(new PictureDrawable(
									picture));

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		});

		// setContentView(webView);
	}

	public void onPrintPreviewClick(View v) {
//		webView.measure(MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED,
//				MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0,
//				MeasureSpec.UNSPECIFIED));
//		webView.layout(0, 0, webView.getMeasuredWidth(),
//				webView.getMeasuredHeight());
//		webView.setDrawingCacheEnabled(true);
//		webView.buildDrawingCache();
//
//		Picture picture = webView.capturePicture();
//
//		bm = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(),
//				Bitmap.Config.ARGB_8888);

		Intent serverIntent = new Intent(getApplicationContext(),
				DeviceListActivity.class);
		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupPrint() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mPrintService == null)
				setupPrint();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mPrintService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mPrintService.getState() == BluetoothPrintService.STATE_NONE) {
				// Start the Bluetooth print services
				mPrintService.start();
			}
		}
	}

	private void setupPrint() {
		Log.d(TAG, "setupPrint()");

		// Initialize the BluetoothPrintService to perform bluetooth connections
		mPrintService = new BluetoothPrintService(this, mHandler);
		mWoosim = new WoosimService(mHandler);
	}

	// The Handler that gets information back from the BluetoothPrintService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			case MESSAGE_READ:
				mWoosim.processRcvData((byte[]) msg.obj, msg.arg1);
				break;
			case WoosimService.MESSAGE_PRINTER:
				switch (msg.arg1) {
				case WoosimService.MSR:
					Log.d(TAG, "MSR");
					if (msg.arg2 == 0) {
						Toast.makeText(getApplicationContext(),
								"MSR reading failure", Toast.LENGTH_SHORT)
								.show();
					} else {

					}
					break;
				}
				break;
			}
		}
	};

	@Override
	public synchronized void onPause() {
		super.onPause();
		Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth print services
		if (mPrintService != null)
			mPrintService.stop();
		Log.e(TAG, "--- ON DESTROY ---");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.print_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent serverIntent = null;
		switch (item.getItemId()) {
		case R.id.secure_connect_scan:
			// Launch the DeviceListActivity to see devices and do scan
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			return true;
		case R.id.insecure_connect_scan:
			// Launch the DeviceListActivity to see devices and do scan
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent,
					REQUEST_CONNECT_DEVICE_INSECURE);
			return true;
		}
		return false;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, true);
			}
			break;
		case REQUEST_CONNECT_DEVICE_INSECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, false);
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a print
				setupPrint();
			} else {
				// User did not enable Bluetooth or an error occurred
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BLuetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		mPrintService.connect(device, secure);

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pirntBMPImage();
	}

	/**
	 * Print data.
	 * 
	 * @param data
	 *            A byte array to print.
	 */
	private void sendData(byte[] data) {

		// Check that we're actually connected before trying printing
		if (mPrintService.getState() != BluetoothPrintService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Check that there's actually something to send
		if (data.length > 0)
			mPrintService.write(data);
	}

	public void pirntBMPImage() {

		if (pictureDrawn) {
			Bitmap bmp = bm;

			if (bmp == null) {
				Log.e(TAG, "resource decoding is failed");
				return;
			}

			byte[] data = WoosimImage.printRGBbitmap(0, 0, 384, bmp.getHeight(), bmp);
			bmp.recycle();

			byte[] cmd_pagemode = { ESC, 0x4c }; // Select page mode (ESC L)
			byte[] cmd_stdmode = { ESC, 0x53 }; // Select standard mode (ESC S)

			sendData(cmd_pagemode);
			sendData(data);
			sendData(cmd_stdmode);
			finish();
		}

	}

	public void pirnt2inchReceipt() {

		InputStream inStream = getResources().openRawResource(R.raw.receipt2);
		sendData(CMD_INIT_PRT);
		try {
			byte[] data = new byte[inStream.available()];
			while (inStream.read(data) != -1) {
				sendData(data);
			}
		} catch (IOException e) {
			Log.e(TAG, "sample 2inch receipt print fail.", e);
		} finally {
			if (inStream != null)
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		/* Shift-JIS Font Sample Print */
		// StringBuffer strbuff = new StringBuffer();
		//
		// strbuff.append("ã�‚ ã�� ã�„ ã�ƒ ã�† ã�… ã�ˆ ã�‡ ã�Š ã�‰ ã�‹ ã�� ã�� ã�‘ ã�“ ã�Œ ã�Ž ã�� ã�’ ã�” ã�• ã�— ã�™ ã�› ã�� ã�– ã�˜ ã�š ã�œ ã�ž ã�Ÿ ã�¡ ã�¤ ã�¦ ã�¨ ã�  ã�¢ ã�¥ ã�§ ã�© ã�ª ã�« ã�¬ ã�­ ã�® ã�¯ ã�² ã�µ ã�¸ ã�» ã�° ã�³ ã�¶ ã�¹ ã�¼ ã�± ã�´ ã�· ã�º ã�½ ã�¾ ã�¿ ã‚€ ã‚� ã‚‚ ã‚„ ã‚ƒ ã‚† ã‚… ã‚ˆ ã‚‡ ã‚‰ ã‚Š ã‚‹ ã‚Œ ã‚� ã‚� ã‚Ž ã‚’ ã‚“ ã�£ \r\n\r\n");
		// strbuff.append("ã‚¢ ã‚¡ ã‚¤ ã‚£ ã‚¦ ã‚¥ ã‚¨ ã‚§ ã‚ª ã‚© ã‚« ã‚­ ã‚¯ ã‚± ã‚³ ã‚¬ ã‚® ã‚° ã‚² ã‚´ ã‚µ ã‚· ã‚¹ ã‚» ã‚½ ã‚¶ ã‚¸ ã‚º ã‚¼ ã‚¾ ã‚¿ ãƒ� ãƒ„ ãƒ† ãƒˆ ãƒ€ ãƒ‚ ãƒ… ãƒ‡ ãƒ‰ ãƒŠ ãƒ‹ ãƒŒ ãƒ� ãƒŽ ãƒ� ãƒ’ ãƒ• ãƒ˜ ãƒ› ãƒ� ãƒ“ ãƒ– ãƒ™ ãƒœ ãƒ‘ ãƒ” ãƒ— ãƒš ãƒ� ãƒž ãƒŸ ãƒ  ãƒ¡ ãƒ¢ ãƒ¤ ãƒ£ ãƒ¦ ãƒ¥ ãƒ¨ ãƒ§ ãƒ© ãƒª ãƒ« ãƒ¬ ãƒ­ ãƒ¯ ãƒ® ãƒ² ãƒ³ ãƒ´ ãƒƒ \r\n\r\n");
		// strbuff.append("äºœå”–æ‚ªåœ§å›²ç‚ºåŒ»å£±ç¨²é£²éš å˜˜æ¬�åŽ©å–¶æ „é ´é‹­é§…æ‚¦é–²ç„”ç¸�å¡©å¥¥å¿œæ¨ªæ¬§æ®´é´¬é´Žé»„æ¸©ç©�ä»®ä¾¡å˜©ç”»ä¼šå£Šæ‡�çµµæ¦‚æŸ¿è›ŽéˆŽæ‹¡æ’¹æ®»è¦šå­¦æ¥½æ¸‡å‹§å·»å¯›æ­“æ½…è¦³è«Œé–¢é™¥èˆ˜å·Œæ—¢å¸°æ°—äº€å�½æˆ¯çŠ æ—§æ‹ æŒ™è™šä¾ å³¡å¼·æŒŸæ•™ç‹­éƒ·å°­æš�");
		// strbuff.append("å€¶åŒºèº¯é§†å‹²è–«å¾„æ�µæŽ²æ¸“çµŒç¶™ç¹‹èŒŽè›�è»½é šé¶�æ’ƒå€¹å‰£åœ�æ¤œæ¨©çŒ®ç ”çœŒé™ºé¡•é¨“åŽ³æˆ¸å‘‰å¨¯åŠ¹åºƒæ˜‚é‰±å�·éº¹å›½é»’æ­³æ¸ˆç •æ–Žå‰¤æ¡œé›‘å�‚æƒ¨æ¡Ÿèš•è®ƒè³›æ®‹ç³¸æ­¯å…�è¾žæ¹¿å®Ÿå±¡è•ŠèˆŽå†™é‡ˆå¯¿å�Žç¹�å¾“æ¸‹ç�£ç¸¦ç²›å‡¦ç·’å�™å¥¨å°†å°šæ¸‰ç„¼ç§°");
		// strbuff.append("\r\n\r\n");
		// strbuff.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()\r\n\r\n");
		// String string = strbuff.toString();
		//
		// byte[] shiftJIS = null;
		// try {
		// shiftJIS = string.getBytes("Shift_JIS");
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// if (shiftJIS.length == 0) return;
		//
		// sendData(CMD_INIT_PRT);
		// sendData(shiftJIS);
	}

	/**
	 * convet pictur into bitmap
	 * 
	 * @param pictureDrawable
	 * @return
	 */
	private Bitmap pictureDrawable2Bitmap(PictureDrawable pictureDrawable) {
		Bitmap bitmap = Bitmap.createBitmap(
				pictureDrawable.getIntrinsicWidth(),
				pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		canvas.drawPicture(pictureDrawable.getPicture());
		Log.e("Height", String.valueOf(bitmap.getHeight()));
		return bitmap;

	}

}
