package com.commonsense.hkgalden.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.commonsense.hkgaldenPaid.R;
import com.commonsense.hkgalden.async.ReplyYourStuff;
import com.commonsense.hkgalden.backend.RateIntentService;
import com.commonsense.hkgalden.backend.RateResponseReceiver;
import com.commonsense.hkgalden.model.Favourite;
import com.commonsense.hkgalden.model.History;
import com.commonsense.hkgalden.model.Lm;
import com.commonsense.hkgalden.util.DatabaseManager;
import com.commonsense.hkgalden.util.FileUtil;
import com.commonsense.hkgalden.util.GaldenUtils;
import com.commonsense.hkgalden.util.SystemUtils;
import com.commonsense.hkgalden.web.WebAccess;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.google.ads.*;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

//2013-12-08 larrylo read post incomplete
public class ReadPost extends FragmentActivity {

	private ArrayList<String> functions;
	private String topicId;
	private String page;
	private String color;
	String title;
	String uname;
	String f_ident;
	int tlock;
	String good;
	String bad;

	int numberOfPages;

	private String preference = "LoginPrefs";
	private String token;
	private Intent intent;
	private History history = new History();
	private Favourite favourite = new Favourite();
	private RateResponseReceiver receiver;
	FragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;
	int mCount;
	float density;
	ReadPost parent = this;
	Fragment fragment;
	
	Menu menu;
	
	private AdView adView;
	
	private EasyTracker galdenTracker = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawable(null);	

		setContentView(R.layout.readpost_frame);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		//Admob
//		adView = new AdView(this, AdSize.BANNER, "a152cf95090c478");
//		LinearLayout layout = (LinearLayout)findViewById(R.id.ad_view);
//		layout.addView(adView);
//		adView.loadAd(new AdRequest());
		
		SharedPreferences settings = getSharedPreferences("LoginPrefs", 0);
		token = settings.getString("tokenplus", "");

		intent = getIntent();
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			Uri data = getIntent().getData();
			List<String> params = data.getPathSegments();
			topicId = params.get(1); // "1234"
			if (params.contains("page"))
				page = params.get(params.indexOf("page")+1);
			else
				page = "1";

		}	else{
			topicId  = getIntent().getExtras().getString("topic");
			page  = getIntent().getExtras().getString("page");
		}
		
		
		//google analytics
		galdenTracker = EasyTracker.getInstance(ReadPost.this);
		galdenTracker.send(MapBuilder.createEvent("Gallica", "view post", topicId, null).build());
		
		
		//initialize viewpager
		mAdapter = new FragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        density = getResources().getDisplayMetrics().density;
		
        //go to the page
        if (page.equals(null)){
        	mCount = 1;
        	mAdapter.notifyDataSetChanged();
            mIndicator.notifyDataSetChanged();
        }
        else{
        	mCount = Integer.valueOf(page);
        	mAdapter.notifyDataSetChanged();
        	mIndicator.notifyDataSetChanged();
        	mPager.setCurrentItem(Integer.valueOf(page));
        }
        
        //make the nev bar transparent
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintColor(Color.parseColor("#000000"));
		}
		
		IntentFilter filter = new IntentFilter(RateResponseReceiver.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new RateResponseReceiver();
		registerReceiver(receiver, filter);

	}
	
	class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) 
        {
    		fragment = new ReadPostFragment(parent, this, topicId, Integer.toString(position+1));
            return fragment;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mCount;
        }
        
        public void setCount(int replyCount){
        	//cal page number
			int remainder =  Integer.valueOf(replyCount) % 25 ;
			int numberOfPages =  Integer.valueOf(replyCount) / 25 + 1 ;
			if(remainder ==0)
				numberOfPages =  Integer.valueOf(replyCount) / 25;
			if(numberOfPages > mCount)
				mCount = numberOfPages;
            notifyDataSetChanged();
            mIndicator.notifyDataSetChanged();
        }
        
        @Override
        public CharSequence getPageTitle(int position){
        	String title = "第 "+Integer.toString(position+1)+" 頁";
            return title;
        }

    }
	
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
 
	@Override
	public void onStop() {
		super.onStop();
		History history = new History();
		history.setId(topicId);
		history.setUname(uname);
		history.setTitle(title);
		history.setPage(Integer.toString(mPager.getCurrentItem()+1));
		history.setIdent(f_ident);
		history.setDate();
		DatabaseManager.init(parent);
		DatabaseManager.getInstance().addHistory(history);
		EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy()
	{		
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if ((keyCode == KeyEvent.KEYCODE_BACK)) { //Back key pressed
			//Things to Do
			String uri = Environment.getExternalStorageDirectory()+ File.separator + "Gallica" + File.separator + "ReadPost";
			File dir = new File(uri);
			if(dir.exists()){
				FileUtil.deleteDirectory(dir);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_post, menu);
		this.menu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final int menuItemIndex = item.getItemId();
		String ss  = functions.get(menuItemIndex).toString();
		if (ss.equalsIgnoreCase("Quote")) {

			SystemUtils.toast(ReadPost.this ,  "Quote");

		} else if (ss.equalsIgnoreCase("Block")){

			new AlertDialog.Builder(ReadPost.this)
			.setTitle("Warning")
			.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int whichButton) {
					SystemUtils.toast(ReadPost.this, "Blocked");


				}
			})
			.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int whichButton) {
					// Do nothing.
				}
			}).show();

		}

		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;	

		case R.id.action_post_refresh:
			refresh();
			return true;
		case R.id.action_share:
			galdenTracker.send(MapBuilder.createEvent("Gallica", "share", topicId, null).build());
			share();
			return true;			
		case R.id.action_reply:
			reply();
			return true;
		case R.id.action_onelm:
			galdenTracker.send(MapBuilder.createEvent("Gallica", "reply", topicId, null).build());
			oneLm();
			return true;
		case R.id.action_selectp:
			selectPage();
			return true;
		case R.id.action_favour:
			galdenTracker.send(MapBuilder.createEvent("Gallica", "favourite", topicId, null).build());
			favour();
			return true;
		case R.id.action_good:
			galdenTracker.send(MapBuilder.createEvent("Gallica", "rate", topicId, null).build());
			rate(true);
			return true;
		case R.id.action_bad:
			galdenTracker.send(MapBuilder.createEvent("Gallica", "rate", topicId, null).build());
			rate(false);
			return true;
		case R.id.action_settings:
			setting();
			return true;
		case R.id.action_finish:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void selectPage() {
		// TODO Auto-generated method stub

		ArrayList<String> pageSet = new ArrayList<String>();
		for(int t = 0 ; t < numberOfPages ; t++){
			pageSet.add("第 " + String.valueOf(t+1)+" 頁");
		}
		if(numberOfPages==0){
			pageSet.add("第 " + String.valueOf("1")+" 頁");
		}
		String[] pageArray = new String[pageSet.size()];
		pageArray = pageSet.toArray(pageArray);
		final String[] option = pageArray;			
		new AlertDialog.Builder(this).setItems(option, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//TODO - Code when list item is clicked (int which - is param that gives you the index of clicked item)
				if(option[which].equalsIgnoreCase("第 " + String.valueOf(which+1)+" 頁")){
					SystemUtils.toast(ReadPost.this, "Page " + String.valueOf(which+1));
					Intent i = new Intent();
					i.setClass(ReadPost.this.getApplicationContext(), ReadPost.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("topic" ,topicId );
					i.putExtra("page" , String.valueOf(which+1));
					startActivity(i);
				}			 
			}			
		})
		.setTitle("我想睇...")
		.setCancelable(true)        
		.show();
	}




	private void lastPage() {
		SystemUtils.toast(ReadPost.this, "Last page");

		startActivity(new Intent(this, ReadPost.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
				.putExtra("topic" ,topicId )
				.putExtra("page" , String.valueOf(numberOfPages)));

	}




	private void firstPage() {
		// TODO Auto-generated method stub
		SystemUtils.toast(ReadPost.this, "First page");

		startActivity(new Intent(this, ReadPost.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
				.putExtra("topic" ,topicId )
				.putExtra("page" , "1"));


	}

	private void nextPage() {
		// TODO Auto-generated method stub
		SystemUtils.toast(ReadPost.this, "Next page");
		if (Integer.valueOf(page) == numberOfPages){
			SystemUtils.toast(ReadPost.this, "This is the LAST page!");
		}
		else{
			startActivity(new Intent(this, ReadPost.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
					.putExtra("topic" ,topicId )
					.putExtra("page" , String.valueOf(Integer.valueOf(page)+1)));
		}
	}

	private void previousPage() {
		// TODO Auto-generated method stub
		SystemUtils.toast(ReadPost.this, "Previous page");

		if (Integer.valueOf(page) == 1){
			SystemUtils.toast(ReadPost.this, "This is the FIRST page!");
		}
		else{
			startActivity(new Intent(this, ReadPost.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
					.putExtra("topic" ,topicId )
					.putExtra("page" , String.valueOf(Integer.valueOf(page)-1)));
		}
	}


	private void refresh() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, ReadPost.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
				.putExtra("topic" ,topicId )
				.putExtra("page" , Integer.toString(mPager.getCurrentItem()+1)));
	}

	private void rate(boolean good) {
		// TODO Auto-generated method stub
		SharedPreferences settings = getSharedPreferences(preference, 0);
		token = settings.getString("tokenplus", "");

		if (token.equals("")){
			SystemUtils.toast(getApplicationContext(), "你未登入喎!");
			startActivity(new Intent(this, Profile.class));	
		}
		else {
			if(isMyRateServiceRunning()){
				stopService(new Intent().setClass(ReadPost.this ,RateIntentService.class));
			}
			if (good){
				startService(new Intent().setClass(ReadPost.this ,RateIntentService.class).putExtra("argument", topicId + "|g|" + token ));
				menu.findItem(R.id.action_good).setTitle(getString(R.string.action_good) + " (" + (Integer.valueOf(this.good)+1) + ")");
				menu.findItem(R.id.action_good).setEnabled(false);
				menu.findItem(R.id.action_bad).setEnabled(false);
			}
			else{
				startService(new Intent().setClass(ReadPost.this ,RateIntentService.class).putExtra("argument", topicId + "|b|" + token ));
				menu.findItem(R.id.action_bad).setTitle(getString(R.string.action_bad) + " (" + (Integer.valueOf(this.bad)+1) + ")");
				menu.findItem(R.id.action_good).setEnabled(false);
				menu.findItem(R.id.action_bad).setEnabled(false);
			}
		}

	}

	private boolean isMyRateServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

			System.out.println("Running Service :" + service.service.getClassName());
			if (RateIntentService.class.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}



	private void reply() {
		// TODO Auto-generated method stub
		SharedPreferences settings = getSharedPreferences(preference, 0);
		token = settings.getString("tokenplus", "");

		if (token.equals("")){
			SystemUtils.toast(getApplicationContext(), "你未登入喎!");
			startActivity(new Intent(this, Profile.class));	
		}
		else if (tlock == 1){
			SystemUtils.toast(getApplicationContext(), "POST已推爆!");
		}
		else {
			startActivity(new Intent(this, Reply.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
					.putExtra("id", topicId)
					.putExtra("topic", title)
					.putExtra("isQuote", false)
					.putExtra("sCol", color));
		}

	}


	private void setting() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, Setting.class));
	}

	private void share() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, title + " " + GaldenUtils.postExternalLink + topicId);
	
		startActivity(Intent.createChooser(intent, "Share"));
	}

	private void favour() {
		// TODO Auto-generated method stub

		SystemUtils.toast(ReadPost.this, "favourite id " + topicId);
		favourite.setId(topicId);
		favourite.setDate();
		DatabaseManager.init(getApplication());		
		DatabaseManager.getInstance().addFavourite(favourite);		

	}

	private void oneLm() {
		// TODO Auto-generated method stub
		SharedPreferences settings = getSharedPreferences(preference, 0);
		token = settings.getString("tokenplus", "");

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String content  =  preferences.getString("lm_message","一鍵留名");
		Log.i("一鍵留名" , content);
		String argument = topicId+"|"+content+"|"+token+"|" + color ;
		if (token.equals("")){
			SystemUtils.toast(getApplicationContext(), "你未登入喎!");
			startActivity(new Intent(this, Profile.class));			
		}
		else if (tlock == 1){
			SystemUtils.toast(getApplicationContext(), "POST已推爆!");
		}
		else {
			new ReplyYourStuff(ReadPost.this).execute(argument);
			//add LM
			Lm lm = new Lm();
			lm.setId(topicId);
			lm.setDate();
			DatabaseManager.init(getApplication());
			DatabaseManager.getInstance().addLm(lm);
		}
	}


	public class rateTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog dialog;
		private int statusCode;

		public rateTask(final Application application) {
			dialog = new ProgressDialog(application);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(ReadPost.this, "撈緊", "等陣...", true, false);
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
				SystemUtils.toast(getApplication(), "成功評分!");
				onCreate(new Bundle());
			}
			else SystemUtils.toast(getApplication(), "Rate Fail! Error Code: " + Integer.toString(statusCode));
		}


		@Override
		protected Boolean doInBackground(String... params) {
			return getData(params[0]);
		}

		public Boolean getData(String argument){

			WebAccess wa = new WebAccess();
			String result = wa.postRate(argument);
			statusCode = Integer.parseInt(result);
			if (statusCode == 200){
				return true;
			}
			else{
				return false;
			}
		}
	}
}