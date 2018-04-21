package com.commonsense.hkgalden.adapter;

import java.io.File;
import java.util.List;

import com.commonsense.hkgaldenPaid.R;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class IconsAdapter extends ArrayAdapter {

	/** Contacts list */
	private List contacts;

	/** Context */
	private Context context;
	
	/** View ID */
	private int textViewResourceId;
	
	public IconsAdapter(Context context, int textViewResourceId,
			List icons) {
		super(context, textViewResourceId, icons);
		this.context = context;
		this.contacts = icons;
		this.textViewResourceId  = textViewResourceId;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// Keeps reference to avoid future findViewById()
		ContactsViewHolder viewHolder;

		if (v == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.icon_single, parent, false);
			viewHolder = new ContactsViewHolder();
			viewHolder.imgIcon = (ImageView) v.findViewById(R.id.icon);	
			v.setTag(viewHolder);
		} else {
			viewHolder = (ContactsViewHolder) v.getTag();
		}
		
		final Icons content = (Icons) contacts.get(position);
		if (content != null) {	
			//ImageView view =viewHolder.imgIcon ;
			ImageView view = viewHolder.imgIcon ;
			Picasso.with(context).load(new File(content.getCode())).resize(100, 100).placeholder(R.drawable.ic_launcher).into(view);
//			Picasso.with(context).load(new File(content.getCode())).resize(100, 100).placeholder(R.drawable.ic_launcher).centerCrop().into(view);
		}
		
		return v;
	}

	static class ContactsViewHolder {
		ImageView imgIcon;
	}
}