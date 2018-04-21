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
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.commonsense.hkgalden.adapter.Blocklist;
import com.commonsense.hkgalden.adapter.BlocklistAdapter;
import com.commonsense.hkgalden.backend.BlockUserIntentService;
import com.commonsense.hkgalden.backend.BlockUserResponseReceiver;
import com.commonsense.hkgalden.backend.UnBlockUserResponseReceiver;
import com.commonsense.hkgalden.backend.UnblockUserIntentService;
import com.commonsense.hkgalden.web.WebAccess;
import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class BlockListActivity extends Activity {


	private EditText txtUserId;
	private Button add;
	private ListView list;
	private BlockUserResponseReceiver breceiver;
	private UnBlockUserResponseReceiver ubreceiver;
	private String token ;
	private SharedPreferences settings;
	private BlocklistAdapter dataAdapter;
	private 	List<Blocklist> listBlock;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


		setContentView(R.layout.blist);
		setTitle(getResources().getString(R.string.action_blist));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// enable ActionBar app icon to behave as action to toggle nav drawer

		//getActionBar().hide();

		add = (Button) this.findViewById(R.id.buttonAdd);
		txtUserId = (EditText) this.findViewById(R.id.editTextUser);
		
		String userid = getIntent().getExtras().getString("user");
		if (!userid.equals(null))
			txtUserId.setText(userid);

		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String userid = txtUserId.getText().toString().trim();
				SharedPreferences	settings = getSharedPreferences("LoginPrefs", 0);
				String token = settings.getString("tokenplus", "");
				startService(
						new Intent().setClass(BlockListActivity.this, BlockUserIntentService.class)
						.putExtra("argument", userid+"|"+token));
			}});
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window win = getWindow();
			WindowManager.LayoutParams winParams = win.getAttributes();
			final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
			winParams.flags |= bits;
			win.setAttributes(winParams);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintColor(Color.parseColor("#444444"));
		}


		IntentFilter filter = new IntentFilter(BlockUserResponseReceiver.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		breceiver = new BlockUserResponseReceiver();
		registerReceiver(breceiver, filter);

		IntentFilter filter2 = new IntentFilter(UnBlockUserResponseReceiver.ACTION_RESP);
		filter2.addCategory(Intent.CATEGORY_DEFAULT);
		ubreceiver = new UnBlockUserResponseReceiver();
		registerReceiver(ubreceiver, filter2);

		list = (ListView)this. findViewById(R.id.b_list);

		settings = getSharedPreferences("LoginPrefs", 0);
		token = settings.getString("tokenplus", "");
		String[] tokens = {token};
		new LoadBlockedUser(BlockListActivity.this).execute(tokens);
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
//		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
//		
//		String blocklistString = "" ;
//		int length = listBlock.size();
//		for(int i = 0 ; i < length ; i++){
//			
//			Blocklist bl = listBlock.get(i);
//			String argument = i==length-1 ? bl.getId() : bl.getId() + ";";
//			blocklistString += argument ;
//		}
//		settings.edit().putString("blocklist", blocklistString ).commit();
//		settings.edit().putString("blocklist", blocklistString ).apply();

		//DatabaseManager.getInstance().close();
		unregisterReceiver(breceiver);
		unregisterReceiver(ubreceiver);
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
					JSONArray jsonArray = json.getJSONArray("blocklist");

					listBlock = new ArrayList<Blocklist> ();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonItem = jsonArray.getJSONObject(i);
						String blockList =jsonItem.toString();
						Blocklist country = new Gson().fromJson(blockList, Blocklist.class);
						listBlock.add(country);
						dataAdapter = new BlocklistAdapter(activity,
								R.layout.blocklist, listBlock);
						dataAdapter.notifyDataSetChanged();
						list.setAdapter(dataAdapter);
						list.setCacheColorHint(0);
					}

					list.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {
							Blocklist selected = (Blocklist) arg0.getItemAtPosition(arg2);
							StringBuffer message = new StringBuffer();
							String words = message.append("User :").append(selected.getUsername()).
									append(" uid:").append(selected.getId()).toString();
							Toast.makeText(activity, words, Toast.LENGTH_LONG).show();
						}
					});

					//long click item
					list.setOnItemLongClickListener(new OnItemLongClickListener() {
						@Override
						public boolean onItemLongClick(AdapterView<?> parent, View view,
								final int position, long id) {
							// TODO Auto-generated method stub
							final Blocklist selected = (Blocklist)parent.getItemAtPosition(position);
							final String userud = selected.getId();
							DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									switch (which){
									case DialogInterface.BUTTON_POSITIVE:
										//Yes button clicked
										String userid = userud;
										SharedPreferences	settings = getSharedPreferences("LoginPrefs", 0);
										String token = settings.getString("tokenplus", "");

										activity.startService(
												new Intent().setClass(activity, UnblockUserIntentService.class)
												.putExtra("argument", userid+"|"+token));
										break;

									case DialogInterface.BUTTON_NEGATIVE:
										//No button clicked
										break;
									}
								}
							};

							AlertDialog.Builder builder = new AlertDialog.Builder(BlockListActivity.this);
							builder.setMessage(getResources().getString(R.string.balert_delete_title))
							.setPositiveButton(getResources().getString(R.string.balert_delete_ok), dialogClickListener)
							.setNegativeButton(getResources().getString(R.string.balert_delete_cancel), dialogClickListener).show();
							return true;
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
			return new WebAccess().getBlockedUserData(uid[0]);
		}
	}


}
