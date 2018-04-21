package com.commonsense.hkgalden.async;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.commonsense.hkgalden.adapter.DialogPic;
import com.commonsense.hkgalden.adapter.DialogPicAdapter;
import com.commonsense.hkgalden.web.WebAccess;
import com.commonsense.hkgaldenPaid.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class DialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diapic_list_layout);

		//String topicId = "https://spreadsheets.google.com/feeds/list/0ApEyQph2NSyNdDA2WmNpR2tPQkdsR2JtRnYwcy1NRHc/6/public/basic?alt=json";
		String topicId = "https://spreadsheets.google.com/feeds/list/0AqFbGbqKGPjAdGNPdGUtQ3dlSmoydU45ZXd6cGE4eGc/1/public/basic?alt=json";
		String[] execute = {topicId};
		new DownloadDialog(this).execute(execute);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	public class DownloadDialog extends AsyncTask<String, Void , String>{


		private ProgressDialog dialog;
		private Activity activity;

		public DownloadDialog(final Activity parent) {
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
				JSONArray jsonArray = jsonData.getJSONArray("entry");
				//Log.d("testing", jsArrayString);


				final GridView listView = (GridView) DialogActivity.this.findViewById(R.id.list);

				ArrayList<DialogPic> ct = new ArrayList<DialogPic> ();
				for(int i = 0 ; i < jsonArray.length() ; i++){
					DialogPic dpDialogPic = new DialogPic ();

					JSONObject jsD = jsonArray.getJSONObject(i);			
					JSONObject jsC = jsD.getJSONObject("content");
					String jsV = jsC.get("$t").toString();
					int length = jsV.length();
					String urlString = jsV.substring(6, length);
					//Log.d("testing individual link",  urlString);	
					//
					
				
					dpDialogPic.setCode(urlString);
					ct.add(dpDialogPic);
				}
				//	ArrayAdapter<String> itemAdapter = new ArrayAdapter<String>(this, R.layout.dialogpic_detail, dialogA);
				DialogPicAdapter itemAdapter = new DialogPicAdapter(DialogActivity.this, R.layout.diapic_row, ct);
				//final DialogPic selectedDialogPic = new DialogPic(); 
				listView.setAdapter(itemAdapter);
				listView.setCacheColorHint(0);
				listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
						//do something on click
						DialogPic	selectedDialogPic = (DialogPic) listView.getItemAtPosition(position);
						Intent i = new Intent();
						i.putExtra("dialog", selectedDialogPic.getCode() );
						setResult(RESULT_OK, i);
						finish();
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
