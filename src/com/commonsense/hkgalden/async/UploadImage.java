package com.commonsense.hkgalden.async;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.commonsense.hkgaldenPaid.R;
import com.commonsense.hkgalden.util.SystemUtils;
import com.commonsense.hkgalden.web.WebAccess;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.EditText;

public class UploadImage extends AsyncTask<File, Void, String>{

	private ProgressDialog dialog;
	private Activity activity;


	public UploadImage(final Activity parent) {
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
		this.dialog.setMessage("Image is uploading....");
		this.dialog.setCancelable(false);
		this.dialog.show();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		System.out.println(result);
		try {
			JSONObject json = new JSONObject(result);
			JSONObject jsonData = json.getJSONObject("data");
			String jsonDataUrl = jsonData.getString("url").toString();
			String formatted = "[img]"+jsonDataUrl+"[/img]";
			SystemUtils.toast(this.activity.getApplicationContext(), formatted);
			EditText edtContent = (EditText)this.activity.findViewById(R.id.content_post);
			edtContent.append(formatted);
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
	protected String doInBackground(File... uid) {
		//		submitAllBuildResult(uid[0]);

		return new WebAccess().getData(uid[0]);
	}
}
