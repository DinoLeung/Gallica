package com.commonsense.hkgalden.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.commonsense.hkgaldenPaid.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TopicAdapter extends ArrayAdapter<Topics> {

	/** Contacts list */
	private ArrayList<Topics> contacts;

	/** Context */
	private Context context;

	public TopicAdapter(Context context, int textViewResourceId,
			ArrayList<Topics> listTopics) {
		super(context, textViewResourceId, listTopics);
		this.context = context;
		this.contacts = listTopics;

	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// Keeps reference to avoid future findViewById()
		ContactsViewHolder viewHolder;

		if (v == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
			boolean dark  =  preferences.getBoolean("dark", false);		
			if(dark==true){			
				LayoutInflater li = (LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.topiclist_detail_dark, parent, false);			
			}else{
				LayoutInflater li = (LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.topiclist_detail, parent, false);
			}
			viewHolder = new ContactsViewHolder();
			viewHolder.txTitle = (TextView) v.findViewById(R.id.textViewTopic);
			viewHolder.txUsername = (TextView) v.findViewById(R.id.textViewUser);
			viewHolder.txRating = (TextView) v.findViewById(R.id.textViewRating);
			viewHolder.txReplies = (TextView) v.findViewById(R.id.textViewReplies);
			viewHolder.txDate = (TextView) v.findViewById(R.id.textViewTime);
			v.setTag(viewHolder);
		} else {
			viewHolder = (ContactsViewHolder) v.getTag();
		}

		final Topics content = (Topics) contacts.get(position);
		if (content != null) {
			viewHolder.txTitle.setText(content.getTle());
			viewHolder.txUsername.setText(content.getUname() + "--"); 
			viewHolder.txRating.setText("[" + content.getRate() + "評分]") ;
			viewHolder.txReplies.setText("[" +content.getCount() + "回覆]");

			//viewHolder.txContent.setText(content.getText());
			String dateString = content.getLtime();
			//twitterHumanFriendlyDate(dateString);
			//viewHolder.txDate.setText("最後回覆" + dateString);
			viewHolder.txDate.setText("最後回覆 " + twitterHumanFriendlyDate(dateString));
			
		}
		return v;
	}

	static class ContactsViewHolder {
	    TextView txTitle;
	    TextView txUsername;		
		TextView txRating;
		TextView txReplies;
		TextView txDate;
	}

	public static String twitterHumanFriendlyDate(String dateStr) {
		// parse Twitter date
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		dateFormat.setLenient(false);
		Date created = null;
		try {
			created = dateFormat.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
 
		// today
		TimeZone.setDefault(TimeZone.getTimeZone("Hongkong"));
		Date today = new Date();
		
		Log.i("today", today.toString());
		Log.i("created", created.toString());
 
		// how much time since (ms)
		Long duration = today.getTime() - created.getTime();
 
		int second = 1000;
		int minute = second * 60;
		int hour = minute * 60;
		int day = hour * 24;
 
		if (duration < second * 7) {
			return "剛剛";
		}
 
		if (duration < minute) {
			int n = (int) Math.floor(duration / second);
			return n + " 秒前";
		}
 
		if (duration < minute * 2) {
			return "約  1 分鐘前";
		}
 
		if (duration < hour) {
			int n = (int) Math.floor(duration / minute);
			return n + " 分鐘前 ";
		}
 
		if (duration < hour * 2) {
			return "約 1 小時前";
		}
 
		if (duration < day) {
			int n = (int) Math.floor(duration / hour);
			return n + " 小時前";
		}
		if (duration > day && duration < day * 2) {
			return " 昨日";
		}
 
		if (duration < day * 365) {
			int n = (int) Math.floor(duration / day);
			return n + " 日 前";
		} else {
			return "over a year ago";
		}
	}
	

}