package com.commonsense.hkgalden.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.commonsense.hkgaldenPaid.R;
import com.commonsense.hkgalden.adapter.TopicAdapter;
import com.commonsense.hkgalden.adapter.Topics;
import com.commonsense.hkgalden.model.Channel;
import com.commonsense.hkgalden.util.ColorUtils;
import com.commonsense.hkgalden.util.DatabaseManager;
import com.commonsense.hkgalden.util.GaldenUtils;
import com.commonsense.hkgalden.web.WebAccess;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.google.ads.*;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChannelFragment extends Fragment{
	public static final String ARG_CHANNEL_NUMBER = "channel_number";
	private String resultString = "";
	private LinearLayout llBg; 
	private ArrayList<Topics> listTopics = new ArrayList<Topics>();

	private TopicAdapter dataAdapter;
	private PullAndLoadListView list;
	static LoadPost mp;
	boolean refresh = false;
	static int currentPage = 1;
	private String ident;
	private String colour;
	private String token;
	private boolean tm;

	private LayoutInflater inflater;
	private ViewGroup container;
	
	private AdView adView;
	
	private EasyTracker galdenTracker = null;

	//	private Bundle savedInstanceState;

	public ChannelFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		this.inflater = inflater;
		this.container = container;
		View rootView = inflater.inflate(R.layout.fragment_channel, container, false);

		int i = getArguments().getInt(ARG_CHANNEL_NUMBER);
		
		DatabaseManager.init(this.getActivity());
		Channel channelData = DatabaseManager.getInstance().getAllChannel().get(i);
		String channel = channelData.getName();
		ident = channelData.getIdent();
		colour = "#"+channelData.getColor();
//		String channel = getResources().getStringArray(R.array.channel_array)[i];
//		ident = getResources().getStringArray(R.array.ident_array)[i];
//		colour = getResources().getStringArray(R.array.colour_array)[i];
		
		//Admob
//		adView = new AdView(getActivity(), AdSize.BANNER, "a152cf95090c478");
//		LinearLayout layout = (LinearLayout)rootView.findViewById(R.id.ad_view);
//		layout.addView(adView);
//		adView.loadAd(new AdRequest());

		//google analytics
		galdenTracker = EasyTracker.getInstance(getActivity());
		galdenTracker.send(MapBuilder.createEvent("Gallica", "view channel", ident, null).build());
		
		//switches
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

		llBg = (LinearLayout)rootView.findViewById(R.id.llForumChannel);
//		llBg.setBackgroundColor(Color.parseColor(colour));
		list = (PullAndLoadListView) rootView.findViewById(R.id.lstTopics);
		list.setSelector(R.drawable.list_selector);
		tm = preferences.getBoolean("tm", false);
		SharedPreferences settings = getActivity().getSharedPreferences("LoginPrefs", 0);
		token = settings.getString("tokenplus", "");
		
		boolean dark  =  preferences.getBoolean("dark", false);		
		if(!dark){		
//			llBg.setBackgroundColor(Color.parseColor(colour));
			getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(colour)));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				Window win = this.getActivity().getWindow();
				WindowManager.LayoutParams winParams = win.getAttributes();
				final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
				winParams.flags |= bits;
				win.setAttributes(winParams);
				SystemBarTintManager tintManager = new SystemBarTintManager(this.getActivity());
				tintManager.setStatusBarTintEnabled(true);
				tintManager.setStatusBarTintColor(Color.parseColor(colour));
				list.setBackgroundColor(ColorUtils.colorTone(colour, 0.2f, 1.45f));
			}
		}
		getActivity().setTitle(channel);

		
		ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			list.onRefresh();
			refresh = true;
			mp =  new LoadPost(getActivity());
			String[] execute = {ident, String.valueOf(currentPage), Boolean.toString(tm), token};
			mp.execute(execute);
		} else {
			Toast.makeText(getActivity().getApplicationContext(), "上唔到網呀大哥..." , Toast.LENGTH_LONG).show();
		}
		
		dataAdapter = new TopicAdapter(getActivity(),
				R.layout.topiclist_detail, listTopics);
		list.setAdapter(dataAdapter);
		
		// Set a listener to be invoked when the list should be refreshed.
		list.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				refresh();
				dataAdapter.notifyDataSetChanged();
			}
		});

		// set a listener to be invoked when the list reaches the end
		list.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
				if (networkInfo != null && networkInfo.isAvailable()) {
					mp =  new LoadPost(getActivity());
					currentPage++;
					String[] execute = {ident, String.valueOf(currentPage), Boolean.toString(tm), token};
					mp.execute(execute);
					dataAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getActivity().getApplicationContext(), "上唔到網呀大哥..." , Toast.LENGTH_LONG).show();
				}
			}
		});
		
		//Click item
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				final Topics st =  (Topics) arg0.getItemAtPosition(arg2);
				
				DatabaseManager.init(getActivity());
				com.commonsense.hkgalden.model.History historyData = DatabaseManager.getInstance().getHistory(st.getId());
				
				int page = 1;
				if (historyData != null)
					page = Integer.valueOf(historyData.getPage());
				startActivity(new Intent(getActivity(), ReadPost.class)		
					.putExtra("topic" ,st.getId())
					.putExtra("page", String.valueOf(page)));
			}
		});
		
		//long click
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				
				final Topics st =  (Topics) arg0.getItemAtPosition(arg2);

				int count = Integer.valueOf(st.getCount());
				int remainder =  count % 25 ;

				int numberOfPages =  count / 25 + 1 ;
				if(remainder ==0){
					numberOfPages =  count / 25;
				}

				ArrayList<String> pageSet = new ArrayList<String>();
				if(numberOfPages >0){
					for(int t = 0 ; t < numberOfPages ; t++){
						pageSet.add("第 " + String.valueOf(t+1)+" 頁");
					}
				}else{
						pageSet.add("第 1 頁");							
				}
			
				String[] pageArray = new String[pageSet.size()];
				pageArray = pageSet.toArray(pageArray);
				final String[] option = pageArray;	

				new AlertDialog.Builder(getActivity()).setItems(option, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						startActivity(new Intent(getActivity(), ReadPost.class)
						.putExtra("topic" ,st.getId().toString())
						.putExtra("page", String.valueOf(which+1)));
					}
				})
				.setTitle("睇邊頁")
				.setCancelable(true)        
				.show();

				return true;
			}

		});

		return rootView;
	}
	
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(getActivity()).activityStart(getActivity());
	}
 
	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(getActivity()).activityStop(getActivity());
	}

	public class LoadPost extends AsyncTask<String, Void, String>{
		private Activity activity;
		private volatile boolean running = true;
		public LoadPost(final Activity parent) {
			activity = parent;
		}
		
		@Override
		protected void onCancelled() {
			if(refresh){
				list.onRefreshComplete();
				refresh = false;
			}
			else
				list.onLoadMoreComplete();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
	
//			llBg.setBackgroundColor(Color.parseColor(colour));
			 
			try {
				JSONObject json = new JSONObject(result);
				JSONArray jsonArray = json.getJSONArray("topics");
				
				List<String> blockedList = new ArrayList<String>();
				try {
					JSONArray blockeduser = json.getJSONArray("blockedusers");
					for(int i = 0; i < blockeduser.length(); i++)
						blockedList.add(blockeduser.getString(i));
				}catch (Exception e){}

				//listTopics = new ArrayList<Topics> (); 
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonItem = jsonArray.getJSONObject(i);

				    String countryInfo =jsonItem.toString();
				    Topics country = new Gson().fromJson(countryInfo, Topics.class);
				    
				    if(!blockedList.contains(country.getUid()))
				    	listTopics.add(country);
				    
//				    dataAdapter.notifyDataSetChanged();
					
					if(refresh){
						refresh = false;
						list.onRefreshComplete();
					}
					else{
						list.onLoadMoreComplete();
					}
				}
				

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		protected String doInBackground(String... uid) {
			if (isCancelled())
				return null;
			return getPostList(uid[0], uid[1], uid[2], uid[3]);				
		}
	}
	
	public String getPostList(String ident, String page, String tm, String token){
		
		WebAccess wa = new WebAccess();
		String result = "";
		resultString = "";
		Log.d("ident" , ident);
		Log.d("page" , page);
		result = wa.getData(ident, page, tm, token);	
		resultString+=result;
		return result;
	}

	public void refresh()
	{
		ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			String ident = GaldenUtils.getIdent();
			currentPage = 1;
			String[] execute = {ident, String.valueOf(currentPage), Boolean.toString(tm), token};
			listTopics.clear();
			dataAdapter.clear();
			refresh = true;
			mp =  new LoadPost(getActivity());
			mp.execute(execute);
		} else {
			Toast.makeText(getActivity().getApplicationContext(), "上唔到網呀大哥..." , Toast.LENGTH_LONG).show();
		}
	}
}