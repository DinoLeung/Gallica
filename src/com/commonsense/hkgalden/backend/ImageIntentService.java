package com.commonsense.hkgalden.backend;



import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread.State;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.ByteArrayBuffer;




import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;


public class ImageIntentService extends IntentService {

	public ImageIntentService() {
		super("");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			//Uri data = intent.getData();
			String urlPath = intent.getStringExtra("urlpath");
			URL url = new URL(urlPath);
			File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Gallica"  );
			if(!folder.exists()){
				folder.mkdirs();
			}
			File imageFolder = new File(folder.getAbsolutePath() + File.separator + "Photos"  );
			if(!imageFolder.exists()){
				imageFolder.mkdirs();
			}
			
			String imageName = urlPath.substring(urlPath.lastIndexOf("/"), urlPath.length());
			File file = new File(imageFolder.getAbsolutePath() + File.separator +  imageName );
			if(!file.exists()){
				URLConnection con = url.openConnection();
				int contentLength = con.getContentLength();
				//	InputStream stream = con.getInputStream();
				DataInputStream stream = new DataInputStream(url.openStream());
				byte[] buff = new byte[contentLength];
				stream.readFully(buff);
				stream.close();
				FileOutputStream outputStream = new FileOutputStream(file);
				DataOutputStream fos = new DataOutputStream(outputStream);
				fos.write(buff);
				fos.flush();
				fos.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}



}