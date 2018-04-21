package com.commonsense.hkgalden.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.commonsense.hkgaldenPaid.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.commonsense.hkgalden.adapter.DialogPic;
import com.commonsense.hkgalden.adapter.DialogPicAdapter;
import com.commonsense.hkgalden.adapter.TopicAdapter;
import com.commonsense.hkgalden.adapter.Topics;
import com.commonsense.hkgalden.async.DialogActivity;
import com.commonsense.hkgalden.async.DialogActivity.DownloadDialog;
import com.commonsense.hkgalden.backend.BlockUserResponseReceiver;
import com.commonsense.hkgalden.backend.RateResponseReceiver;
import com.commonsense.hkgalden.backend.UnBlockUserResponseReceiver;
import com.commonsense.hkgalden.model.BlockedUser;
import com.commonsense.hkgalden.ui.ChannelFragment.LoadPost;
import com.commonsense.hkgalden.util.DatabaseManager;
import com.commonsense.hkgalden.util.GaldenUtils;
import com.commonsense.hkgalden.web.WebAccess;
import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class LMCreateActivity extends Activity {


	private ArrayList<Topics> listTopics = new ArrayList<Topics>();
	private EditText txtUserId;
	private Button add;
	private ListView list;
	private ArrayAdapter<Integer> adapter;
	private ArrayList<Integer> userIdList = new ArrayList<Integer> (); 
	private BlockUserResponseReceiver breceiver;
	private UnBlockUserResponseReceiver ubreceiver;
	private String token ;
	private SharedPreferences settings;
	private String userid;

	private TopicAdapter dataAdapter;
	static LoadBlockedUser mp;
	boolean loading = false;
	static int currentPage = 1;
	private int index = 0;
	private int top = 0;
	private String ident;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


		setContentView(R.layout.lmlist);
		setTitle(getResources().getString(R.string.action_lm_op));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		String page = "1";
		settings = getSharedPreferences("LoginPrefs", 0);
		userid = getIntent().getExtras().getString("user");
		token = settings.getString("tokenplus", "");
		add = (Button) this.findViewById(R.id.buttonAdd);
		txtUserId = (EditText) this.findViewById(R.id.editTextUser);
		txtUserId.setText(userid);

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

		list = (ListView)this. findViewById(R.id.b_list);

		String[] tokens = {page , userid , token};
		mp = new LoadBlockedUser(LMCreateActivity.this);
		mp.execute(tokens);

		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(txtUserId.getText().toString().length() > 0 ){
					startActivity(new Intent().
							setClass(LMCreateActivity.this, LMCreateActivity.class).
							putExtra("user" , txtUserId.getText().toString())
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				}
			}});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy()
	{		
		super.onDestroy();
	}


	public class LoadBlockedUser extends AsyncTask<String, Void , String>{


		private ProgressDialog dialog;
		private Activity activity;

		public LoadBlockedUser(final Activity parent) {
			//dialog = new ProgressDialog(parent);
			dialog = new ProgressDialog(parent);	 
			activity = parent;
		}


		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			this.dialog.setMessage("Loading HKGalden BLock List....");
			this.dialog.setCancelable(false);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				System.out.println("restul");
				System.out.println(result);
				JSONObject json = new JSONObject(result);
				
				try{
					JSONArray jsonArray = json.getJSONArray("topics");

					//listTopics = new ArrayList<Topics> (); 
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonItem = jsonArray.getJSONObject(i);

						String countryInfo =jsonItem.toString();
						Topics country = new Gson().fromJson(countryInfo, Topics.class);
						listTopics.add(country);
						dataAdapter = new TopicAdapter(activity , 
								R.layout.topiclist_detail, listTopics);

						dataAdapter.notifyDataSetChanged();
						list.setAdapter(dataAdapter);
						list.setCacheColorHint(0);
						list.setSelectionFromTop(index, top);
						loading = false;		


					}
					list.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {


							final Topics st =  (Topics) arg0.getItemAtPosition(arg2);

							int page = 1;
							startActivity(new Intent(activity, ReadPost.class)		
							.putExtra("topic" ,st.getId())
							.putExtra("page", String.valueOf(page)));
						}
					});

					//long click item
					list.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> parent, View view,
								final int position, long id) {
							// TODO Auto-generated method stub
							final Topics st =  (Topics) parent.getItemAtPosition(position);

							Intent intent = new Intent(Intent.ACTION_SEND);
							intent.setType("text/plain");
							intent.putExtra(Intent.EXTRA_TEXT, GaldenUtils.postExternalLink + st.getId());
							intent.putExtra(android.content.Intent.EXTRA_SUBJECT, st.getTle());
							startActivity(Intent.createChooser(intent, "Share"));

							return true;
						}
					});


					//over scroll
					list.setOnScrollListener(new OnScrollListener() {

						@Override
						public void onScroll(AbsListView view, int firstVisibleItem,
								int visibleItemCount, int totalItemCount) {

							Log.v("Hi", "first:" + firstVisibleItem + " visible:" + visibleItemCount + " total:" + totalItemCount);
							if (!loading && firstVisibleItem + visibleItemCount >= totalItemCount ) {

								//	Log.v("Hi", "Come in lar");
								index = list.getFirstVisiblePosition();
								View v = list.getChildAt(0);
								top = (v == null) ? 0 : v.getTop();
								currentPage++;
								String[] execute = { String.valueOf(currentPage) , userid , token};
								System.out.println("currentPage : " + currentPage);
								new LoadBlockedUser(LMCreateActivity.this).execute(execute);
								loading = true;
							}
						}

						@Override
						public void onScrollStateChanged(AbsListView view,
								int scrollState) {
							// TODO Auto-generated method stub

							switch (scrollState) {
							case OnScrollListener.SCROLL_STATE_IDLE:
								// when list scrolling stops

								break;
							case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
								break;
							case OnScrollListener.SCROLL_STATE_FLING:
								break;
							}

						}
					});

				}catch (JSONException jsonex){
					String jsonEroorMessage = json.getString("error");
					Toast.makeText(activity, jsonEroorMessage, Toast.LENGTH_LONG).show();
				//	activity.finish();
				}
			

			}catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//Edited by Larry Lo for checking
				//ExceptionLogWriter.writeLog(e , getActivity());
			}

			if (dialog!=null) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
				dialog=null;
			}
		}

		@Override
		protected String doInBackground(String... uid) {
			//		submitAllBuildResult(uid[0]);
			String page = uid[0]; 
			String userid = uid[1];
			String token = uid[2]; 
			System.out.println("1:"  + page );
			System.out.println("1:"  + userid );
			System.out.println("1:"  + token );
			return getPostList(uid[0] , uid[1] , uid[2]);
		}
	}

	public String getPostList(String page , String  userid, String token ) {
		WebAccess wa = new WebAccess();
		String result  ="";
		result = wa.getPostRecord(page, userid, token);	
		loading = true;
		return result;
	}



}
