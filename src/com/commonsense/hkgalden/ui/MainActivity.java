package com.commonsense.hkgalden.ui;

import com.commonsense.hkgaldenPaid.R;
import com.commonsense.hkgalden.util.DatabaseManager;
import com.commonsense.hkgalden.util.GaldenUtils;
import com.commonsense.hkgalden.util.SystemUtils;
import com.commonsense.hkgalden.web.WebAccess;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {
	/* The click listener for ListView in the navigation drawer */
	public class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}
	private DrawerLayout mDrawerLayout;
	private ListView LmDrawerList;
	private ListView RmDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	public static final String preference = "LoginPrefs";
	private String[] mChannelTitles;
	public boolean isExpiry = false;
	public int statusCode;
	static logoutTask lt; 
	//	private SensorManager mSensorManager;
	//	private float historyMove[] = new float[3];
	//	private static final int SHAKE_THRESHOLD = 10;
	private boolean dark = false;
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		getWindow().setBackgroundDrawable(null);	
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		dark  =  preferences.getBoolean("dark", false);		
		if(dark==true){			
			//	setTheme(android.R.style.Theme_Holo);				
			setContentView(R.layout.activity_main_dark);
		}
		else{				
			setContentView(R.layout.activity_main);
		}
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(null);
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		dark  =  preferences.getBoolean("dark", false);		
		if(dark==true){			
			//	setTheme(android.R.style.Theme_Holo);				
			setContentView(R.layout.activity_main_dark);
			//this.getActionBar()
			setTheme(android.R.style.Theme_Holo);			

			this.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
		}
		else{				
			setContentView(R.layout.activity_main);
		}

		//auto orientation
		//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

		mTitle = mDrawerTitle = getTitle();
		//		mChannelTitles = getResources().getStringArray(R.array.channel_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		LmDrawerList = (ListView) findViewById(R.id.left_drawer);
		RmDrawerList = (ListView) findViewById(R.id.right_drawer);

		DatabaseManager.init(getApplication());
		mChannelTitles = DatabaseManager.getInstance().getAllChannelName();

		int layout = dark ? R.layout.drawer_list_item_dark : R.layout.drawer_list_item;


		// set up the drawer's list view with items and click listener
		LmDrawerList.setAdapter(new ArrayAdapter<String>(this.getBaseContext(),
				layout, mChannelTitles));
		LmDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		RmDrawerList.setAdapter(new ArrayAdapter<String>(this.getBaseContext(),
				layout, getResources().getStringArray(R.array.specials_array)));
		RmDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				RmDrawerList.setItemChecked(position, true);
				switch (position) {
				case 0:
					startActivity(new Intent(MainActivity.this, TwitterListActivity.class));
					break;
				case 1:
					startActivity(new Intent(MainActivity.this, YoutubeChannel.class));
					break;
				}
				mDrawerLayout.closeDrawer(RmDrawerList);
			}
		});

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		//		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		//		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		//				SensorManager.SENSOR_DELAY_NORMAL);
		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

		//Check session
		SharedPreferences settings = getSharedPreferences(preference, 0);

		if (settings.getInt("expiryyear", 0) > 0){

			int[] now = SystemUtils.getNow();
			int expiryyear = settings.getInt("expiryyear", 0);
			int expirymonth = settings.getInt("expirymonth", 0);
			int expiryday = settings.getInt("expiryday", 0);

			lt = new logoutTask(getApplication());
			if (now[0] > expiryyear){
				SystemUtils.toast(getApplication(), "Session Expired! Please Login Again.");
				lt.execute();
			}
			else if ((now[0] >= expiryyear) && (now[1] > expirymonth)){
				SystemUtils.toast(getApplication(), "Session Expired! Please Login Again.");
				lt.execute();
			}
			else if ((now[0] >= expiryyear) && (now[1] > expirymonth) &&(now[2] >= expiryday)){
				SystemUtils.toast(getApplication(), "Session Expired! Please Login Again.");
				lt.execute();
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		//		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		//		mSensorManager.unregisterListener(mSensorListener);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch(item.getItemId()) {
		
		case R.id.action_refresh:
			ChannelFragment fragment = (ChannelFragment) getFragmentManager().findFragmentById(R.id.content_frame);
			fragment.refresh();
			return true;
		case R.id.action_newPost:
			String preference = "LoginPrefs";
			SharedPreferences settings = getSharedPreferences(preference, 0);
			String token = settings.getString("tokenplus", "");
			if (token.equals("")){
				SystemUtils.toast(getApplicationContext(), "你未登入喎!");
				startActivity(new Intent(this, Profile.class));	
			}
			else {
				startActivity(new Intent(MainActivity.this, NewPost.class).putExtra("ident" , GaldenUtils.getIdent()));				
			}
			return true;
		case R.id.action_favourite:
			SystemUtils.toast(MainActivity.this, "賣飛佛");
			startActivity(new Intent(MainActivity.this, Favourite.class));
			return true;
		case R.id.action_history:
			startActivity(new Intent(MainActivity.this, History.class));
			return true;
		case R.id.action_lm:
			startActivity(new Intent(MainActivity.this, LM.class));
			return true;
		case R.id.action_lm_op:
			settings = getSharedPreferences("LoginPrefs", 0);
			token = settings.getString("tokenplus", "");
			if (token.equals("")){
				SystemUtils.toast(getApplicationContext(), "你未登入喎!");
				startActivity(new Intent(this, Profile.class));	
			}
			else {

				final EditText input = new EditText(MainActivity.this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				input.setText(settings.getString("id", "冧巴"));  
				input.setLayoutParams(lp);

				//	SystemUtils.toast(getApplicationContext(), "!!!!!");
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(getResources().getString(R.string.action_lm_op));
				builder.setMessage(getResources().getString(R.string.action_lm_opuser))
				.setCancelable(true).setView(input)
				.setPositiveButton("Search", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if(input.getText().toString().trim().length() ==0){
							SystemUtils.toast(MainActivity.this, "UserId Missing");
						}
						startActivity(new Intent(MainActivity.this,
								LMCreateActivity.class).putExtra("user", input.getText().toString().trim()));					 
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).show();
			}	
			return true;
		case R.id.action_profile:
			startActivity(new Intent(MainActivity.this, Profile.class));
			return true;
		case R.id.action_blist:
			settings = getSharedPreferences("LoginPrefs", 0);
			token = settings.getString("tokenplus", "");
			if (token.equals("")){
				SystemUtils.toast(getApplicationContext(), "你未登入喎!");
				startActivity(new Intent(this, Profile.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));	
			}
			else {
				startActivity(new Intent(MainActivity.this, BlockListActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("user", ""));				
			}
			return true;
		case R.id.action_settings:
			startActivity(new Intent(MainActivity.this, Setting.class));
			return true;
		case R.id.action_share:
			Intent intent = new Intent(Intent.ACTION_SEND);
			String packageName = this.getPackageName();
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, "HKGalden 香港膠登討論區瀏覽器，一個嚮往自由，百鳥爭鳴的香港本土討論區 " + "http://market.android.com/details?id="+packageName);
			intent.putExtra(android.content.Intent.EXTRA_SUBJECT, 
					getResources().getString(R.string.app_name) +  getResources().getString(R.string.app_ver) );
			startActivity(Intent.createChooser(intent, "Share"));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new ChannelFragment();
		Bundle args = new Bundle();
		args.putInt(ChannelFragment.ARG_CHANNEL_NUMBER, position);
		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		//		fragmentManager.beginTransaction().add(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		LmDrawerList.setItemChecked(position, true);
		setTitle(mChannelTitles[position]);
		mDrawerLayout.closeDrawer(LmDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	public class logoutTask extends AsyncTask<String, String, Boolean> {

		private ProgressDialog dialog;

		public logoutTask(final Application application) {
			dialog = new ProgressDialog(application);

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(MainActivity.this, "撈緊", "等陣...", true, false);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			if (dialog!=null) {
				dialog=null;
			}

			if (result){
				SystemUtils.toast(getApplication(), "unreg Success!");
			}else SystemUtils.toast(getApplication(), "unreg Fail! Error code: " + Integer.toString(statusCode));
		}


		@Override
		protected Boolean doInBackground(String... params) {
			return unreg();
		}
	}

	public boolean unreg(){
		boolean result = false;
		SharedPreferences settings = getSharedPreferences(preference, 0);
		String token = settings.getString("tokenplus", "");
		WebAccess wa = new WebAccess();
		String string = wa.logout(token);
		statusCode = Integer.valueOf(string.substring(0, 2));
		String msg = string.substring(3, string.length());
		if (msg.equals("{\"msg\":\"unreg_success\"}")){

			result = true;

			settings.edit().remove("password").commit();
			settings.edit().remove("id").commit();
			settings.edit().remove("username").commit();
			settings.edit().remove("tokenplus").commit();

			settings.edit().remove("expiryyear").commit();
			settings.edit().remove("expirymonth").commit();
			settings.edit().remove("expiryday").commit();
		}
		return result;
	}
}