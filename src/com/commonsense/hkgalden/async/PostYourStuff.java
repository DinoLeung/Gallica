package com.commonsense.hkgalden.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.commonsense.hkgalden.ui.MainActivity;
import com.commonsense.hkgalden.web.WebAccess;

public class PostYourStuff  extends AsyncTask<String, Void, Boolean>{
	

	private ProgressDialog dialog;
	private final Activity user; 
	private String ident;


	public PostYourStuff(final Activity parent) {
		dialog = new ProgressDialog(parent);	 
		user = parent;

	}

	@Override
	protected Boolean doInBackground(String... uid) {	
	
		return sendPost(uid[0]);				
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
		
	    this.user.startActivity(new Intent(user, MainActivity.class).putExtra("ident", ident).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.dialog.setTitle("HKGalden Posting");
		this.dialog.setMessage("Post is Submitting....");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}
	
	public boolean sendPost(String data){			
		WebAccess wa = new WebAccess();
		Log.d("argument" , data);
		String result = wa.postData(data);
		String [] res =result.split("\\|");
		ident = res[0];
		String statusCode = res[1];
		return (statusCode ==String.valueOf(200));				
	}

	
}


