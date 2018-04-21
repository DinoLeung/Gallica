package com.commonsense.hkgalden.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.commonsense.hkgalden.ui.ReadPost;
import com.commonsense.hkgalden.util.DatabaseManager;
import com.commonsense.hkgalden.web.WebAccess;

public class ReplyYourStuff  extends AsyncTask<String, Void, Boolean>{
	

	private ProgressDialog dialog;
	private final Activity user;
	private String postId;
	private String postContent;
	private String token;

	public ReplyYourStuff(final Activity parent) {
		dialog = new ProgressDialog(parent);	 
		user = parent;

	}

	@Override
	protected Boolean doInBackground(String... uid) {
		Log.i("ready","doInBackground");
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
		
		DatabaseManager.init(user);
		com.commonsense.hkgalden.model.History historyData = DatabaseManager.getInstance().getHistory(postId);
		
		int page = 1;
		if (historyData != null)
			page = Integer.valueOf(historyData.getPage());
		this.user.startActivity(new Intent(user, ReadPost.class)
		.putExtra("topic" ,postId )
		.putExtra("page", String.valueOf(page)).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Log.i("ready","onPreExecute");
		this.dialog.setTitle("HKGalden Posting");
		this.dialog.setMessage("Reply is Submitting....");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}
	
	public boolean sendPost(String argument){
		
		
		String[] dataSet = argument.split("\\|");
		postId = dataSet[0];
		postContent = dataSet[1];
		token = dataSet[2];
		
		Log.i("ready",postId+" "+postContent+" "+token);
//		ident = dataSet[3];
//		Log.i("ready",postId+" "+postContent+" "+token+" "+ident);
		
		String data = postId + "|" + postContent+ "|" + token;
		WebAccess wa = new WebAccess();
		Log.d("argument" , data);
		String result = wa.postReply(data);
		Log.i("", "finish WebAccess");
		return (result == String.valueOf(200));				
	}

	
}
