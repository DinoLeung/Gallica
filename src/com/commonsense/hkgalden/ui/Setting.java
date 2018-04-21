package com.commonsense.hkgalden.ui;

import java.io.File;

import com.commonsense.hkgaldenPaid.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;


public class Setting extends PreferenceActivity implements OnSharedPreferenceChangeListener {
	
	boolean oDark;
	boolean oTm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);	 
		
		//get original dark mode setting
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		oDark = preferences.getBoolean("dark", false);
		oTm = preferences.getBoolean("tm", false);
		
		if(oDark){
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//				Window win = getWindow();
//				WindowManager.LayoutParams winParams = win.getAttributes();
//				final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//				winParams.flags |= bits;
//				win.setAttributes(winParams);
//				SystemBarTintManager tintManager = new SystemBarTintManager(this);
//				tintManager.setStatusBarTintEnabled(true);
//				tintManager.setStatusBarTintColor(Color.BLACK);
//			}
			getListView().setBackgroundColor(Color.BLACK);
			setTheme(R.style.Theme_DarkText);
		}
		else{
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#777777")));
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//				Window win = getWindow();
//				WindowManager.LayoutParams winParams = win.getAttributes();
//				final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//				winParams.flags |= bits;
//				win.setAttributes(winParams);
//				SystemBarTintManager tintManager = new SystemBarTintManager(this);
//				tintManager.setStatusBarTintEnabled(true);
//				tintManager.setStatusBarTintColor(Color.parseColor("#777777"));
//			}
		}

		//     this.getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_bar_setting));
		this.setTitle("設定");
		getFragmentManager().beginTransaction().add(android.R.id.content, new SettingFragment()).commit();
		SharedPreferences sp=  getSharedPreferences("brightness", 0);
		sp.registerOnSharedPreferenceChangeListener(this);
		//f9d33f 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.setting_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.

		// Handle action buttons
		switch(item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.action_report:
			String OSver = System.getProperty("os.version");
			String api = String.valueOf(android.os.Build.VERSION.SDK_INT);
			String device = android.os.Build.DEVICE;
			String model = android.os.Build.MODEL;
			String brand = android.os.Build.BRAND;
			reportToAdmin(getResources().getStringArray(R.array.emailDevelop) , 
					getResources().getString(R.string.subject),
					getResources().getString(R.string.message) +"\n\n\n\n\n\nDevice info:" + "\nOS version: "+OSver+"\nAPI level: "+api+"\nDevice: "+device+"\nModel: "+model+"\nBrand: "+brand,
					null );
			return true;


		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void reportToAdmin(String[] recipents , String subject , String message , File attachment ) {
		// TODO Auto-generated method stub

		Intent mailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		mailIntent.setType("message/rfc822");	
		mailIntent.putExtra(Intent.EXTRA_EMAIL  , recipents);
		mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		mailIntent.putExtra(Intent.EXTRA_TEXT   , message);

		if(attachment!=null){
			if (!attachment.exists() || !attachment.canRead()) {
				Toast.makeText(this.getApplicationContext(), 
						"Attachment Error", 
						Toast.LENGTH_SHORT).show();
				System.out.println("ATTACHMENT ERROR");
			}
			else
			{
				Uri uri = Uri.fromFile(attachment);
				mailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			}
		}


		//Send, if valid!
		try {
			startActivity(Intent.createChooser(mailIntent, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this.getApplicationContext(), 
					"There are no email clients installed.", 
					Toast.LENGTH_SHORT).show();
		}


	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		boolean nDark = preferences.getBoolean("dark", false);
		boolean nTm = preferences.getBoolean("tm", false);

		if( keyCode==KeyEvent.KEYCODE_BACK && (nDark!=oDark || nTm != oTm)) 
		{
			startActivity(new Intent(Setting.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
		}
		return super.onKeyDown(keyCode, event);	
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
		// TODO Auto-generated method stub
		if(TextUtils.equals(key, "brightness")){
			int brightness=sp.getInt("brightness", 125);
			LayoutParams lp=   getWindow().getAttributes();
			lp.screenBrightness=brightness/255.0f;
			getWindow().setAttributes(lp);
		}
	}


}