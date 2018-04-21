package com.commonsense.hkgalden.adapter;

import java.util.List;

import com.commonsense.hkgaldenPaid.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class DialogPicAdapter extends ArrayAdapter {

	/** Contacts list */
	private List contacts;

	/** Context */
	private Context context;
	
	public DialogPicAdapter(Context context, int textViewResourceId,
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
			v = li.inflate(R.layout.diapic_row, parent, false);
			viewHolder = new ContactsViewHolder();
			viewHolder.imgIcon = (ImageView) v.findViewById(R.id.imageView1);	
			v.setTag(viewHolder);
		} else {
			viewHolder = (ContactsViewHolder) v.getTag();
		}
		
		
		
		final DialogPic content = (DialogPic) contacts.get(position);
		if (content != null) {	
			//ImageView view =viewHolder.imgIcon ;
			ImageView view =viewHolder.imgIcon ;
			String ss = content.getCode();	
		
			if("http://img3.wikia.nocookie.net/__cb20140306063440/evchk/images/6/66/UiP6vjX.gif".equals(ss)){				
				Picasso.with(context).load(R.drawable.upj).resize(500, 500).placeholder(R.drawable.ic_stub).centerCrop().into(view);	
			}else{
				Picasso.with(context).load(content.getCode()).resize(500, 500).placeholder(R.drawable.ic_stub).centerCrop().into(view);		
			}
		}
		return v;
	}

	static class ContactsViewHolder {
		ImageView imgIcon;
	}

}