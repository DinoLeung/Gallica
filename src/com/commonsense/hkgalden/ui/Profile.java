package com.commonsense.hkgalden.ui;


import org.json.JSONException;
import org.json.JSONObject;

import com.commonsense.hkgaldenPaid.R;
import com.commonsense.hkgalden.util.SystemUtils;
import com.commonsense.hkgalden.web.WebAccess;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Profile extends Activity {

	private Button submit;
	private Button unreg;
	private EditText txtEmail;
	private EditText txtPassword;
	private TextView txtId;
	private TextView txtName;
	private String email;
	private String password;
	private TextView tokenText;
	private int statusCode;
	public static final String preference = "LoginPrefs";
	static loginTask lt;
	static logoutTask logoutt;
	private String tok;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SharedPreferences settings = getSharedPreferences(preference, 0);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Profile.this);
		boolean dark  =  preferences.getBoolean("dark", false);
		
		if(dark){
			setContentView(R.layout.profile_dark);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				Window win = getWindow();
				WindowManager.LayoutParams winParams = win.getAttributes();
				final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
				winParams.flags |= bits;
				win.setAttributes(winParams);
				SystemBarTintManager tintManager = new SystemBarTintManager(this);
				tintManager.setStatusBarTintEnabled(true);
				tintManager.setStatusBarTintColor(Color.BLACK);
			}
		}
		else{
			setContentView(R.layout.profile);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#777777")));
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				Window win = getWindow();
				WindowManager.LayoutParams winParams = win.getAttributes();
				final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
				winParams.flags |= bits;
				win.setAttributes(winParams);
				SystemBarTintManager tintManager = new SystemBarTintManager(this);
				tintManager.setStatusBarTintEnabled(true);
				tintManager.setStatusBarTintColor(Color.parseColor("#777777"));
			}
		}
		
		setTitle("帳戶");

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		final String token = settings.getString("tokenplus", "");
		tok=token;
		tokenText = (TextView)findViewById(R.id.text_token);
		tokenText.setText(token);
		
		txtId = (TextView)findViewById(R.id.text_id);
		txtId.setText(settings.getString("id", "冧巴"));
		txtName = (TextView)findViewById(R.id.text_name);
		txtName.setText(settings.getString("username", "膠名"));
		txtEmail = (EditText)findViewById(R.id.email);
		txtEmail.setText(settings.getString("email", ""));
		txtPassword = (EditText)findViewById(R.id.password);
		txtPassword.setText(settings.getString("password", ""));		
		txtPassword.setOnKeyListener(new OnKeyListener()
		{

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				boolean handled = false;
				if (keyCode == KeyEvent.KEYCODE_ENTER)
				{
					// Handle pressing "Enter" key here
					start();
					handled = true;
				}
				return handled;

			}
		});
		
		submit = (Button)this.findViewById(R.id.login);
		
		if (token.equals("")){
			submit.setText("登入");
		}else{submit.setText("登出");}

		submit.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				//get login id and password
				
				/*
				email = txtEmail.getText().toString();
				password = txtPassword.getText().toString();
								
				if (email.equals("") || password.equals("")){
					SystemUtils.toast(getApplication(), "請輸入電郵和密碼!");
				}
				
				else if (token.equals("")){
					lt = new loginTask(getApplication());
					String[] execute = {email, password};
					lt.execute(execute);
				}
				else{
					logoutt = new logoutTask(getApplication());
					logoutt.execute();
					SystemUtils.toast(getApplication(), "已成功登出!");
				}*/
				start();
			}
		});
	}
	
	private void start(){
		email = txtEmail.getText().toString();
		password = txtPassword.getText().toString();
						
		if (email.equals("") || password.equals("")){
			SystemUtils.toast(getApplication(), "請輸入電郵和密碼!");
		}
		
		else if (tok.equals("")){
			lt = new loginTask(getApplication());
			String[] execute = {email, password};
			lt.execute(execute);
		}
		else{
			logoutt = new logoutTask(getApplication());
			logoutt.execute();
			SystemUtils.toast(getApplication(), "已成功登出!");
		}
		
	}
	public class loginTask extends AsyncTask<String, String, Boolean> {
		
		private ProgressDialog dialog;
		
		public loginTask(final Application application) {
			dialog = new ProgressDialog(application);

		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(Profile.this, "撈緊", "等陣...", true, false);
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
				SystemUtils.toast(getApplication(), "已成功登入!");
				onCreate(new Bundle());
				View v=Profile.this.getCurrentFocus();
			    if(v==null)
			        return;				    
				InputMethodManager inputManager = (InputMethodManager) Profile.this.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			else if (statusCode == 403){
				SystemUtils.toast(getApplication(), "Login Fail! \nWrong Password or Wrong Password");
			}
			else SystemUtils.toast(getApplication(), "Login Fail! Error Code: " + Integer.toString(statusCode));
		}


		@Override
		protected Boolean doInBackground(String... params) {
			return getData(params[0], params[1]);
		}
	}
	
	public Boolean getData(String email, String password){
		
		WebAccess wa = new WebAccess();

		String[] loginResult = wa.login(email,password);
		
		statusCode = Integer.parseInt(loginResult[0]);
		
		if (statusCode == 200){
			
			String userInfo = wa.getUserData(loginResult[1]);
			
			//convert JSON string (resultString) into string array
			String[] info = new String[2];
			try {
				JSONObject json = new JSONObject(userInfo);
				
				JSONObject jsonObject = json.getJSONObject("data");
				JSONObject jsonItem = jsonObject.getJSONObject("user");
				
				info[0] = jsonItem.getString("id");
				info[1] = jsonItem.getString("username");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			int[] expiryDate = SystemUtils.getExpiryDay();
			
			//save
			SharedPreferences settings = getSharedPreferences(preference, 0);
			
			SharedPreferences d_settings = PreferenceManager.getDefaultSharedPreferences(this);
			
			settings.edit().putString("tokenplus", loginResult[1]).commit();
			settings.edit().putString("email", email).commit();
			settings.edit().putString("password", password).commit();
			settings.edit().putString("id", info[0]).commit();
			settings.edit().putString("username", info[1]).commit();
			//lm_message
			d_settings.edit().putString("lm_message", info[1]+"BB 一鍵留名！").commit();
			
			settings.edit().putInt("expiryyear", expiryDate[0]).commit();
			settings.edit().putInt("expirymonth", expiryDate[1]).commit();
			settings.edit().putInt("expiryday", expiryDate[2]).commit();
			
			return true;
		}
		else{
			return false;
		}
	}
	
public class logoutTask extends AsyncTask<String, String, Boolean> {
		
		private ProgressDialog dialog;
		
		public logoutTask(final Application application) {
			dialog = new ProgressDialog(application);

		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(Profile.this, "撈緊", "等陣...", true, false);
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
				onCreate(new Bundle());
				
			}
			else SystemUtils.toast(getApplication(), "unreg Fail! Error code: " + Integer.toString(statusCode));
		}


		@Override
		protected Boolean doInBackground(String... params) {
			return unreg();
		}
	}
	
	public boolean unreg(){
		SharedPreferences settings = getSharedPreferences(preference, 0);
		String token = settings.getString("tokenplus", "");
		WebAccess wa = new WebAccess();
		String string = wa.logout(token);
		statusCode = Integer.valueOf(string.substring(0, 2));
		String msg = string.substring(3, string.length());
		if (msg.equals("{\"msg\":\"unreg_success\"}")){
			
			settings.edit().remove("password").commit();
			settings.edit().remove("id").commit();
			settings.edit().remove("username").commit();
			settings.edit().remove("tokenplus").commit();
			
			settings.edit().remove("expiryyear").commit();
			settings.edit().remove("expirymonth").commit();
			settings.edit().remove("expiryday").commit();
			
			return true;
		}
		return false;
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

}