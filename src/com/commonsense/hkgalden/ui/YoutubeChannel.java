package com.commonsense.hkgalden.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.commonsense.hkgaldenPaid.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class YoutubeChannel extends Activity {
	
	String name[] = {"膠登音樂", "TC_@膠登音樂", "妁小汀@膠登音樂"};
	String link[] = {"http://gdata.youtube.com/feeds/api/playlists/PLF5Xa96kaky8rZUUNqpEI-Vo4mTEQJ8Iw?v=2&alt=json", "http://gdata.youtube.com/feeds/api/playlists/PLeb3tpu4_FW9UimC7wbwPJ5xrEn2eik26?v=2&alt=json", "http://gdata.youtube.com/feeds/api/playlists/PLeb3tpu4_FW8mbErsfOmIeZGAex2WMtdZ?v=2&alt=json"};
	private SimpleAdapter listItemAdapter;
	private ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.y_channel_layout);
		
		ListView list = (ListView) findViewById(R.id.listYoutube);
		
		list.setSelector(R.drawable.list_selector);

		
		for (int i = 0; i < name.length; i++){
			HashMap<String, String> item = new HashMap<String, String>();
			
			item.put("name", name[i]);
			item.put("link", link[i]);
			
			
			listItem.add(item);
			
			listItemAdapter = new SimpleAdapter(getApplication(), listItem,R.layout.y_channel_rows,
					new String[] {"name"},
					new int[] {R.id.y_name});
			listItemAdapter.notifyDataSetChanged();

			list.setAdapter(listItemAdapter);
		}
		
		//Click item
				list.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						String name = listItem.get(arg2).get("name").toString();
						String link = listItem.get(arg2).get("link").toString();
						

						String message = name + "|" + link;
					//	SystemUtils.toast(getApplication(), message);
						startActivity(new Intent(getApplication(), YoutubeListActivity.class)
						.putExtra("link", link )
						);
					}
				});
		
	}
	

}
