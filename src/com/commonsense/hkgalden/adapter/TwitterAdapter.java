package com.commonsense.hkgalden.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.commonsense.hkgaldenPaid.R;
import com.commonsense.hkgalden.twitter.Tweet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TwitterAdapter extends ArrayAdapter {

	/** Contacts list */
	private List contacts;

	/** Context */
	private Context context;
	
	public TwitterAdapter(Context context, int textViewResourceId,
			List icons) {
		super(context, textViewResourceId, icons);
		this.context = context;
		this.contacts = icons;
	
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// Keeps reference to avoid future findViewById()
		ContactsViewHolder viewHolder;

		if (v == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.read_traffic, parent, false);
			viewHolder = new ContactsViewHolder();
			viewHolder.txContent = (TextView) v.findViewById(R.id.userName);
			viewHolder.txtDate = (TextView) v.findViewById(R.id.content);
			v.setTag(viewHolder);
		} else {
			viewHolder = (ContactsViewHolder) v.getTag();
		}

		final Tweet content = (Tweet) contacts.get(position);
		if (content != null) {
			viewHolder.txContent.setText(content.getText());
			String dateString = content.getDateCreated();
			viewHolder.txtDate.setText(twitterHumanFriendlyDate(dateString));
		}
		return v;
	}

	static class ContactsViewHolder {
		TextView txContent;
		TextView txtDate ;
	}


	public static String twitterHumanFriendlyDate(String dateStr) {
		// parse Twitter date
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
		dateFormat.setLenient(false);
		Date created = null;
		try {
			created = dateFormat.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
 
		// today
		Date today = new Date();
 
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
			return "約1小時前";
		}
 
		if (duration < day) {
			int n = (int) Math.floor(duration / hour);
			return n + " 小時前";
		}
		if (duration > day && duration < day * 2) {
			return "昨日";
		}
 
		if (duration < day * 365) {
			int n = (int) Math.floor(duration / day);
			return n + " 日 前";
		} else {
			return "over a year ago";
		}
	}
	

	

}