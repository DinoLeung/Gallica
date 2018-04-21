package com.commonsense.hkgalden.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.commonsense.hkgaldenPaid.R;
import com.commonsense.hkgalden.adapter.Content;
import com.commonsense.hkgalden.adapter.ContentAdapter;
import com.commonsense.hkgalden.ui.ReadPost.FragmentAdapter;
import com.commonsense.hkgalden.util.ColorUtils;
import com.commonsense.hkgalden.util.SystemUtils;
import com.commonsense.hkgalden.web.WebAccess;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.viewpagerindicator.TitlePageIndicator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;

public class ReadPostFragment extends Fragment{

	String id;
	String page;
	String title;
	int tlock;
	boolean rated;
	String token;
	boolean dark;
	ReadPost parent;
	FragmentAdapter adapter;
	WebView web;
	
	
	public ReadPostFragment(ReadPost parent, FragmentAdapter adapter ,String id, String page){
		this.id = id;
		this.page = page;
		this.parent = parent;
		this.adapter = adapter;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.readpost_webview, container, false);
		
		web = (WebView) v.findViewById(R.id.webView1);
		web.setBackgroundColor(Color.TRANSPARENT);
		web.setWebChromeClient(new WebChromeClient());
//		web.getSettings().setJavaScriptEnabled(true);
		web.loadDataWithBaseURL(null, "<div style='position: absolute;top: 50%;left: 50%;margin-top: -50px;margin-left: -50px;width: 100px;height: 100px;'><span style='color:#FFFFFF'>等陣...</span></div>", "text/html", "utf-8", null);
		
		SharedPreferences settings = getActivity().getSharedPreferences("LoginPrefs", 0);
		token = settings.getString("tokenplus", "");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		dark  =  preferences.getBoolean("dark", false);
		
		web.setWebViewClient(new WebViewClient() {
        	
        	@Override
        	public void onPageFinished(WebView view, java.lang.String url) {
//        		Log.d(TAG,"onPageFinished called with url : ");
        		super.onPageFinished(view, url);
        	}
        	
        	@Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		Log.d("testet","shouldOverrideUrlLoading called with url : " + url);		        		
                if (url.startsWith("gallica")){
                	SharedPreferences settings = getActivity().getSharedPreferences("LoginPrefs", 0);
					String token = settings.getString("tokenplus", "");
				
//                	Toast.makeText(getApplicationContext(), Arrays.toString(data),Toast.LENGTH_SHORT).show();
//					int tlock = settings.getInt("tlock", 0);

					if (token.equals("")){
						SystemUtils.toast(getActivity(), "你未登入喎!");
						startActivity(new Intent(getActivity(), Profile.class));	
					}
					else if (tlock == 1){
						SystemUtils.toast(getActivity().getApplicationContext(), "POST已推爆!");
					}
					else {
						//String[] data = url.split("/");
						String[] data = url.split("\\//");
						String ss = data[1];
						String[] regex = ss.split("\\/");
						String function = regex[0];

						if("quote".equals(function)){  //data[2].equals("quote")

							String quoteId = regex[1];
							String status = regex[2];									
							startActivity(new Intent(getActivity(), Reply.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
									.putExtra("id", id)
									.putExtra("topic", title)
									.putExtra("isQuote", true)
									.putExtra("quoteId",quoteId) // data[3])
									.putExtra("isTopic", Boolean.valueOf(status))); //Boolean.valueOf(data[4])
									
						}
						//CommenSense please re-check as HTTP error medsage shown without first vote
//						if("rate".equals(function)){ //data[2].equals("rate")
//							SystemUtils.toast(getApplicationContext(),"RATE!");
//							if (rated){
//								SystemUtils.toast(getApplicationContext(), "你一早評咗分!");
//							}
//							else{
//								if(isMyRateServiceRunning()){
//									stopService(new Intent().setClass(ReadPost.this ,RateIntentService.class));
//								}	
//								//String topicId = regex[1];
//								String status = regex[2];
//								
//								startService(new Intent().setClass(ReadPost.this ,RateIntentService.class).
//										putExtra("argument", topicId  + status + token )); 
//								//topicId + data[4] + token
//							}
//						}
						if("getPosts".equals(function)){ //data[2].equals("getPosts")
							String id = regex[1];
							startActivity(new Intent(getActivity(),
									LMCreateActivity.class).putExtra("user", id.trim()));
						}
						if("blockUser".equals(function)){ //data[2].equals("blockUser")
							String id = regex[1];
							startActivity(new Intent(getActivity(),
									BlockListActivity.class).putExtra("user", id.trim()));
						}
					}
//					settings.edit().remove("tlock").commit();
                	return true;
                }
                else {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url)); 
					startActivity(i);
                    return true;
                }
            }        	
        });
		
		ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isAvailable()) {
			LoadPost lp = new LoadPost();
			String[] params = {id, page, token};
			lp.execute(params);
		}
		else{
			web.loadDataWithBaseURL(null, "<div style='position: absolute;top: 50%;left: 50%;margin-top: -50px;margin-left: -50px;width: 150px;height: 100px;'><span style='color:#FFFFFF'>上唔到網呀大哥...</span></div>", "text/html", "utf-8", null);
		}
		return v;
	}
	
	public class LoadPost extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... uid) {		
			return getPost(uid[0], uid[1], uid[2]);				
		}

		public String getPost(String topic  , String page, String token){
			WebAccess wa = new WebAccess();
			return wa.getPostData(topic, page, token);		
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			ArrayList<Content> ctList = new ArrayList<Content>();
			JSONObject json;
			try {
				json = new JSONObject(result);
				JSONObject	topic = json.getJSONObject("topic");
				rated = Boolean.valueOf(json.get("rated").toString());
				if (rated){
					parent.menu.findItem(R.id.action_good).setEnabled(false);
					parent.menu.findItem(R.id.action_bad).setEnabled(false);
				}
				title = topic.getString("title");
				parent.title = title;
				
				int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
				TextView titleView = (TextView) parent.findViewById(titleId);
				titleView.setEllipsize(TruncateAt.MARQUEE);
				titleView.setMarqueeRepeatLimit(1);
		        // in order to start strolling, it has to be focusable and focused
				titleView.setFocusable(true);
				titleView.setFocusableInTouchMode(true);
				titleView.requestFocus();
				
				tlock = topic.getInt("tlock");
				parent.tlock = tlock;
				parent.setTitle(title);
				
				parent.f_ident = topic.getString("f_ident");
				String color = ColorUtils.getColorByIdent(getActivity(), parent.f_ident);
				
				if(!dark){
					parent.getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
						SystemBarTintManager tintManager = new SystemBarTintManager(parent);
						tintManager.setStatusBarTintEnabled(true);
						int colorInt = dark==true ? Color.BLACK : Color.parseColor(color);
						tintManager.setStatusBarTintColor(colorInt);
					}
					((View) parent.mIndicator).setBackgroundColor(ColorUtils.colorTone(color, 0.2f, 1.5f));
					((TitlePageIndicator) parent.mIndicator).setFooterColor(ColorUtils.colorTone(color, 1f, 0.5f));
					((TitlePageIndicator) parent.mIndicator).setFooterLineHeight(1 * parent.density); //1dp
					((TitlePageIndicator) parent.mIndicator).setFooterIndicatorHeight(3 * parent.density); //3dp
					((TitlePageIndicator) parent.mIndicator).setTextColor(0xAA000000);
					((TitlePageIndicator) parent.mIndicator).setSelectedColor(0xFF000000);
					((TitlePageIndicator) parent.mIndicator).setSelectedBold(true);
				}else{
					((View) parent.mIndicator).setBackgroundColor(ColorUtils.colorTone(color, 1f, 0.5f));
					((TitlePageIndicator) parent.mIndicator).setFooterColor(ColorUtils.colorTone(color, 1f, 1f));
					((TitlePageIndicator) parent.mIndicator).setFooterLineHeight(1 * parent.density); //1dp
					((TitlePageIndicator) parent.mIndicator).setFooterIndicatorHeight(3 * parent.density); //3dp
					((TitlePageIndicator) parent.mIndicator).setTextColor(0xAAFFFFFF);
					((TitlePageIndicator) parent.mIndicator).setSelectedColor(0xFFFFFFFF);
					((TitlePageIndicator) parent.mIndicator).setSelectedBold(true);
				}
				
				
				Content ct = new Content();
				ct.setUsername(topic.getString("uname"));
				parent.uname = ct.getUsername();
				ct.setSex(topic.getString("gender"));
				ct.setTime(topic.getString("ctime"));
				ct.setContent(topic.getString("content"));
				ct.setT_id(topic.getString("id"));
				ct.setQ_id(topic.getString("id"));
				ct.setTopic(title);
				ct.setIsTopic(true);
				ct.setUserId(topic.getString("uid"));
				ct.setGood(topic.getString("good"));
				ct.setBad(topic.getString("bad"));
				ct.setBadge(topic.getString("badge"));
				
				parent.good = ct.getGood();
				parent.bad = ct.getBad();
				
				parent.menu.findItem(R.id.action_good).setTitle(getString(R.string.action_good) + " (" + parent.good + ")");
				parent.menu.findItem(R.id.action_bad).setTitle(getString(R.string.action_bad) + " (" + parent.bad + ")");
				
				if(page.equals("1"))
					ctList.add(ct);
				
				int repliesTotal = Integer.parseInt(topic.getString("count"), 10);
				
				//cal page number
				int remainder =  repliesTotal % 25 ;
				if(remainder ==0)
					parent.numberOfPages =  repliesTotal / 25;
				else
					parent.numberOfPages =  repliesTotal / 25 + 1 ;
				
				adapter.setCount(repliesTotal);
				
				if (repliesTotal>0){
					
					JSONArray Replies = json.getJSONArray("replys");
					//get blocked user
					try {
						JSONArray blockeduser = json.getJSONArray("blockeduser");
						List<String> blockedList = new ArrayList<String>();
						for(int i = 0; i < blockeduser.length(); i++)
							blockedList.add(blockeduser.getString(i));
						
						for (int i = 0; i < Replies.length(); i++) {
							final JSONObject jsonItem = Replies.getJSONObject(i);
							if(!blockedList.contains(jsonItem.getString("uid"))){
								ct = new Content();
								ct.setUsername(jsonItem.getString("uname"));
								ct.setSex(jsonItem.getString("gender"));
								ct.setTime(jsonItem.getString("r_time"));
								ct.setContent(jsonItem.getString("content"));
								ct.setT_id(topic.getString("id"));
								ct.setQ_id(jsonItem.getString("r_id"));
								ct.setTopic(title);
								ct.setIsTopic(false);
								ct.setUserId(jsonItem.getString("uid"));
								ct.setBadge(jsonItem.getString("badge"));
								ctList.add(ct);
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
						for (int i = 0; i < Replies.length(); i++) {
							final JSONObject jsonItem = Replies.getJSONObject(i);
							ct = new Content();
							ct.setUsername(jsonItem.getString("uname"));
							ct.setSex(jsonItem.getString("gender"));
							ct.setTime(jsonItem.getString("r_time"));
							ct.setContent(jsonItem.getString("content"));
							ct.setT_id(topic.getString("id"));
							ct.setQ_id(jsonItem.getString("r_id"));
							ct.setTopic(title);
							ct.setIsTopic(false);
							ct.setUserId(jsonItem.getString("uid"));
							ct.setBadge(jsonItem.getString("badge"));
							ctList.add(ct);
						}
					}
				}
				//set content
				String fullHTML = new ContentAdapter(getActivity(), dark , token).toHTML(ctList, color);
				
				WebSettings webSettings = web.getSettings();
				webSettings.setDefaultTextEncodingName("UTF-8");
				webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
				webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
				web.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
				web.loadDataWithBaseURL(null, fullHTML, "text/html", "utf-8", null);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

	}
	
}
