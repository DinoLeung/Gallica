package com.commonsense.hkgalden.backend;

import com.commonsense.hkgalden.util.GaldenUtils;
import com.commonsense.hkgalden.web.WebAccess;



import android.app.IntentService;
import android.content.Intent;

public class UnblockUserIntentService extends IntentService {

	public UnblockUserIntentService() {
		super("");
		// TODO Auto-generated constructor stub
	}




	@Override
	protected void onHandleIntent(Intent intent) {

		try {

			//Uri data = intent.getData();
			String arg = intent.getStringExtra("argument");
			String [] argument = arg.split("\\|");
			WebAccess wa = new WebAccess();
			String result = wa.postUnblock(argument[0], argument[1]);
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(GaldenUtils.UNBLOCK_BROADCAST_ACTION);
			broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
			broadcastIntent.putExtra(UnBlockUserResponseReceiver.key, result);
			sendBroadcast(broadcastIntent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}



}