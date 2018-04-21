package com.commonsense.hkgalden.backend;

import org.json.JSONException;
import org.json.JSONObject;

import com.commonsense.hkgalden.ui.BlockListActivity;
import com.commonsense.hkgalden.util.GaldenUtils;
import com.commonsense.hkgalden.util.SystemUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class BlockUserResponseReceiver extends BroadcastReceiver {
	public static final String ACTION_RESP =   GaldenUtils.BLOCK_BROADCAST_ACTION;
	public static final String key = "feedback";

	@Override
	public void onReceive(Context context, Intent intent) {
		String text = intent.getStringExtra(key); 
		try {
			JSONObject json = new JSONObject(text);
			String status = json.getString("status");
			if("true".equals(status)){
				JSONObject jsonMes = json.getJSONObject("blocked");
				String id = jsonMes.getString("id");
				String username = jsonMes.getString("username");
				SystemUtils.toast(context, id+ " " +username + " is blocked");
				context.startActivity(new Intent().setClass
						(context, BlockListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("user", ""));
			}
		//	SystemUtils.toast(context, text);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}