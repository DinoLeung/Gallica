package com.commonsense.hkgalden.adapter;

import java.util.List;

import com.commonsense.hkgaldenPaid.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class YoutubePlayAdapter extends ArrayAdapter {

	/** Contacts list */
	private List contacts;

	/** Context */
	private Context context;
	
	public YoutubePlayAdapter(Context context, int textViewResourceId,
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
			v = li.inflate(R.layout.y_row, parent, false);
			viewHolder = new ContactsViewHolder();
			viewHolder.imgIcon = (ImageView) v.findViewById(R.id.imageView1);	
			viewHolder.txtTitle = (TextView) v.findViewById(R.id.textView1);	
			v.setTag(viewHolder);
		} else {
			viewHolder = (ContactsViewHolder) v.getTag();
		}
		
		
		final YoutubePlay content = (YoutubePlay) contacts.get(position);
		if (content != null) {	

			viewHolder.txtTitle.setText(content.getName());
			ImageView view =viewHolder.imgIcon;
			Log.d("ss", content.getPic());
			Picasso.with(context).load(content.getPic()).resize(320, 160).centerCrop().noFade().into(view);		
		}
		return v;
	}

	static class ContactsViewHolder {
		ImageView imgIcon;
		TextView txtTitle;
	}

}