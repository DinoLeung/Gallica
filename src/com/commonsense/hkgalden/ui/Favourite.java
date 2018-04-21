package com.commonsense.hkgalden.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.commonsense.hkgaldenPaid.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.commonsense.hkgalden.util.DatabaseManager;
import com.commonsense.hkgalden.util.SystemUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class Favourite extends Activity {
	
	private ArrayList<HashMap<String, String>> listItem;
	private SimpleAdapter listItemAdapter;
	private com.commonsense.hkgalden.model.History history = new com.commonsense.hkgalden.model.History();
	private List<com.commonsense.hkgalden.model.Favourite> favourite = new ArrayList<com.commonsense.hkgalden.model.Favourite>();
	private Bundle instanceState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		instanceState = savedInstanceState;
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Favourite.this);
		boolean dark  =  preferences.getBoolean("dark", false);
		
		setTitle(getResources().getString(R.string.action_favourite));
		
		if(dark){
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				Window win = getWindow();
				WindowManager.LayoutParams winParams = win.getAttributes();
				final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
				winParams.flags |= bits;
				win.setAttributes(winParams);
				SystemBarTintManager tintManager = new SystemBarTintManager(this);
				tintManager.setStatusBarTintEnabled(true);
				tintManager.setStatusBarTintColor(Color.BLACK);
			}
		}
		else{
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#777777")));
			
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				Window win = getWindow();
				WindowManager.LayoutParams winParams = win.getAttributes();
				final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
				winParams.flags |= bits;
				win.setAttributes(winParams);
				SystemBarTintManager tintManager = new SystemBarTintManager(this);
				tintManager.setStatusBarTintEnabled(true);
				tintManager.setStatusBarTintColor(Color.parseColor("#777777"));
			}
		}
		
		ListView list = (ListView) findViewById(R.id.history_list);
		
		
		DatabaseManager.init(getApplication());
		
		favourite = DatabaseManager.getInstance().getAllFavourite();
		
		listItem = new ArrayList<HashMap<String, String>>();
		
		for (int i = 0; i < favourite.size(); i++){
			HashMap<String, String> item = new HashMap<String, String>();
			String f = favourite.get(i).getId();
			
			history = DatabaseManager.getInstance().getHistory(f);
			
			item.put("t_id", history.getId());
			item.put("title", history.getTitle());
			item.put("uname", history.getUname());
			item.put("page", "睇到第 "+history.getPage()+" 頁");
			item.put("pageNumber", history.getPage());
			item.put("ident", history.getIdent());
			item.put("date",  history.getDate().toString());
			
			listItem.add(item);
			
			if(dark)
				listItemAdapter = new SimpleAdapter(getApplication(), listItem,R.layout.history_detail_dark,
						new String[] {"title", "uname", "page"},
						new int[] {R.id.textViewTopic, R.id.textViewUser, R.id.textViewPage});
			else
				listItemAdapter = new SimpleAdapter(getApplication(), listItem,R.layout.history_detail,
						new String[] {"title", "uname", "page"},
						new int[] {R.id.textViewTopic, R.id.textViewUser, R.id.textViewPage});
			
			listItemAdapter.notifyDataSetChanged();
			
			list.setAdapter(listItemAdapter);
			
			String string = item.get("t_id")+" | "+item.get("title")+" | "+item.get("uname")+" | "+item.get("page")+" | "+item.get("ident")+" | "+item.get("date");
			Log.i("SQLite", string);
			
			
		}
		
		//Click item
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String id = listItem.get(arg2).get("t_id").toString();
				String tle = listItem.get(arg2).get("title").toString();
				String uname = listItem.get(arg2).get("uname").toString();
				String page = listItem.get(arg2).get("page").toString();
				String pageNumber = listItem.get(arg2).get("pageNumber").toString();
				String ident = listItem.get(arg2).get("ident").toString();
				String date = listItem.get(arg2).get("date").toString();

				String message = id + "|" + tle + "|" + uname + "|" + page+ "|" + ident + "|"+ date;
				SystemUtils.toast(getApplication(), message);
				startActivity(new Intent(getApplication(), ReadPost.class)
				.putExtra("topic", id )
//				.putExtra("sCol", startCol)
//				.putExtra("eCol", endCol)
				.putExtra("page", pageNumber));
			}
		});
		
		//long click item
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				            //Yes button clicked
				        	DatabaseManager.getInstance().delFavourite(favourite.get(position));
				        	onCreate(instanceState);
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
				            //No button clicked
				            break;
				        }
				    }
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(Favourite.this);
				builder.setMessage("刪除呢項記錄???").setPositiveButton("吹咩?!", dialogClickListener)
				    .setNegativeButton("你就輪了!", dialogClickListener).show();
				return true;
			}
		});
		
	    SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                		list,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                            	
                        		//listItemAdapter.notifyDataSetChanged();
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                            		DatabaseManager.getInstance().delFavourite(favourite.get(position));
                                	listItem.remove(listItemAdapter.getItem(position));
                                }
                                listItemAdapter.notifyDataSetChanged();
                            }
                        });
	    list.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
	    list.setOnScrollListener(touchListener.makeScrollListener());
        
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
