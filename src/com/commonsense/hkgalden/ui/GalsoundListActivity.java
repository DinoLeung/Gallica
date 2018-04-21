package com.commonsense.hkgalden.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.commonsense.hkgalden.adapter.YoutubePlay;
import com.commonsense.hkgalden.adapter.YoutubePlayAdapter;
import com.commonsense.hkgalden.web.WebAccess;
import com.commonsense.hkgaldenPaid.R;
import com.squareup.picasso.Picasso;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GalsoundListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.y_list_layout);
		String urlString = "http://api.soundcloud.com/users/59420760/playlists.json?client_id=ff9ed5093f7a0302e7633bdfa77dd3f5";

	//	new GalsoundList(this).execute(urlString);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


public class GalsoundList extends AsyncTask<String, Void , String>{


	private ProgressDialog dialog;
	private Activity activity;

	public GalsoundList(final Activity parent) {
		//dialog = new ProgressDialog(parent);
		dialog = new ProgressDialog(parent);	 
		activity = parent;

	}
	

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.dialog.setMessage("Dialog Picture is downloading....");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			JSONObject json = new JSONObject(result);
			JSONObject jsonData = json.getJSONObject("feed");
			JSONObject jsonData2 = jsonData.getJSONObject("title");
			
			TextView tvTitleTextView  = (TextView) GalsoundListActivity.this.findViewById(R.id.tvTite);			
			tvTitleTextView.setText(jsonData2.getString("$t").toString());
			
			TextView tvAuthTextView  = (TextView) GalsoundListActivity.this.findViewById(R.id.tvAuthr);
			JSONArray jsonDataAuthor = jsonData.getJSONArray("author");
			 JSONObject author = jsonDataAuthor.getJSONObject(0).getJSONObject("name");
			 String authorName = author.getString("$t").toString();
			tvAuthTextView.setText(authorName);
			
			JSONObject jsonMedia = jsonData.getJSONObject("media$group");
			JSONObject jsonDesc = jsonMedia.getJSONObject("media$description");
			String jsDesp = jsonDesc.get("$t").toString();
			
			JSONArray jstsyThumbArray = jsonMedia.getJSONArray("media$thumbnail");				
			String firstThumeString = jstsyThumbArray.getJSONObject(1).getString("url");
		
			/*TextView tvDespextView  = (TextView) YoutubeListActivity.this.findViewById(R.id.tvSDesp);
			tvDespextView.setText(jsDesp);*/
			Button btn = (Button)GalsoundListActivity.this.findViewById(R.id.button1);
			final String desp = jsDesp;
			btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog dlg = new AlertDialog.Builder(GalsoundListActivity.this)
					//.setView(R.layout.textview)
					.setTitle("頻道介紹")
					.setMessage(desp)
					.create(); 
					dlg.show();
				}}) ;
			//	tvDespextView.setText(jsDesp);
			ImageView imgYou  = (ImageView) GalsoundListActivity.this.findViewById(R.id.imageViewS);
			
			Picasso.with(GalsoundListActivity.this).load(firstThumeString).resize(320, 160).centerCrop().noFade().into(imgYou);		
			
			JSONArray jsonArray = jsonData.getJSONArray("entry");			
	
			final ListView listView = (ListView) GalsoundListActivity.this.findViewById(R.id.list);
			
			ArrayList<YoutubePlay> ct = new ArrayList<YoutubePlay> ();
			for(int i = 0 ; i < jsonArray.length() ; i++){
				YoutubePlay dpDialogPic = new YoutubePlay ();
				
				JSONObject jsD = jsonArray.getJSONObject(i);			
				JSONObject jsC = jsD.getJSONObject("media$group");
				JSONObject jsE = jsC.getJSONObject("media$title");
				String jsV = jsE.get("$t").toString();
				JSONObject jsyoutube = jsC.getJSONObject("yt$videoid");
				String jsyoutubeId = jsyoutube.getString("$t") ;
				JSONArray jstyThumbArray = jsC.getJSONArray("media$thumbnail");				
				JSONObject thumeString = jstyThumbArray.getJSONObject(1);
				Log.d("sds", thumeString.toString());
				String thumbString = thumeString.getString("url");
				dpDialogPic.setCode(jsyoutubeId);
				dpDialogPic.setName(jsV);
				dpDialogPic.setPic(thumbString);
				ct.add(dpDialogPic);
			}
			//	ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, R.layout.dialogpic_detail, dialogA);
			YoutubePlayAdapter itemAdapter = new YoutubePlayAdapter(GalsoundListActivity.this, R.layout.y_row, ct);
			//final DialogPic selectedDialogPic = new DialogPic(); 
			listView.setAdapter(itemAdapter);
			listView.setCacheColorHint(0);
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
					//do something on click
					YoutubePlay	selectedDialogPic = (YoutubePlay) listView.getItemAtPosition(position);
					Log.d("rr", selectedDialogPic.getCode());
					Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+selectedDialogPic.getCode()));
					startActivity(i);
						
				}
			});

			
			
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//Edited by Larry Lo for checking
			//ExceptionLogWriter.writeLog(e , getActivity());
		}
		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		if (dialog!=null) {
			dialog=null;
		}

		
	}

	@Override
	protected String doInBackground(String... uid) {
		//		submitAllBuildResult(uid[0]);

		return new WebAccess().getGoogleDocs(uid[0]);
	}
}

}
