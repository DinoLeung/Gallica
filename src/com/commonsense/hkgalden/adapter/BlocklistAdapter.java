package com.commonsense.hkgalden.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

public class BlocklistAdapter extends ArrayAdapter {

	/** Contacts list */
	private List contacts;

	/** Context */
	private Context context;

	public BlocklistAdapter(Context context, int textViewResourceId,
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
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
			boolean dark  =  preferences.getBoolean("dark", false);		
			if(dark==true){			
				LayoutInflater li = (LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.blocklist_dark, parent, false);			
			}else{
				LayoutInflater li = (LayoutInflater) getContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.blocklist, parent, false);
			}
			viewHolder = new ContactsViewHolder();
			viewHolder.txTitle = (TextView) v.findViewById(R.id.textViewTopic);
			viewHolder.txUsername = (TextView) v.findViewById(R.id.textViewUser);
			v.setTag(viewHolder);
		} else {
			viewHolder = (ContactsViewHolder) v.getTag();
		}

		final Blocklist content = (Blocklist) contacts.get(position);
		if (content != null) {
			viewHolder.txTitle.setText(content.getUsername());
			viewHolder.txUsername.setText(content.getId()); 

		}
		return v;
	}

	static class ContactsViewHolder {
	    TextView txTitle;
	    TextView txUsername;		
	}

}