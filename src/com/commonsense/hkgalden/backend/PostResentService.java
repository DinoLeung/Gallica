package com.commonsense.hkgalden.backend;

import com.commonsense.hkgaldenPaid.R;
import com.commonsense.hkgalden.util.DatabaseManager;
import com.commonsense.hkgalden.util.SystemUtils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;

public class PostResentService extends Service {

	public class uploadTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			
			DatabaseManager.init(PostResentService.this);
			//List<OOrder> oo = DatabaseManager.getInstance().getUnsendOrder();
			//Toast.makeText(EOrderSubmitService.this, "Unsend Order : " + String.valueOf(oo.size()), 1).show();
			//Log.i("Unsend Order:", String.valueOf(oo.size()));
			
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager
					.getActiveNetworkInfo();

			boolean connected = activeNetworkInfo != null
					&& activeNetworkInfo.isAvailable()
					&& activeNetworkInfo.isConnected();

			if (connected) {
			
				submitPost();
			
			} else {
				
				createNotification(" Submit :" + SystemUtils.getNow(), "No Network");
			}
			
			// TODO Auto-generated method stub
			return null;
		}
	

		protected void onCancelled() {
			createNotification("Posting Interrupted", "Please resend later");
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			SharedPreferences sp = getSharedPreferences("post", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("submit", "N");
			editor.commit();
			
			System.out.println("Stop Running service!");
			
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}
	
	private Handler handler = new Handler();
	
	private uploadTask task = null;

	private Runnable submitPost = new Runnable() {
		
		public void run() {
			
			SharedPreferences sp = getSharedPreferences("post", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("submit", "Y");
			editor.commit();
			System.out.println("Start Running Posting service!");
			task = new uploadTask();
			task.execute();
	        handler.postDelayed(this, 1000*60*10);
	    }
	};
	
	public void createNotification(String contentTitle, String contentText) {
		
		NotificationManager mNotificationManager;
		Notification.Builder builder;
		Context mContext;
		int NOTIFICATION_ID = SystemUtils.generateFNumber();
		Notification mNotification;

		mContext = this.getApplicationContext();
		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		builder = new Notification.Builder(mContext);
		// Build the notification using Notification.Builder
		builder.setAutoCancel(true);
		mNotification = builder.getNotification();
		mNotification.icon = R.drawable.ic_launcher;
		//mNotification.number = 2 << 2;
		mNotification.when = System.currentTimeMillis();
		mNotification.tickerText = contentTitle;
		mNotification.defaults |= Notification.DEFAULT_LIGHTS  | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
		mNotification.flags |= Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL;
		// PendingIntent pendingIntent = PendingIntent.getActivity(parent,
		// 0, parent.getIntent(), 0);
		mNotification.setLatestEventInfo(this, contentTitle, contentText, null);
		mNotificationManager.notify(NOTIFICATION_ID, mNotification);
		// mNotificationManager.cancel(NOTIFICATION_ID);
	}
	    
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public void onDestroy() {
		//handler.removeCallbacks(submitOrder);
		
		if(task != null)
	    {
	      if(!task.isCancelled())
	        task.cancel(true);
	    }
		super.onDestroy();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		
		//task = new uploadTask();
		//task.execute();
		handler.postDelayed(submitPost, 1000);
		super.onStart(intent, startId);
	}
	
	
	public boolean submitOrder(String refno) {

		try {
				//submit Post / Reply at poor network condition

			return true;

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		}

	}

	


	
	private boolean submitPost(){
		
		String refno = "";
		String orderno = "";
		String salesid = "";
		
		
		return true;
	}
	

	
}
