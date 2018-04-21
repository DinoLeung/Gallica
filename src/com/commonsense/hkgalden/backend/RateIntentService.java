package com.commonsense.hkgalden.backend;

import com.commonsense.hkgalden.util.GaldenUtils;
import com.commonsense.hkgalden.web.WebAccess;



import android.app.IntentService;
import android.content.Intent;

public class RateIntentService extends IntentService {

	public RateIntentService() {
		super("");
		// TODO Auto-generated constructor stub
	}




	@Override
	protected void onHandleIntent(Intent intent) {

		try {

			//Uri data = intent.getData();
			String argument = intent.getStringExtra("argument");
			String currentTopicId =  argument.split("\\|")[0];
			WebAccess wa = new WebAccess();
			String result = wa.postRate(argument);
			int statusCode = Integer.parseInt(result);
			String resilr  = statusCode == 200 ? wa.getRate(currentTopicId, String.valueOf(1)) : "HTTP Error";
			//	Toast.makeText(getApplication(), resilr, Toast.LENGTH_LONG).show();
			//intent = new Intent(GaldenUtils.RATE_BROADCAST_ACTION);
		


			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(GaldenUtils.RATE_BROADCAST_ACTION);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra(RateResponseReceiver.key, resilr);
			sendBroadcast(broadcastIntent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}



}