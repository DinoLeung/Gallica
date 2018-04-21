package com.commonsense.hkgalden.backend;

import com.commonsense.hkgalden.util.GaldenUtils;
import com.commonsense.hkgalden.util.SystemUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class RateResponseReceiver extends BroadcastReceiver {
	public static final String ACTION_RESP =   GaldenUtils.RATE_BROADCAST_ACTION;
	public static final String key = "feedback";

	@Override
	public void onReceive(Context context, Intent intent) {
		String text = intent.getStringExtra(key);
		SystemUtils.toast(context, text);
		//context.startActivity(intent);
	}
}